package com.iftekhar.ai_paradox.repository;

import com.iftekhar.ai_paradox.model.CtEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CtEvaluationRepository extends JpaRepository<CtEvaluation, Long> {
    List<CtEvaluation> findByQuestionId(String questionId);
}
