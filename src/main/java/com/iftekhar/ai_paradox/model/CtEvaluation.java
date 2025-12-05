package com.iftekhar.ai_paradox.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "ct_evaluation")
public class CtEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LINK TO SURVEY FORM
    @Column(name = "survey_id", nullable = false)
    private Long surveyId;     // FK to survey_forms.id

    // which question this evaluation is for
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private CtQuestion question;

    // what they wrote
    @Lob
    @Column(name = "student_answer", nullable = false, columnDefinition = "TEXT")
    private String studentAnswer;

    // model evaluation
    @Column(name = "score")
    private Integer score;

    @Column(name = "on_topic")
    private Boolean onTopic;

    @Lob
    @Column(name = "skills_json")
    private String skillsJson;   // e.g. {"interpretation":4,"analysis":3}

    @Lob
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Lob
    @Column(name = "strength", columnDefinition = "TEXT")
    private String strength;

    @Lob
    @Column(name = "weakness", columnDefinition = "TEXT")
    private String weakness;

    @Column(name = "eval_model", length = 100)
    private String evalModel;    // e.g. gpt-4o-mini

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }

    // --- getters & setters ---

    public Long getId() {
        return id;
    }

    public CtQuestion getQuestion() {
        return question;
    }

    public void setQuestion(CtQuestion question) {
        this.question = question;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getOnTopic() {
        return onTopic;
    }

    public void setOnTopic(Boolean onTopic) {
        this.onTopic = onTopic;
    }

    public String getSkillsJson() {
        return skillsJson;
    }

    public void setSkillsJson(String skillsJson) {
        this.skillsJson = skillsJson;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    public String getEvalModel() {
        return evalModel;
    }

    public void setEvalModel(String evalModel) {
        this.evalModel = evalModel;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
