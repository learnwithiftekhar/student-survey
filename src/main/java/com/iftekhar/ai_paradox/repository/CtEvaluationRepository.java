package com.iftekhar.ai_paradox.repository;

import com.iftekhar.ai_paradox.model.CtEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CtEvaluationRepository extends JpaRepository<CtEvaluation, Long> {
    List<CtEvaluation> findByQuestionId(String questionId);

    List<CtEvaluation> findBySurveyId(Long surveyId);

    // OPTION 1: Using custom query (RECOMMENDED - more explicit)
    @Query("SELECT e FROM CtEvaluation e WHERE e.surveyId = :surveyId AND e.question.id = :questionId")
    Optional<CtEvaluation> findBySurveyIdAndQuestionId(@Param("surveyId") Long surveyId,
                                                       @Param("questionId") String questionId);

    // OPTION 2: Spring Data JPA naming convention (also works)
    // Optional<CtEvaluation> findBySurveyIdAndQuestion_Id(Long surveyId, String questionId);

    @Query("SELECT e FROM CtEvaluation e JOIN FETCH e.question WHERE e.surveyId = :surveyId")
    List<CtEvaluation> findBySurveyIdWithQuestion(@Param("surveyId") Long surveyId);

    // Check if survey has been evaluated (has scores)
    @Query("SELECT COUNT(e) > 0 FROM CtEvaluation e WHERE e.surveyId = :surveyId AND e.score IS NOT NULL")
    boolean isSurveyEvaluated(@Param("surveyId") Long surveyId);

    // Count evaluated vs unevaluated
    @Query("SELECT COUNT(e) FROM CtEvaluation e WHERE e.surveyId = :surveyId AND e.score IS NULL")
    long countUnevaluatedAnswers(@Param("surveyId") Long surveyId);
}