package com.iftekhar.ai_paradox.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iftekhar.ai_paradox.dto.CtEvaluationRequest;
import com.iftekhar.ai_paradox.dto.CtEvaluationResult;
import com.iftekhar.ai_paradox.model.CtEvaluation;
import com.iftekhar.ai_paradox.model.CtQuestion;
import com.iftekhar.ai_paradox.model.CtSkill;
import com.iftekhar.ai_paradox.repository.CtEvaluationRepository;
import com.iftekhar.ai_paradox.repository.CtQuestionRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CtScoringService {
    private final ChatClient chatClient;
    private final CtQuestionRepository questionRepository;
    private final CtEvaluationRepository evaluationRepository;
    private final SurveyService surveyService;

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

    public CtScoringService(ChatClient.Builder chatClientBuilder, CtQuestionRepository questionRepository, CtEvaluationRepository evaluationRepository, SurveyService surveyService) {
        this.chatClient = chatClientBuilder.build();
        this.questionRepository = questionRepository;
        this.evaluationRepository = evaluationRepository;
        this.surveyService = surveyService;
    }

    public CtEvaluationResult evaluate(CtEvaluationRequest request) {
        CtQuestion q = questionRepository.findById(request.getQuestionId())
                .orElseThrow(()->new IllegalArgumentException("Unknown question ID"));

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
                request.getStudentAnswer()
        );

        Message systemMessage = new SystemMessage(SYSTEM_PROMPT);
        Message userMessage = new UserMessage(userPrompt);

        String content = chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(userPrompt)
                .call().content();; // JSON string

        CtEvaluationResult eval;

        try {
            // Map snake_case -> camelCase automatically via @JsonProperty if needed
            eval = objectMapper.readValue(content, CtEvaluationResult.class);

        } catch (Exception e) {

            eval = new CtEvaluationResult();
            eval.setQuestionId(q.getId());
            eval.setScore(0);
            eval.setOnTopic(false);
            eval.setReason("Failed to parse model response: " + e.getMessage());
            eval.setStrength("");
            eval.setWeakness("");
        }
        CtEvaluation entity = new CtEvaluation();
        entity.setSurveyId(request.getSurveyId());          // link to survey_forms.id
        entity.setQuestion(q);
        entity.setStudentAnswer(request.getStudentAnswer());
        entity.setScore(eval.getScore());
        entity.setOnTopic(eval.isOnTopic());
        entity.setReason(eval.getReason());
        entity.setStrength(eval.getStrength());
        entity.setWeakness(eval.getWeakness());
        entity.setEvalModel("gpt-4o-mini");

        // skills map → JSON string
        try {
            if (eval.getSkills() != null) {
                String skillsJson = objectMapper.writeValueAsString(eval.getSkills());
                entity.setSkillsJson(skillsJson);
            }
        } catch (Exception e) {
            // if serialization fails, ignore skillsJson but still save row
            entity.setSkillsJson(null);
        }

        evaluationRepository.save(entity);

        // 7) Return result to caller (for UI)
        return eval;
    }
}
