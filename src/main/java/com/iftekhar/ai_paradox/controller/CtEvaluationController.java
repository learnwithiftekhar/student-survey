package com.iftekhar.ai_paradox.controller;

import com.iftekhar.ai_paradox.dto.*;
import com.iftekhar.ai_paradox.service.CtScoringService;
import com.iftekhar.ai_paradox.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ct")
@RequiredArgsConstructor
@Slf4j
public class CtEvaluationController {
    private final QuestionService questionService;
    private final CtScoringService scoringService;

    /**
     * Get all active questions
     */
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDto>> listQuestions() {
        log.info("Fetching all active questions");
        List<QuestionDto> questions = questionService.getActiveQuestions();
        return ResponseEntity.ok(questions);
    }

    /**
     * Get a specific question by ID
     */
    @GetMapping("/questions/{id}")
    public ResponseEntity<QuestionDto> getQuestion(@PathVariable String id) {
        log.info("Fetching question: {}", id);
        QuestionDto question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    /**
     * Evaluate a single answer
     * NOTE: This endpoint expects the answer to already be saved in ct_evaluation
     */
    @PostMapping("/evaluate")
    public ResponseEntity<CtEvaluationResult> evaluateSingle(@RequestBody CtEvaluationRequest request) {
        log.info("Evaluating single answer - Survey: {}, Question: {}",
                request.getSurveyId(), request.getQuestionId());

        CtEvaluationResult result = scoringService.evaluate(request);
        return ResponseEntity.ok(result);
    }

    /**
     * Evaluate all answers for a survey at once (PREFERRED METHOD)
     * This endpoint expects answers to already be saved in ct_evaluation
     */
    @PostMapping("/evaluate-survey/{surveyId}")
    public ResponseEntity<BatchEvaluationResult> evaluateSurvey(@PathVariable Long surveyId) {
        log.info("Starting batch evaluation for survey: {}", surveyId);

        BatchEvaluationRequest request = new BatchEvaluationRequest();
        request.setSurveyId(surveyId);
        // No need to pass items - service will load from ct_evaluation table

        BatchEvaluationResult result = scoringService.evaluateSurvey(request);

        if (!result.isSuccess()) {
            log.error("Evaluation failed for survey: {}, error: {}",
                    surveyId, result.getErrorMessage());
            return ResponseEntity.status(500).body(result);
        }

        log.info("Batch evaluation completed successfully for survey: {}", surveyId);
        return ResponseEntity.ok(result);
    }
}