package com.iftekhar.ai_paradox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchEvaluationRequest {
    private Long surveyId;
    // Items are no longer needed - answers are loaded from ct_evaluation table
    // Keeping this field for backward compatibility, but it's ignored
    private List<QuestionAnswerDto> items;
}