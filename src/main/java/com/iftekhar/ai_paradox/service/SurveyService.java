package com.iftekhar.ai_paradox.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iftekhar.ai_paradox.dto.EvaluationDisplayDto;
import com.iftekhar.ai_paradox.dto.SurveyFormDTO;
import com.iftekhar.ai_paradox.model.CtEvaluation;
import com.iftekhar.ai_paradox.model.CtQuestion;
import com.iftekhar.ai_paradox.model.GroupType;
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
import java.util.*;
import java.util.stream.Collectors;

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
    public SurveyFormDTO saveSurvey(SurveyFormDTO surveyFormDTO) {
        log.info("Saving survey for student: {}", surveyFormDTO.getStudentId());

        // Check if student ID already exists (for new surveys only)
        if (surveyFormDTO.getId() == null && surveyFormRepository.existsByStudentId(surveyFormDTO.getStudentId())) {
            log.error("Duplicate student ID: {}", surveyFormDTO.getStudentId());
            throw new IllegalArgumentException("A survey with this Student ID already exists");
        }

        // ✅ NEW - Validate group type
        if (surveyFormDTO.getGroupType() == null) {
            throw new IllegalArgumentException("Group type is required");
        }


        // Step 1: Save basic survey info to survey_forms table
        SurveyForm surveyForm = new SurveyForm();

        // ✅ NEW - Set group type
        surveyForm.setGroupType(surveyFormDTO.getGroupType());
        surveyForm.setName(surveyFormDTO.getName());
        surveyForm.setStudentId(surveyFormDTO.getStudentId());
        surveyForm.setAge(surveyFormDTO.getAge());
        surveyForm.setEthicalConsiderations(surveyFormDTO.getEthicalConsiderations());        surveyForm.setAcademicYearSemester(surveyFormDTO.getAcademicYearSemester());
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

        SurveyForm savedSurvey = surveyFormRepository.save(surveyForm);
        log.info("Saved survey with ID: {}", savedSurvey.getId());

        // Step 2: Save answers to ct_evaluation table (with NULL scores initially)
        saveAnswersToEvaluationTable(savedSurvey.getId(), surveyFormDTO);

        surveyFormDTO.setId(savedSurvey.getId());
        surveyFormDTO.setCreatedAt(savedSurvey.getCreatedAt());
        surveyFormDTO.setEvaluated(false);  // ✅ New survey is not evaluated yet

        return surveyFormDTO;
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


                    SurveyFormDTO dto = SurveyFormDTO.builder()
                            .id(survey.getId())
                            .name(survey.getName())
                            .studentId(survey.getStudentId())
                            .age(survey.getAge())
                            .ethicalConsiderations(survey.getEthicalConsiderations())
                            .academicYearSemester(survey.getAcademicYearSemester())  // ✅ Add this
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
                            .evaluated(isEvaluated)
                            .createdAt(survey.getCreatedAt())
                            .updatedAt(survey.getUpdatedAt())
                            .submittedByIp(survey.getSubmittedByIp())
                            .isCompleted(survey.getIsCompleted())
                            .build();

                    // ✅ Add overall score if evaluated
                    if (isEvaluated) {
                        Map<String, Object> overallScore = getOverallEvaluationScore(survey.getId());
                        dto.setOverallScore((Integer) overallScore.get("totalScore"));
                        dto.setMaxPossibleScore((Integer) overallScore.get("maxPossibleScore"));
                        dto.setScorePercentage((Double) overallScore.get("percentage"));

                        @SuppressWarnings("unchecked")
                        Map<String, String> implication = (Map<String, String>) overallScore.get("implication");
                        dto.setCtLevel(implication.get("label"));
                        dto.setCtLevelColor(implication.get("color"));
                    }

                    return dto;
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
                .groupType(survey.getGroupType())  // ✅ NEW - Add group type
                .name(survey.getName())
                .studentId(survey.getStudentId())
                .age(survey.getAge())
                .ethicalConsiderations(survey.getEthicalConsiderations())
                .academicYearSemester(survey.getAcademicYearSemester())
                .location(survey.getLocation())
                .devices(survey.getDevices())
                .devicesOthersSpecify(survey.getDevicesOthersSpecify())
                .internetAccess(survey.getInternetAccess())
                .hoursOnline(survey.getHoursOnline())
                .familiarWithAI(survey.getFamiliarWithAI())
                .studyTools(survey.getStudyTools())
                .writingTools(survey.getWritingTools())
                .noteTools(survey.getNoteTools())
                .researchTools(survey.getResearchTools())
                .presentationTools(survey.getPresentationTools())
                .aiUsageFrequency(survey.getAiUsageFrequency())
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

    /**
     * Get count of evaluated surveys
     */
    @Transactional(readOnly = true)
    public long getEvaluatedSurveysCount() {
        return ctEvaluationRepository.countEvaluatedSurveys();
    }

    /**
     * Get evaluation details for a survey
     */
    @Transactional(readOnly = true)
    public List<EvaluationDisplayDto> getEvaluationsBySurveyId(Long surveyId) {
        List<CtEvaluation> evaluations = ctEvaluationRepository.findBySurveyIdWithQuestion(surveyId);

        return evaluations.stream()
                .map(this::mapToEvaluationDisplayDto)
                .collect(Collectors.toList());
    }

    /**
     * Calculate overall evaluation score and implication
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getOverallEvaluationScore(Long surveyId) {
        List<CtEvaluation> evaluations = ctEvaluationRepository.findBySurveyIdWithQuestion(surveyId);

        int totalScore = 0;
        int maxPossibleScore = 0;
        int evaluatedCount = 0;

        for (CtEvaluation eval : evaluations) {
            if (eval.getScore() != null) {
                totalScore += eval.getScore();
                evaluatedCount++;
            }
            maxPossibleScore += eval.getQuestion().getMaxScore();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalScore", totalScore);
        result.put("maxPossibleScore", maxPossibleScore);
        result.put("evaluatedCount", evaluatedCount);
        result.put("totalQuestions", evaluations.size());
        result.put("percentage", maxPossibleScore > 0 ? (totalScore * 100.0 / maxPossibleScore) : 0);
        result.put("implication", getScoreImplication(totalScore));

        return result;
    }

    /**
     * Map CtEvaluation to EvaluationDisplayDto
     */
    private EvaluationDisplayDto mapToEvaluationDisplayDto(CtEvaluation evaluation) {
        CtQuestion question = evaluation.getQuestion();

        Map<String, Integer> skills = null;
        if (evaluation.getSkillsJson() != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                skills = mapper.readValue(evaluation.getSkillsJson(),
                        new TypeReference<Map<String, Integer>>() {});
            } catch (Exception e) {
                log.warn("Failed to parse skills JSON for evaluation ID: {}", evaluation.getId());
            }
        }

        return EvaluationDisplayDto.builder()
                .questionId(question.getId())
                .questionNumber(question.getNumber())
                .questionTitle(question.getTitle())
                .maxScore(question.getMaxScore())
                .score(evaluation.getScore())
                .onTopic(evaluation.getOnTopic())
                .reason(evaluation.getReason())
                .strength(evaluation.getStrength())
                .weakness(evaluation.getWeakness())
                .skills(skills)
                .studentAnswer(evaluation.getStudentAnswer())
                .build();
    }
    /**
     * Get score implication based on total score (out of 60)
     */
    private Map<String, String> getScoreImplication(int totalScore) {
        Map<String, String> implication = new HashMap<>();

        if (totalScore >= 48) {
            implication.put("label", "Advanced / High CT");
            implication.put("color", "green");
            implication.put("description", "Strong ability to interpret, analyse, evaluate, infer, design studies, and reflect on own learning and AI use.");
        } else if (totalScore >= 36) {
            implication.put("label", "Competent / Developing CT");
            implication.put("color", "blue");
            implication.put("description", "Demonstrates consistent, though uneven, critical thinking. Can analyse and infer but may miss complexity.");
        } else if (totalScore >= 24) {
            implication.put("label", "Emerging / Basic CT");
            implication.put("color", "yellow");
            implication.put("description", "Shows basic understanding of case and some ability to identify assumptions or draw inferences.");
        } else {
            implication.put("label", "At Risk / Limited CT");
            implication.put("color", "red");
            implication.put("description", "Struggles to interpret the problem, recognise assumptions, or construct coherent arguments.");
        }

        return implication;
    }

    /**
     * Get list of survey IDs that haven't been evaluated yet
     */
    @Transactional(readOnly = true)
    public List<Long> getUnevaluatedSurveyIds() {
        log.info("Fetching unevaluated survey IDs");

        List<Long> allSurveyIds = surveyFormRepository.findAll()
                .stream()
                .map(SurveyForm::getId)
                .collect(Collectors.toList());

        List<Long> unevaluatedIds = allSurveyIds.stream()
                .filter(surveyId -> !ctEvaluationRepository.isSurveyEvaluated(surveyId))
                .collect(Collectors.toList());

        log.info("Found {} unevaluated surveys out of {} total surveys",
                unevaluatedIds.size(), allSurveyIds.size());
        return unevaluatedIds;
    }

    // ========== ✅ NEW METHODS - GROUP-RELATED OPERATIONS ==========

    /**
     * ✅ NEW - Get all surveys by group type with pagination
     */
    @Transactional(readOnly = true)
    public Page<SurveyFormDTO> getAllSurveysByGroup(GroupType groupType, Pageable pageable) {
        log.info("Fetching surveys for {}", groupType);

        Page<SurveyForm> surveyPage = surveyFormRepository.findByGroupType(groupType, pageable);

        return surveyPage.map(survey -> {
            List<CtEvaluation> evaluations = ctEvaluationRepository.findBySurveyIdWithQuestion(survey.getId());
            boolean isEvaluated = evaluations.stream()
                    .allMatch(eval -> eval.getScore() != null);

            SurveyFormDTO dto = buildDTOWithAnswers(survey, evaluations, isEvaluated);

            if (isEvaluated) {
                Integer totalScore = evaluations.stream()
                        .mapToInt(e -> e.getScore() != null ? e.getScore() : 0)
                        .sum();
                dto.setTotalScore(totalScore);

                Map<String, Object> overallScore = calculateOverallCTScore(totalScore);
                if (overallScore != null && overallScore.containsKey("implication")) {
                    Map<String, String> implication = (Map<String, String>) overallScore.get("implication");
                    dto.setCtLevel(implication.get("label"));
                    dto.setCtLevelColor(implication.get("color"));
                }
            }

            return dto;
        });
    }

    /**
     * ✅ NEW - Count surveys by group
     */
    @Transactional(readOnly = true)
    public long countByGroup(GroupType groupType) {
        return surveyFormRepository.countByGroupType(groupType);
    }

    /**
     * ✅ NEW - Get comprehensive statistics for a specific group
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getGroupStatistics(GroupType groupType) {
        log.info("Calculating statistics for {}", groupType);

        Map<String, Object> stats = new HashMap<>();

        // Basic counts
        long totalCount = surveyFormRepository.countByGroupType(groupType);
        long completedCount = surveyFormRepository.countByGroupTypeAndIsCompleted(groupType, true);

        stats.put("totalSurveys", totalCount);
        stats.put("completedSurveys", completedCount);
        stats.put("groupName", groupType.getDisplayName());

        // Get all surveys for this group
        List<SurveyForm> surveys = surveyFormRepository.findByGroupType(groupType);

        // Calculate evaluation statistics
        int evaluatedCount = 0;
        List<Integer> totalScores = new ArrayList<>();
        Map<String, Integer> ctLevelCounts = new HashMap<>();

        for (SurveyForm survey : surveys) {
            List<CtEvaluation> evaluations = ctEvaluationRepository.findBySurveyIdWithQuestion(survey.getId());

            // Check if ALL questions for this survey have been evaluated
            boolean isEvaluated = !evaluations.isEmpty() &&
                    evaluations.stream().allMatch(eval -> eval.getScore() != null);

            if (isEvaluated) {
                evaluatedCount++;

                // Calculate total score
                int totalScore = evaluations.stream()
                        .mapToInt(e -> e.getScore() != null ? e.getScore() : 0)
                        .sum();
                totalScores.add(totalScore);

                // Categorize CT level
                Map<String, Object> overallScore = calculateOverallCTScore(totalScore);
                if (overallScore != null && overallScore.containsKey("implication")) {
                    Map<String, String> implication = (Map<String, String>) overallScore.get("implication");
                    String ctLevel = implication.get("label");
                    ctLevelCounts.put(ctLevel, ctLevelCounts.getOrDefault(ctLevel, 0) + 1);
                }
            }
        }

        stats.put("evaluatedCount", evaluatedCount);
        stats.put("ctLevelDistribution", ctLevelCounts);

        // Calculate score statistics
        if (!totalScores.isEmpty()) {
            double averageScore = totalScores.stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0);
            int minScore = totalScores.stream()
                    .mapToInt(Integer::intValue)
                    .min()
                    .orElse(0);
            int maxScore = totalScores.stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0);

            stats.put("averageScore", String.format("%.2f", averageScore));
            stats.put("minScore", minScore);
            stats.put("maxScore", maxScore);
            stats.put("scoreRange", maxScore - minScore);
        } else {
            stats.put("averageScore", "N/A");
            stats.put("minScore", "N/A");
            stats.put("maxScore", "N/A");
            stats.put("scoreRange", "N/A");
        }

        // Demographics
        Map<String, Long> locationDistribution = surveys.stream()
                .collect(Collectors.groupingBy(SurveyForm::getLocation, Collectors.counting()));
        stats.put("locationDistribution", locationDistribution);

        return stats;
    }

    /**
     * ✅ NEW - Get comparison data between groups
     */
    @Transactional(readOnly = true)
    public Map<String, Object> compareGroups() {
        log.info("Generating group comparison data");

        Map<String, Object> comparison = new HashMap<>();

        Map<String, Object> groupAStats = getGroupStatistics(GroupType.GROUP_A);
        Map<String, Object> groupBStats = getGroupStatistics(GroupType.GROUP_B);

        comparison.put("groupA", groupAStats);
        comparison.put("groupB", groupBStats);

        // Calculate differences if both groups have evaluated surveys
        if (groupAStats.containsKey("averageScore") && groupBStats.containsKey("averageScore")) {
            try {
                double avgA = Double.parseDouble(groupAStats.get("averageScore").toString());
                double avgB = Double.parseDouble(groupBStats.get("averageScore").toString());
                double difference = avgA - avgB;

                comparison.put("averageScoreDifference", String.format("%.2f", difference));
                comparison.put("groupAPerformsBetter", difference > 0);
            } catch (NumberFormatException e) {
                log.warn("Could not calculate score difference");
            }
        }

        return comparison;
    }

    /**
     * ✅ NEW - Get average score for a group
     */
    @Transactional(readOnly = true)
    public Double getAverageScoreByGroup(GroupType groupType) {
        // Get all survey IDs for this group
        List<Long> surveyIds = surveyFormRepository.findSurveyIdsByGroupType(groupType);

        if (surveyIds.isEmpty()) {
            return 0.0;
        }

        // Get all evaluations for these surveys
        List<CtEvaluation> evaluations = ctEvaluationRepository.findAll().stream()
                .filter(e -> surveyIds.contains(e.getSurveyId()) && e.getScore() != null)
                .collect(Collectors.toList());

        if (evaluations.isEmpty()) {
            return 0.0;
        }

        return evaluations.stream()
                .mapToInt(CtEvaluation::getScore)
                .average()
                .orElse(0.0);
    }

    /**
     * Calculate overall CT score
     * NO CHANGES
     */
    private Map<String, Object> calculateOverallCTScore(int totalScore) {
        Map<String, Object> result = new HashMap<>();
        result.put("totalScore", totalScore);
        result.put("maxScore", 60);
        result.put("percentage", (totalScore * 100.0) / 60);
        result.put("implication", getScoreImplication(totalScore));
        return result;
    }
}