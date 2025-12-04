package com.iftekhar.ai_paradox.controller;

import com.iftekhar.ai_paradox.dto.CtEvaluationRequest;
import com.iftekhar.ai_paradox.dto.CtEvaluationResult;
import com.iftekhar.ai_paradox.dto.QuestionDto;
import com.iftekhar.ai_paradox.service.CtScoringService;
import com.iftekhar.ai_paradox.service.QuestionService;
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
}
