package com.iftekhar.ai_paradox.service;

import com.iftekhar.ai_paradox.dto.SurveyFormDTO;
import com.iftekhar.ai_paradox.mapper.SurveyFormMapper;
import com.iftekhar.ai_paradox.model.SurveyForm;
import com.iftekhar.ai_paradox.repository.SurveyFormRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SurveyService {
    private final SurveyFormRepository surveyFormRepository;
    private final SurveyFormMapper surveyFormMapper;

    /**
     * Save a new survey or update existing one
     */
    public SurveyFormDTO saveSurvey(SurveyFormDTO surveyFormDTO) {
        log.info("Saving survey for student: {}", surveyFormDTO.getStudentId());

        // Check if student ID already exists (for new surveys only)
        if (surveyFormDTO.getId() == null && surveyFormRepository.existsByStudentId(surveyFormDTO.getStudentId())) {
            log.error("Duplicate student ID: {}", surveyFormDTO.getStudentId());
            throw new IllegalArgumentException("A survey with this Student ID already exists");
        }

        // Convert DTO to Entity
        SurveyForm surveyForm = surveyFormMapper.toEntity(surveyFormDTO);

        // Save to database
        SurveyForm savedSurvey = surveyFormRepository.save(surveyForm);

        log.info("Survey saved successfully with ID: {}", savedSurvey.getId());

        // Convert back to DTO and return
        return surveyFormMapper.toDTO(savedSurvey);
    }

    /**
     * Update an existing survey
     */
    public SurveyFormDTO updateSurvey(Long id, SurveyFormDTO surveyFormDTO) {
        log.info("Updating survey with ID: {}", id);

        SurveyForm existingSurvey = surveyFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found with ID: " + id));

        // Check if student ID is being changed and if it's already in use
        if (!existingSurvey.getStudentId().equals(surveyFormDTO.getStudentId()) &&
                surveyFormRepository.existsByStudentId(surveyFormDTO.getStudentId())) {
            throw new IllegalArgumentException("A survey with this Student ID already exists");
        }

        // Update the existing entity
        surveyFormMapper.updateEntityFromDTO(surveyFormDTO, existingSurvey);

        // Save updated entity
        SurveyForm updatedSurvey = surveyFormRepository.save(existingSurvey);

        log.info("Survey updated successfully with ID: {}", updatedSurvey.getId());

        return surveyFormMapper.toDTO(updatedSurvey);
    }

    /**
     * Get survey by ID
     */
    @Transactional
    public SurveyFormDTO getSurveyById(Long id) {
        log.info("Fetching survey with ID: {}", id);

        SurveyForm surveyForm = surveyFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found with ID: " + id));

        return surveyFormMapper.toDTO(surveyForm);
    }

    /**
     * Get survey by student ID
     */
    @Transactional
    public SurveyFormDTO getSurveyByStudentId(String studentId) {
        log.info("Fetching survey for student ID: {}", studentId);

        SurveyForm surveyForm = surveyFormRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found for student ID: " + studentId));

        return surveyFormMapper.toDTO(surveyForm);
    }

    /**
     * Get all surveys
     */
    @Transactional
    public List<SurveyFormDTO> getAllSurveys() {
        log.info("Fetching all surveys");

        return surveyFormRepository.findAll().stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all surveys with pagination
     */
    @Transactional
    public Page<SurveyFormDTO> getAllSurveys(Pageable pageable) {
        log.info("Fetching surveys with pagination - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        return surveyFormRepository.findAll(pageable)
                .map(surveyFormMapper::toDTO);
    }

    /**
     * Get surveys by level of study
     */
    @Transactional
    public List<SurveyFormDTO> getSurveysByLevelOfStudy(String levelOfStudy) {
        log.info("Fetching surveys by level of study: {}", levelOfStudy);

        return surveyFormRepository.findByLevelOfStudy(levelOfStudy).stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get surveys by location
     */
    @Transactional
    public List<SurveyFormDTO> getSurveysByLocation(String location) {
        log.info("Fetching surveys by location: {}", location);

        return surveyFormRepository.findByLocation(location).stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get surveys by AI usage
     */
    @Transactional
    public List<SurveyFormDTO> getSurveysByAIUsage(String useAITools) {
        log.info("Fetching surveys by AI usage: {}", useAITools);

        return surveyFormRepository.findByUseAITools(useAITools).stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get surveys by AI usage frequency
     */
    @Transactional
    public List<SurveyFormDTO> getSurveysByAIUsageFrequency(String frequency) {
        log.info("Fetching surveys by AI usage frequency: {}", frequency);

        return surveyFormRepository.findByAiUsageFrequency(frequency).stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get completed surveys
     */
    @Transactional
    public List<SurveyFormDTO> getCompletedSurveys() {
        log.info("Fetching completed surveys");

        return surveyFormRepository.findByIsCompleted(true).stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get surveys by date range
     */
    @Transactional
    public List<SurveyFormDTO> getSurveysByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching surveys between {} and {}", startDate, endDate);

        return surveyFormRepository.findByCreatedAtBetween(startDate, endDate).stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get surveys by age range
     */
    @Transactional
    public List<SurveyFormDTO> getSurveysByAgeRange(Integer minAge, Integer maxAge) {
        log.info("Fetching surveys for age range: {} - {}", minAge, maxAge);

        return surveyFormRepository.findByAgeRange(minAge, maxAge).stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get surveys by internet access
     */
    @Transactional
    public List<SurveyFormDTO> getSurveysByInternetAccess(String internetAccess) {
        log.info("Fetching surveys by internet access: {}", internetAccess);

        return surveyFormRepository.findByInternetAccess(internetAccess).stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get latest surveys (top 10)
     */
    @Transactional
    public List<SurveyFormDTO> getLatestSurveys() {
        log.info("Fetching latest 10 surveys");

        return surveyFormRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
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

        // AI usage frequency breakdown
        Map<String, Long> frequencyBreakdown = new HashMap<>();
        frequencyBreakdown.put("Daily", (long) getSurveysByAIUsageFrequency("Daily").size());
        frequencyBreakdown.put("Weekly", (long) getSurveysByAIUsageFrequency("Weekly").size());
        frequencyBreakdown.put("Occasionally", (long) getSurveysByAIUsageFrequency("Occasionally").size());
        frequencyBreakdown.put("Rarely", (long) getSurveysByAIUsageFrequency("Rarely").size());
        frequencyBreakdown.put("Never", (long) getSurveysByAIUsageFrequency("Never").size());
        statistics.put("aiUsageFrequencyBreakdown", frequencyBreakdown);

        // Internet access breakdown
        Map<String, Long> internetBreakdown = new HashMap<>();
        internetBreakdown.put("Yes", (long) getSurveysByInternetAccess("Yes").size());
        internetBreakdown.put("No", (long) getSurveysByInternetAccess("No").size());
        statistics.put("internetAccessBreakdown", internetBreakdown);

        // Age statistics
        List<SurveyForm> allSurveys = surveyFormRepository.findAll();
        if (!allSurveys.isEmpty()) {
            double avgAge = allSurveys.stream()
                    .mapToInt(SurveyForm::getAge)
                    .average()
                    .orElse(0.0);

            int minAge = allSurveys.stream()
                    .mapToInt(SurveyForm::getAge)
                    .min()
                    .orElse(0);

            int maxAge = allSurveys.stream()
                    .mapToInt(SurveyForm::getAge)
                    .max()
                    .orElse(0);

            Map<String, Object> ageStats = new HashMap<>();
            ageStats.put("average", Math.round(avgAge * 100.0) / 100.0);
            ageStats.put("minimum", minAge);
            ageStats.put("maximum", maxAge);
            statistics.put("ageStatistics", ageStats);
        }

        // Time-based statistics
        if (!allSurveys.isEmpty()) {
            LocalDateTime firstSubmission = allSurveys.stream()
                    .map(SurveyForm::getCreatedAt)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            LocalDateTime lastSubmission = allSurveys.stream()
                    .map(SurveyForm::getCreatedAt)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            Map<String, Object> timeStats = new HashMap<>();
            timeStats.put("firstSubmission", firstSubmission);
            timeStats.put("lastSubmission", lastSubmission);
            statistics.put("timeStatistics", timeStats);
        }

        log.info("Statistics generated successfully");
        return statistics;
    }

    /**
     * Get surveys by level of study and location
     */
    @Transactional
    public List<SurveyFormDTO> getSurveysByLevelAndLocation(String levelOfStudy, String location) {
        log.info("Fetching surveys by level: {} and location: {}", levelOfStudy, location);

        return surveyFormRepository.findByLevelOfStudyAndLocation(levelOfStudy, location).stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get surveys by AI tools usage and frequency
     */
    @Transactional
    public List<SurveyFormDTO> getSurveysByAIToolsAndFrequency(String useAITools, String frequency) {
        log.info("Fetching surveys by AI usage: {} and frequency: {}", useAITools, frequency);

        return surveyFormRepository.findByAIToolsUsageAndFrequency(useAITools, frequency).stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Delete old surveys (cleanup)
     */
    public void deleteOldSurveys(LocalDateTime beforeDate) {
        log.info("Deleting surveys older than: {}", beforeDate);
        surveyFormRepository.deleteByCreatedAtBefore(beforeDate);
        log.info("Old surveys deleted successfully");
    }

    /**
     * Bulk save surveys
     */
    public List<SurveyFormDTO> saveSurveys(List<SurveyFormDTO> surveyFormDTOs) {
        log.info("Bulk saving {} surveys", surveyFormDTOs.size());

        List<SurveyForm> surveyForms = surveyFormDTOs.stream()
                .map(surveyFormMapper::toEntity)
                .collect(Collectors.toList());

        List<SurveyForm> savedSurveys = surveyFormRepository.saveAll(surveyForms);

        log.info("Bulk save completed: {} surveys saved", savedSurveys.size());

        return savedSurveys.stream()
                .map(surveyFormMapper::toDTO)
                .collect(Collectors.toList());
    }

}
