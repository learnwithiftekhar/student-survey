package com.iftekhar.ai_paradox.dto;

import java.util.List;

public class BatchEvaluationResult {
    private Long surveyId;
    private List<CtEvaluationResult> evaluations;
    private boolean success = true;
    private String errorCode;     // e.g. MODEL_CALL_FAILED, DB_ERROR, PARSE_ERROR
    private String errorMessage;  // human readable message

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public List<CtEvaluationResult> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<CtEvaluationResult> evaluations) {
        this.evaluations = evaluations;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
