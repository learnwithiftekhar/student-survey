package com.iftekhar.ai_paradox.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CtEvaluationResult {
    private String questionId;
    private int score;

    @JsonProperty("on_topic")// 0–maxScore
    private boolean onTopic;
    private Map<String, Integer> skills;  // e.g. {"interpretation":4,"analysis":3}
    private String reason;
    private String strength;
    private String weakness;
}
