package com.iftekhar.ai_paradox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDisplayDto {
    private String questionId;
    private String questionNumber;
    private String questionTitle;
    private Integer maxScore;
    private Integer score;
    private Boolean onTopic;
    private String reason;
    private String strength;
    private String weakness;
    private Map<String, Integer> skills;
    private String studentAnswer;
}
