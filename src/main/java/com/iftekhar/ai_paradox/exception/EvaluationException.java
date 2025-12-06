package com.iftekhar.ai_paradox.exception;

public class EvaluationException extends RuntimeException {
    private final Long surveyId;
    private final String questionId;

    public EvaluationException(String message) {
        super(message);
        this.surveyId = null;
        this.questionId = null;
    }

    public EvaluationException(String message, Long surveyId) {
        super(message);
        this.surveyId = surveyId;
        this.questionId = null;
    }

    public EvaluationException(String message, Long surveyId, String questionId) {
        super(message);
        this.surveyId = surveyId;
        this.questionId = questionId;
    }

    public EvaluationException(String message, Throwable cause) {
        super(message, cause);
        this.surveyId = null;
        this.questionId = null;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public String getQuestionId() {
        return questionId;
    }
}
