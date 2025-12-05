package com.iftekhar.ai_paradox.service;

import com.iftekhar.ai_paradox.dto.SurveyFormDTO;
import com.iftekhar.ai_paradox.model.CtEvaluation;
import com.iftekhar.ai_paradox.model.CtQuestion;
import com.iftekhar.ai_paradox.model.SurveyForm;
import com.iftekhar.ai_paradox.repository.CtEvaluationRepository;
import com.iftekhar.ai_paradox.repository.CtQuestionRepository;
import com.iftekhar.ai_paradox.repository.SurveyFormRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SurveyService {
    private final SurveyFormRepository surveyFormRepository;
    private final CtEvaluationRepository evaluationRepository;
    private final CtQuestionRepository questionRepository;

    /**
     * Save a new survey
     */
    public SurveyFormDTO saveSurvey(SurveyFormDTO surveyFormDTO) {
        log.info("Saving survey for student: {}", surveyFormDTO.getStudentId());

        // Check if student ID already exists (for new surveys only)
        if (surveyFormDTO.getId() == null && surveyFormRepository.existsByStudentId(surveyFormDTO.getStudentId())) {
            log.error("Duplicate student ID: {}", surveyFormDTO.getStudentId());
            throw new IllegalArgumentException("A survey with this Student ID already exists");
        }

        // Convert DTO to Entity (basic info only, no answer fields)
        SurveyForm surveyForm = new SurveyForm();
        surveyForm.setName(surveyFormDTO.getName());
        surveyForm.setStudentId(surveyFormDTO.getStudentId());
        surveyForm.setAge(surveyFormDTO.getAge());
        surveyForm.setLevelOfStudy(surveyFormDTO.getLevelOfStudy());
        surveyForm.setAcademicYearSemester(surveyFormDTO.getAcademicYearSemester());
        surveyForm.setLocation(surveyFormDTO.getLocation());
        surveyForm.setDevices(surveyFormDTO.getDevices() != null ? new HashSet<>(surveyFormDTO.getDevices()) : new HashSet<>());
        surveyForm.setDevicesOthersSpecify(surveyFormDTO.getDevicesOthersSpecify());
        surveyForm.setInternetAccess(surveyFormDTO.getInternetAccess());
        surveyForm.setHoursOnline(surveyFormDTO.getHoursOnline());
        surveyForm.setFamiliarWithAI(surveyFormDTO.getFamiliarWithAI());
        surveyForm.setUseAITools(surveyFormDTO.getUseAITools());
        surveyForm.setStudyTools(surveyFormDTO.getStudyTools() != null ? new HashSet<>(surveyFormDTO.getStudyTools()) : new HashSet<>());
        surveyForm.setWritingTools(surveyFormDTO.getWritingTools() != null ? new HashSet<>(surveyFormDTO.getWritingTools()) : new HashSet<>());
        surveyForm.setNoteTools(surveyFormDTO.getNoteTools() != null ? new HashSet<>(surveyFormDTO.getNoteTools()) : new HashSet<>());
        surveyForm.setResearchTools(surveyFormDTO.getResearchTools() != null ? new HashSet<>(surveyFormDTO.getResearchTools()) : new HashSet<>());
        surveyForm.setPresentationTools(surveyFormDTO.getPresentationTools() != null ? new HashSet<>(surveyFormDTO.getPresentationTools()) : new HashSet<>());
        surveyForm.setAiUsageFrequency(surveyFormDTO.getAiUsageFrequency());
        surveyForm.setLearnedAboutAI(surveyFormDTO.getLearnedAboutAI());
        surveyForm.setSubmittedByIp(surveyFormDTO.getSubmittedByIp());
        surveyForm.setIsCompleted(surveyFormDTO.getIsCompleted() != null ? surveyFormDTO.getIsCompleted() : false);

        // Save to database
        SurveyForm savedSurvey = surveyFormRepository.save(surveyForm);

        // Save answers to ct_evaluation table
        saveAnswersToEvaluationTable(savedSurvey.getId(), surveyFormDTO);

        log.info("Survey saved successfully with ID: {}", savedSurvey.getId());

        // Return DTO with ID
        surveyFormDTO.setId(savedSurvey.getId());
        surveyFormDTO.setCreatedAt(savedSurvey.getCreatedAt());
        surveyFormDTO.setUpdatedAt(savedSurvey.getUpdatedAt());

        return surveyFormDTO;
    }

    /**
     * Save answer fields to ct_evaluation table
     */
    private void saveAnswersToEvaluationTable(Long surveyId, SurveyFormDTO dto) {
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
     * Save individual answer to ct_evaluation
     */
    private void saveAnswerRecord(Long surveyId, String questionId, String answerText) {
        if (answerText == null || answerText.isBlank()) {
            log.warn("Skipping empty answer for question: {}", questionId);
            return;
        }

        CtQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found: " + questionId));

        // Create evaluation record with answer but NO scores yet
        CtEvaluation evaluation = new CtEvaluation();
        evaluation.setSurveyId(surveyId);
        evaluation.setQuestion(question);
        evaluation.setStudentAnswer(answerText);
        // score, onTopic, skillsJson, reason, strength, weakness are all NULL
        // They will be filled in later by CtScoringService

        evaluationRepository.save(evaluation);
        log.debug("Saved answer for survey: {}, question: {}", surveyId, questionId);
    }

    /**
     * Get survey by ID with answers loaded from ct_evaluation
     */
    @Transactional
    public SurveyFormDTO getSurveyById(Long id) {
        log.info("Fetching survey with ID: {}", id);

        SurveyForm surveyForm = surveyFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found with ID: " + id));

        // Load answers from ct_evaluation
        List<CtEvaluation> evaluations = evaluationRepository.findBySurveyIdWithQuestion(id);

        return buildDTOWithAnswers(surveyForm, evaluations);
    }

    /**
     * Build DTO from entity and evaluations
     */
    private SurveyFormDTO buildDTOWithAnswers(SurveyForm entity, List<CtEvaluation> evaluations) {
        SurveyFormDTO dto = new SurveyFormDTO();

        // Basic fields
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStudentId(entity.getStudentId());
        dto.setAge(entity.getAge());
        dto.setLevelOfStudy(entity.getLevelOfStudy());
        dto.setAcademicYearSemester(entity.getAcademicYearSemester());
        dto.setLocation(entity.getLocation());
        dto.setDevices(entity.getDevices() != null ? new HashSet<>(entity.getDevices()) : new HashSet<>());
        dto.setDevicesOthersSpecify(entity.getDevicesOthersSpecify());
        dto.setInternetAccess(entity.getInternetAccess());
        dto.setHoursOnline(entity.getHoursOnline());
        dto.setFamiliarWithAI(entity.getFamiliarWithAI());
        dto.setUseAITools(entity.getUseAITools());
        dto.setStudyTools(entity.getStudyTools() != null ? new HashSet<>(entity.getStudyTools()) : new HashSet<>());
        dto.setWritingTools(entity.getWritingTools() != null ? new HashSet<>(entity.getWritingTools()) : new HashSet<>());
        dto.setNoteTools(entity.getNoteTools() != null ? new HashSet<>(entity.getNoteTools()) : new HashSet<>());
        dto.setResearchTools(entity.getResearchTools() != null ? new HashSet<>(entity.getResearchTools()) : new HashSet<>());
        dto.setPresentationTools(entity.getPresentationTools() != null ? new HashSet<>(entity.getPresentationTools()) : new HashSet<>());
        dto.setAiUsageFrequency(entity.getAiUsageFrequency());
        dto.setLearnedAboutAI(entity.getLearnedAboutAI());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setSubmittedByIp(entity.getSubmittedByIp());
        dto.setIsCompleted(entity.getIsCompleted());

        // Map answers from ct_evaluation back to DTO
        for (CtEvaluation eval : evaluations) {
            String questionId = eval.getQuestion().getId();
            String answerText = eval.getStudentAnswer();

            switch (questionId) {
                case "CTA-AI-01.A1" -> dto.setCoreProblemSummary(answerText);
                case "CTA-AI-01.A2" -> dto.setPeripheralEducation(answerText);
                case "CTA-AI-01.B1" -> dto.setImplicitAssumptions(answerText);
                case "CTA-AI-01.B2" -> dto.setArgumentAnalysis(answerText);
                case "CTA-AI-01.B3" -> dto.setEvidenceVsOpinion(answerText);
                case "CTA-AI-01.C1" -> dto.setTableInferences(answerText);
                case "CTA-AI-01.C2" -> dto.setResearchDesign(answerText);
                case "CTA-AI-01.D1" -> dto.setLearningBenefit(answerText);
                case "CTA-AI-01.D2" -> dto.setAiUsageReflection(answerText);
                default -> log.warn("Unknown question ID: {}", questionId);
            }
        }

        return dto;
    }

    /**
     * Get all surveys with pagination
     */
    @Transactional
    public Page<SurveyFormDTO> getAllSurveys(Pageable pageable) {
        log.info("Fetching surveys with pagination - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<SurveyForm> surveyPage = surveyFormRepository.findAll(pageable);

        // For list view, we don't need to load all answers
        // Just convert basic entity to DTO
        return surveyPage.map(this::entityToBasicDTO);
    }

    /**
     * Convert entity to DTO without answers (for list views)
     */
    private SurveyFormDTO entityToBasicDTO(SurveyForm entity) {
        SurveyFormDTO dto = new SurveyFormDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStudentId(entity.getStudentId());
        dto.setAge(entity.getAge());
        dto.setLevelOfStudy(entity.getLevelOfStudy());
        dto.setLocation(entity.getLocation());
        dto.setUseAITools(entity.getUseAITools());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setIsCompleted(entity.getIsCompleted());
        return dto;
    }

    /**
     * Get survey by student ID
     */
    @Transactional
    public SurveyFormDTO getSurveyByStudentId(String studentId) {
        log.info("Fetching survey for student ID: {}", studentId);

        SurveyForm surveyForm = surveyFormRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found for student ID: " + studentId));

        List<CtEvaluation> evaluations = evaluationRepository.findBySurveyIdWithQuestion(surveyForm.getId());

        return buildDTOWithAnswers(surveyForm, evaluations);
    }

    /**
     * Delete survey by ID
     */
    public void deleteSurvey(Long id) {
        log.info("Deleting survey with ID: {}", id);

        if (!surveyFormRepository.existsById(id)) {
            throw new IllegalArgumentException("Survey not found with ID: " + id);
        }

        surveyFormRepository.deleteById(id);
        log.info("Survey deleted successfully with ID: {}", id);
    }

    /**
     * Check if student ID exists
     */
    @Transactional
    public boolean existsByStudentId(String studentId) {
        log.info("Checking if student ID exists: {}", studentId);
        return surveyFormRepository.existsByStudentId(studentId);
    }

    /**
     * Count surveys by level of study
     */
    @Transactional
    public long countByLevelOfStudy(String levelOfStudy) {
        log.info("Counting surveys by level of study: {}", levelOfStudy);
        return surveyFormRepository.countByLevelOfStudy(levelOfStudy);
    }

    /**
     * Count surveys by location
     */
    @Transactional
    public long countByLocation(String location) {
        log.info("Counting surveys by location: {}", location);
        return surveyFormRepository.countByLocation(location);
    }

    /**
     * Count surveys by AI usage
     */
    @Transactional
    public long countByAIUsage(String useAITools) {
        log.info("Counting surveys by AI usage: {}", useAITools);
        return surveyFormRepository.countByUseAITools(useAITools);
    }

    /**
     * Get comprehensive survey statistics
     */
    @Transactional
    public Map<String, Object> getStatistics() {
        log.info("Generating survey statistics");

        Map<String, Object> statistics = new HashMap<>();

        // Total counts
        long totalSurveys = surveyFormRepository.count();
        long completedSurveys = surveyFormRepository.findByIsCompleted(true).size();
        long incompleteSurveys = totalSurveys - completedSurveys;

        statistics.put("totalSurveys", totalSurveys);
        statistics.put("completedSurveys", completedSurveys);
        statistics.put("incompleteSurveys", incompleteSurveys);

        // Level of study breakdown
        Map<String, Long> levelBreakdown = new HashMap<>();
        levelBreakdown.put("Bachelor's", countByLevelOfStudy("Bachelor's"));
        levelBreakdown.put("Master's", countByLevelOfStudy("Master's"));
        statistics.put("levelOfStudyBreakdown", levelBreakdown);

        // Location breakdown
        Map<String, Long> locationBreakdown = new HashMap<>();
        locationBreakdown.put("Rural", countByLocation("Rural"));
        locationBreakdown.put("Urban", countByLocation("Urban"));
        statistics.put("locationBreakdown", locationBreakdown);

        // AI usage breakdown
        Map<String, Long> aiUsageBreakdown = new HashMap<>();
        aiUsageBreakdown.put("Yes", countByAIUsage("Yes"));
        aiUsageBreakdown.put("No", countByAIUsage("No"));
        statistics.put("aiUsageBreakdown", aiUsageBreakdown);

        log.info("Statistics generated successfully");
        return statistics;
    }
}