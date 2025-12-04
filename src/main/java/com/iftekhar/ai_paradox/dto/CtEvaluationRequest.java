package com.iftekhar.ai_paradox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CtEvaluationRequest {
    private String questionId;
    private String studentAnswer;
    private Long surveyId;
}
