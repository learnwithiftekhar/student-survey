package com.iftekhar.ai_paradox.repository;

import com.iftekhar.ai_paradox.model.CtQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CtQuestionRepository extends JpaRepository<CtQuestion, String> {
    List<CtQuestion> findByActiveTrue();
    List<CtQuestion> findBySectionOrderByNumberAsc(String section);
    List<CtQuestion> findByIdStartingWith(String prefix);
}
