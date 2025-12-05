package com.iftekhar.ai_paradox.service;

import com.iftekhar.ai_paradox.dto.SurveyFormDTO;
import com.iftekhar.ai_paradox.model.CtEvaluation;
import com.iftekhar.ai_paradox.model.CtQuestion;
import com.iftekhar.ai_paradox.model.SurveyForm;
import com.iftekhar.ai_paradox.repository.CtEvaluationRepository;
import com.iftekhar.ai_paradox.repository.CtQuestionRepository;
import com.iftekhar.ai_paradox.repository.SurveyFormRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {
    private final SurveyFormRepository surveyFormRepository;
    private final CtQuestionRepository ctQuestionRepository;
    private final CtEvaluationRepository ctEvaluationRepository;

    /**
     * Save a new survey submission
     */
    @Transactional
    public SurveyFormDTO saveSurvey(SurveyFormDTO dto) {
        log.info("Saving survey for student: {}", dto.getStudentId());

        // Step 1: Save basic survey info to survey_forms table
        SurveyForm surveyForm = new SurveyForm();
        surveyForm.setName(dto.getName());
        surveyForm.setStudentId(dto.getStudentId());
        surveyForm.setAge(dto.getAge());
        surveyForm.setLevelOfStudy(dto.getLevelOfStudy());
        surveyForm.setLocation(dto.getLocation());
        surveyForm.setDevices(dto.getDevices());
        surveyForm.setStudyTools(dto.getStudyTools());
        surveyForm.setWritingTools(dto.getWritingTools());
        surveyForm.setNoteTools(dto.getNoteTools());
        surveyForm.setResearchTools(dto.getResearchTools());
        surveyForm.setPresentationTools(dto.getPresentationTools());
        surveyForm.setAiUsageFrequency(dto.getAiUsageFrequency());
        surveyForm.setLearnedAboutAI(dto.getLearnedAboutAI());
        surveyForm.setSubmittedByIp(dto.getSubmittedByIp());
        surveyForm.setIsCompleted(true);
        surveyForm.setCreatedAt(LocalDateTime.now());
        surveyForm.setUpdatedAt(LocalDateTime.now());

        SurveyForm savedSurvey = surveyFormRepository.save(surveyForm);
        log.info("Saved survey with ID: {}", savedSurvey.getId());

        // Step 2: Save answers to ct_evaluation table (with NULL scores initially)
        saveAnswersToEvaluationTable(savedSurvey.getId(), dto);

        dto.setId(savedSurvey.getId());
        dto.setCreatedAt(savedSurvey.getCreatedAt());
        dto.setEvaluated(false);  // ✅ New survey is not evaluated yet

        return dto;
    }

    /**
     * Save all 9 answers as ct_evaluation records (without scores)
     */
    private void saveAnswersToEvaluationTable(Long surveyId, SurveyFormDTO dto) {
        log.info("Saving answers to ct_evaluation table for survey: {}", surveyId);

        saveAnswerRecord(surveyId, "CTA-AI-01.A1", dto.getCoreProblemSummary());
        saveAnswerRecord(surveyId, "CTA-AI-01.A2", dto.getPeripheralEducation());
        saveAnswerRecord(surveyId, "CTA-AI-01.B1", dto.getImplicitAssumptions());
        saveAnswerRecord(surveyId, "CTA-AI-01.B2", dto.getArgumentAnalysis());
        saveAnswerRecord(surveyId, "CTA-AI-01.B3", dto.getEvidenceVsOpinion());
        saveAnswerRecord(surveyId, "CTA-AI-01.C1", dto.getTableInferences());
        saveAnswerRecord(surveyId, "CTA-AI-01.C2", dto.getResearchDesign());
        saveAnswerRecord(surveyId, "CTA-AI-01.D1", dto.getLearningBenefit());
        saveAnswerRecord(surveyId, "CTA-AI-01.D2", dto.getAiUsageReflection());
    }

    /**
     * Save individual answer as ct_evaluation record (score = NULL initially)
     */
    private void saveAnswerRecord(Long surveyId, String questionId, String answer) {
        if (answer == null || answer.trim().isEmpty()) {
            log.warn("Skipping empty answer for question: {}", questionId);
            return;
        }

        CtQuestion question = ctQuestionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));

        CtEvaluation evaluation = new CtEvaluation();
        evaluation.setSurveyId(surveyId);
        evaluation.setQuestion(question);
        evaluation.setStudentAnswer(answer);


        ctEvaluationRepository.save(evaluation);
        log.debug("Saved answer for question: {}", questionId);
    }

    /**
     * Get survey by ID with answers from ct_evaluation
     */
    @Transactional(readOnly = true)
    public SurveyFormDTO getSurveyById(Long id) {
        log.info("Fetching survey with ID: {}", id);

        SurveyForm survey = surveyFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found with ID: " + id));

        // Load answers from ct_evaluation
        List<CtEvaluation> evaluations = ctEvaluationRepository.findBySurveyIdWithQuestion(id);

        // Check if survey is evaluated (at least one score is not null)
        boolean isEvaluated = ctEvaluationRepository.isSurveyEvaluated(id);  // ✅ Use repository method

        return buildDTOWithAnswers(survey, evaluations, isEvaluated);  // ✅ Pass evaluation status
    }

    /**
     * Get all surveys with pagination
     */
    @Transactional(readOnly = true)
    public Page<SurveyFormDTO> getAllSurveys(Pageable pageable) {
        log.info("Fetching all surveys with pagination: {}", pageable);

        return surveyFormRepository.findAll(pageable)
                .map(survey -> {
                    // Check evaluation status for each survey
                    boolean isEvaluated = ctEvaluationRepository.isSurveyEvaluated(survey.getId());  // ✅ Check status

                    return SurveyFormDTO.builder()
                            .id(survey.getId())
                            .name(survey.getName())
                            .studentId(survey.getStudentId())
                            .age(survey.getAge())
                            .levelOfStudy(survey.getLevelOfStudy())
                            .location(survey.getLocation())
                            .devices(survey.getDevices())
                            .studyTools(survey.getStudyTools())
                            .writingTools(survey.getWritingTools())
                            .noteTools(survey.getNoteTools())
                            .researchTools(survey.getResearchTools())
                            .presentationTools(survey.getPresentationTools())
                            .useAITools(survey.getAiUsageFrequency() != null &&
                                    !survey.getAiUsageFrequency().equalsIgnoreCase("Never") ? "Yes" : "No")
                            .aiUsageFrequency(survey.getAiUsageFrequency())
                            .learnedAboutAI(survey.getLearnedAboutAI())
                            .evaluated(isEvaluated)  // ✅ Set evaluation status
                            .createdAt(survey.getCreatedAt())
                            .updatedAt(survey.getUpdatedAt())
                            .submittedByIp(survey.getSubmittedByIp())
                            .isCompleted(survey.getIsCompleted())
                            .build();
                });
    }

    /**
     * Delete a survey and its evaluations
     */
    @Transactional
    public void deleteSurvey(Long id) {
        log.info("Deleting survey with ID: {}", id);

        if (!surveyFormRepository.existsById(id)) {
            throw new RuntimeException("Survey not found with ID: " + id);
        }

        // Delete associated evaluations first (due to foreign key)
        ctEvaluationRepository.deleteBySurveyId(id);
        log.info("Deleted evaluations for survey: {}", id);

        // Delete the survey
        surveyFormRepository.deleteById(id);
        log.info("Deleted survey: {}", id);
    }

    /**
     * Build DTO with answers from evaluations
     */
    private SurveyFormDTO buildDTOWithAnswers(SurveyForm survey, List<CtEvaluation> evaluations, boolean isEvaluated) {  // ✅ Add parameter
        SurveyFormDTO dto = SurveyFormDTO.builder()
                .id(survey.getId())
                .name(survey.getName())
                .studentId(survey.getStudentId())
                .age(survey.getAge())
                .levelOfStudy(survey.getLevelOfStudy())
                .location(survey.getLocation())
                .devices(survey.getDevices())
                .studyTools(survey.getStudyTools())
                .writingTools(survey.getWritingTools())
                .noteTools(survey.getNoteTools())
                .researchTools(survey.getResearchTools())
                .presentationTools(survey.getPresentationTools())
                .useAITools(survey.getAiUsageFrequency() != null &&
                        !survey.getAiUsageFrequency().equalsIgnoreCase("Never") ? "Yes" : "No")
                .aiUsageFrequency(survey.getAiUsageFrequency())
                .learnedAboutAI(survey.getLearnedAboutAI())
                .evaluated(isEvaluated)  // ✅ Set evaluation status
                .createdAt(survey.getCreatedAt())
                .updatedAt(survey.getUpdatedAt())
                .submittedByIp(survey.getSubmittedByIp())
                .isCompleted(survey.getIsCompleted())
                .build();

        // Map answers from evaluations back to DTO fields
        for (CtEvaluation eval : evaluations) {
            String questionId = eval.getQuestion().getId();
            String answer = eval.getStudentAnswer();

            switch (questionId) {
                case "CTA-AI-01.A1" -> dto.setCoreProblemSummary(answer);
                case "CTA-AI-01.A2" -> dto.setPeripheralEducation(answer);
                case "CTA-AI-01.B1" -> dto.setImplicitAssumptions(answer);
                case "CTA-AI-01.B2" -> dto.setArgumentAnalysis(answer);
                case "CTA-AI-01.B3" -> dto.setEvidenceVsOpinion(answer);
                case "CTA-AI-01.C1" -> dto.setTableInferences(answer);
                case "CTA-AI-01.C2" -> dto.setResearchDesign(answer);
                case "CTA-AI-01.D1" -> dto.setLearningBenefit(answer);
                case "CTA-AI-01.D2" -> dto.setAiUsageReflection(answer);
                default -> log.warn("Unknown question ID: {}", questionId);
            }
        }

        return dto;
    }
}