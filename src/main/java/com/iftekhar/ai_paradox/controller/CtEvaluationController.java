package com.iftekhar.ai_paradox.controller;

import com.iftekhar.ai_paradox.dto.*;
import com.iftekhar.ai_paradox.service.CtScoringService;
import com.iftekhar.ai_paradox.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ct")
public class CtEvaluationController {
    private final QuestionService questionService;
    private final CtScoringService scoringService;

    public CtEvaluationController(QuestionService questionService, CtScoringService scoringService) {
        this.questionService = questionService;
        this.scoringService = scoringService;
    }

    @GetMapping("/questions")
    public List<QuestionDto> listQuestions() {
        return questionService.getActiveQuestions();
    }

    @GetMapping("/questions/{id}")
    public QuestionDto getQuestion(@PathVariable String id) {
        return questionService.getQuestionById(id);
    }

    @PostMapping("/evaluate")
    public CtEvaluationResult evaluate(@RequestBody CtEvaluationRequest request) {
        return scoringService.evaluate(request);
    }

    @PostMapping("/evaluate-survey")
    public ResponseEntity<BatchEvaluationResult> evaluateSurvey(
            @RequestBody BatchEvaluationRequest request) {
        BatchEvaluationResult result = scoringService.evaluateSurvey(request);
        if (!result.isSuccess()) {
            return ResponseEntity.status(500).body(result); // return failure with error JSON
        }
        return ResponseEntity.ok(result);
    }
}
