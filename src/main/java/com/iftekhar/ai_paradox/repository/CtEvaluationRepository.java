package com.iftekhar.ai_paradox.repository;

import com.iftekhar.ai_paradox.model.CtEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtEvaluationRepository extends JpaRepository<CtEvaluation, Long> {

    @Query("SELECT e FROM CtEvaluation e WHERE e.surveyId = :surveyId AND e.question.id = :questionId")
    Optional<CtEvaluation> findBySurveyIdAndQuestionId(
            @Param("surveyId") Long surveyId,
            @Param("questionId") String questionId
    );

    @Query("SELECT e FROM CtEvaluation e JOIN FETCH e.question WHERE e.surveyId = :surveyId")
    List<CtEvaluation> findBySurveyIdWithQuestion(@Param("surveyId") Long surveyId);

    /**
     * Check if a survey has been evaluated (at least one score is not null)
     */
    @Query("SELECT COUNT(e) > 0 FROM CtEvaluation e WHERE e.surveyId = :surveyId AND e.score IS NOT NULL")
    boolean isSurveyEvaluated(@Param("surveyId") Long surveyId);  // ✅ This method

    /**
     * Delete all evaluations for a survey
     */
    @Modifying
    @Query("DELETE FROM CtEvaluation e WHERE e.surveyId = :surveyId")
    void deleteBySurveyId(@Param("surveyId") Long surveyId);

    /**
     * Count distinct surveys that have been evaluated (at least one score is not null)
     */
    @Query("SELECT COUNT(DISTINCT e.surveyId) FROM CtEvaluation e WHERE e.score IS NOT NULL")
    long countEvaluatedSurveys();
}