package com.iftekhar.ai_paradox.repository;

import com.iftekhar.ai_paradox.model.SurveyForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SurveyFormRepository extends JpaRepository<SurveyForm, Long> {
    /**
     * Find survey form by student ID
     */
    Optional<SurveyForm> findByStudentId(String studentId);

    /**
     * Find all survey forms by level of study
     */
    List<SurveyForm> findByLevelOfStudy(String levelOfStudy);

    /**
     * Find all survey forms by location
     */
    List<SurveyForm> findByLocation(String location);

    /**
     * Find all survey forms by AI usage status
     */
    List<SurveyForm> findByUseAITools(String useAITools);

    /**
     * Find all survey forms by AI usage frequency
     */
    List<SurveyForm> findByAiUsageFrequency(String aiUsageFrequency);

    /**
     * Find all completed survey forms
     */
    List<SurveyForm> findByIsCompleted(Boolean isCompleted);

    /**
     * Find all survey forms created between two dates
     */
    List<SurveyForm> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all survey forms by age range
     */
    @Query("SELECT s FROM SurveyForm s WHERE s.age BETWEEN :minAge AND :maxAge")
    List<SurveyForm> findByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

    /**
     * Count survey forms by level of study
     */
    long countByLevelOfStudy(String levelOfStudy);

    /**
     * Count survey forms by location
     */
    long countByLocation(String location);

    /**
     * Count survey forms by AI usage
     */
    long countByUseAITools(String useAITools);

    /**
     * Find all survey forms by internet access status
     */
    List<SurveyForm> findByInternetAccess(String internetAccess);

    /**
     * Check if student ID already exists
     */
    boolean existsByStudentId(String studentId);

    /**
     * Find survey forms by level of study and location
     */
    @Query("SELECT s FROM SurveyForm s WHERE s.levelOfStudy = :levelOfStudy AND s.location = :location")
    List<SurveyForm> findByLevelOfStudyAndLocation(
            @Param("levelOfStudy") String levelOfStudy,
            @Param("location") String location
    );

    /**
     * Find survey forms by AI tools usage and frequency
     */
    @Query("SELECT s FROM SurveyForm s WHERE s.useAITools = :useAITools AND s.aiUsageFrequency = :frequency")
    List<SurveyForm> findByAIToolsUsageAndFrequency(
            @Param("useAITools") String useAITools,
            @Param("frequency") String frequency
    );

    /**
     * Get statistics: count by age group
     */
    @Query("SELECT " +
            "CASE " +
            "  WHEN s.age BETWEEN 18 AND 22 THEN '18-22' " +
            "  WHEN s.age BETWEEN 23 AND 27 THEN '23-27' " +
            "  WHEN s.age BETWEEN 28 AND 35 THEN '28-35' " +
            "END as ageGroup, " +
            "COUNT(s) as count " +
            "FROM SurveyForm s " +
            "GROUP BY ageGroup")
    List<Object[]> getAgeGroupStatistics();

    /**
     * Get the latest survey forms
     */
    List<SurveyForm> findTop10ByOrderByCreatedAtDesc();

    /**
     * Find survey forms with specific academic year
     */
    @Query("SELECT s FROM SurveyForm s WHERE s.academicYearSemester LIKE :year%")
    List<SurveyForm> findByAcademicYear(@Param("year") String year);

    /**
     * Delete survey forms older than specified date
     */
    void deleteByCreatedAtBefore(LocalDateTime date);
}
