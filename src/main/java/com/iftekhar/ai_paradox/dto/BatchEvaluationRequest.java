package com.iftekhar.ai_paradox.dto;

import java.util.List;

public class BatchEvaluationRequest {
    private Long surveyId;
    private List<QuestionAnswerDto> items;

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public List<QuestionAnswerDto> getItems() {
        return items;
    }

    public void setItems(List<QuestionAnswerDto> items) {
        this.items = items;
    }
}
