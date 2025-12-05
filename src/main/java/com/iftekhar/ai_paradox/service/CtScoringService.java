package com.iftekhar.ai_paradox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iftekhar.ai_paradox.dto.*;
import com.iftekhar.ai_paradox.model.CtEvaluation;
import com.iftekhar.ai_paradox.model.CtQuestion;
import com.iftekhar.ai_paradox.model.CtSkill;
import com.iftekhar.ai_paradox.repository.CtEvaluationRepository;
import com.iftekhar.ai_paradox.repository.CtQuestionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CtScoringService {
    private final ChatClient chatClient;
    private final CtQuestionRepository questionRepository;
    private final CtEvaluationRepository evaluationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String SYSTEM_PROMPT = """
        You are an examiner marking a university-level critical thinking assessment 
        similar to formal CT tests.

        You will receive:
        - One question
        - A marking guideline for that question
        - The maximum score for that question
        - A student's written answer

        Your tasks:
        1. Decide if the answer is on-topic.
        2. Use ONLY the given rubric to assign a score from 0 to maxScore.
        3. Focus on critical thinking skills (interpretation, analysis, evaluation,
           inference, explanation, self-regulation) rather than grammar.
        4. Penalise shallow, descriptive, or off-topic answers.
        5. Reward inference, justification, alternatives, and reflexivity.

        Output rules:
        - Output ONLY valid JSON. No extra text.
        - JSON structure:
          {
            "questionId": "string",
            "score": integer (0-maxScore),
            "on_topic": true/false,
            "skills": { "<skillName>": 0-10, ... },
            "reason": "1–3 short sentences explaining the score",
            "strength": "one clear strength",
            "weakness": "one clear weakness or improvement area"
          }

        Skills:
        - Only output skills that are relevant for the question (they will be listed to you).
        - Skill scores must be consistent with the overall score.
        """;

    private static final String SYSTEM_PROMPT_BATCH = """
        You are an examiner marking a university-level critical thinking assessment.

        You will receive:
        - A surveyId
        - A list of items. Each item has:
          - questionId
          - questionText
          - rubric
          - maxScore
          - studentAnswer

        For EACH item:
        1. Decide if the answer is on-topic.
        2. Use ONLY the given rubric to assign a score from 0 to maxScore.
        3. Focus on critical thinking rather than grammar.
        4. Penalise shallow, descriptive, or off-topic answers.
        5. Reward inference, justification, alternatives, and reflexivity.

        Output rules:
        - Output ONLY valid JSON.
        - JSON structure:
          {
            "surveyId": <number>,
            "evaluations": [
              {
                "questionId": "string",
                "score": integer (0-maxScore),
                "on_topic": true/false,
                "skills": { "<skillName>": 0-10, ... },
                "reason": "1–3 short sentences explaining the score",
                "strength": "one clear strength",
                "weakness": "one clear weakness or improvement area"
              }
            ]
          }
        """;

    public CtScoringService(ChatClient.Builder chatClientBuilder, CtQuestionRepository questionRepository, CtEvaluationRepository evaluationRepository, SurveyService surveyService) {
        this.chatClient = chatClientBuilder.build();
        this.questionRepository = questionRepository;
        this.evaluationRepository = evaluationRepository;
    }

    /**
     * Evaluate a single answer (UPDATE existing ct_evaluation record)
     * This is rarely used now - batch evaluation is preferred
     */
    @Transactional
    public CtEvaluationResult evaluate(CtEvaluationRequest request) {
        log.info("Evaluating survey: {}, question: {}", request.getSurveyId(), request.getQuestionId());

        // FIXED: Changed from findBySurveyIdAndQuestion_Id to findBySurveyIdAndQuestionId
        CtEvaluation existingEval = evaluationRepository
                .findBySurveyIdAndQuestionId(request.getSurveyId(), request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No answer found for survey " + request.getSurveyId() +
                                " and question " + request.getQuestionId()
                ));

        CtQuestion q = existingEval.getQuestion();

        String skillsList = q.getSkills()
                .stream()
                .map(CtSkill::getCode)
                .map(String::toLowerCase)
                .collect(Collectors.joining(", "));

        String userPrompt = """
            questionId: %s

            Question:
            %s

            Marking guideline:
            %s

            Maximum score for this question: %d

            Relevant CT skills to score (if visible in the answer):
            %s

            Student answer:
            %s

            Return ONLY the JSON as specified in the system instructions.
            """.formatted(
                q.getId(),
                q.getText(),
                q.getRubric(),
                q.getMaxScore(),
                skillsList,
                existingEval.getStudentAnswer()  // Use answer from DB
        );

        String content = chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(userPrompt)
                .call().content();

        CtEvaluationResult eval;

        try {
            eval = objectMapper.readValue(content, CtEvaluationResult.class);
        } catch (Exception e) {
            log.error("Failed to parse AI response", e);
            eval = new CtEvaluationResult();
            eval.setQuestionId(q.getId());
            eval.setScore(0);
            eval.setOnTopic(false);
            eval.setReason("Failed to parse model response: " + e.getMessage());
            eval.setStrength("");
            eval.setWeakness("");
        }

        // UPDATE existing record with evaluation results
        existingEval.setScore(eval.getScore());
        existingEval.setOnTopic(eval.isOnTopic());
        existingEval.setReason(eval.getReason());
        existingEval.setStrength(eval.getStrength());
        existingEval.setWeakness(eval.getWeakness());
        existingEval.setEvalModel("gpt-4o-mini");

        // skills map → JSON string
        try {
            if (eval.getSkills() != null) {
                String skillsJson = objectMapper.writeValueAsString(eval.getSkills());
                existingEval.setSkillsJson(skillsJson);
            }
        } catch (Exception e) {
            log.error("Failed to serialize skills", e);
            existingEval.setSkillsJson(null);
        }

        evaluationRepository.save(existingEval);  // UPDATE, not INSERT

        log.info("Evaluation completed for survey: {}, question: {}, score: {}",
                request.getSurveyId(), request.getQuestionId(), eval.getScore());

        return eval;
    }

    /**
     * Evaluate all answers for a survey at once (PREFERRED METHOD)
     */
    @Transactional
    public BatchEvaluationResult evaluateSurvey(BatchEvaluationRequest request) {
        log.info("Starting batch evaluation for survey: {}", request.getSurveyId());

        try {
            // Load existing answers from ct_evaluation
            List<CtEvaluation> existingAnswers = evaluationRepository
                    .findBySurveyIdWithQuestion(request.getSurveyId());

            if (existingAnswers.isEmpty()) {
                log.warn("No answers found for survey: {}", request.getSurveyId());
                return fail(request.getSurveyId(), "NO_ANSWERS",
                        "No answers found for survey " + request.getSurveyId());
            }

            // Check if already evaluated
            boolean alreadyEvaluated = existingAnswers.stream()
                    .allMatch(e -> e.getScore() != null);

            if (alreadyEvaluated) {
                log.warn("Survey {} has already been evaluated", request.getSurveyId());
                return fail(request.getSurveyId(), "ALREADY_EVALUATED",
                        "Survey has already been evaluated. Scores exist.");
            }

            // Build prompt from existing answers
            StringBuilder sb = new StringBuilder();
            sb.append("surveyId: ").append(request.getSurveyId()).append("\n\n");
            sb.append("items:\n");

            int index = 1;
            for (CtEvaluation eval : existingAnswers) {
                CtQuestion q = eval.getQuestion();
                sb.append(index++).append(")\n");
                sb.append("questionId: ").append(q.getId()).append("\n");
                sb.append("questionText:\n").append(q.getText()).append("\n\n");
                sb.append("rubric:\n").append(q.getRubric()).append("\n\n");
                sb.append("maxScore: ").append(q.getMaxScore()).append("\n\n");
                sb.append("studentAnswer:\n").append(eval.getStudentAnswer()).append("\n\n");
            }

            log.debug("Sending evaluation request to AI model");

            String content = chatClient
                    .prompt()
                    .system(SYSTEM_PROMPT_BATCH)
                    .user(sb.toString())
                    .call()
                    .content();

            log.debug("Received response from AI model");

            BatchEvaluationResult parsed = objectMapper.readValue(content, BatchEvaluationResult.class);

            // Update each evaluation record
            for (CtEvaluationResult evalResult : parsed.getEvaluations()) {
                CtEvaluation existingEval = existingAnswers.stream()
                        .filter(e -> e.getQuestion().getId().equals(evalResult.getQuestionId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Question not found in answers: " + evalResult.getQuestionId()
                        ));

                existingEval.setScore(evalResult.getScore());
                existingEval.setOnTopic(evalResult.isOnTopic());
                existingEval.setReason(evalResult.getReason());
                existingEval.setStrength(evalResult.getStrength());
                existingEval.setWeakness(evalResult.getWeakness());
                existingEval.setEvalModel("gpt-4o-mini");

                if (evalResult.getSkills() != null) {
                    existingEval.setSkillsJson(objectMapper.writeValueAsString(evalResult.getSkills()));
                }

                evaluationRepository.save(existingEval);

                log.debug("Updated evaluation for question: {}, score: {}",
                        evalResult.getQuestionId(), evalResult.getScore());
            }

            log.info("Batch evaluation completed successfully for survey: {}", request.getSurveyId());

            return parsed;

        } catch (IllegalArgumentException e) {
            log.error("Invalid question ID in evaluation", e);
            return fail(request.getSurveyId(), "INVALID_QUESTION_ID", e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("JSON parsing error", e);
            return fail(request.getSurveyId(), "JSON_PARSE_ERROR", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during evaluation", e);
            return fail(request.getSurveyId(), "INTERNAL_ERROR", e.getMessage());
        }
    }

    private BatchEvaluationResult fail(Long surveyId, String code, String msg) {
        BatchEvaluationResult err = new BatchEvaluationResult();
        err.setSurveyId(surveyId);
        err.setSuccess(false);
        err.setErrorCode(code);
        err.setErrorMessage(msg);
        return err;
    }
}
