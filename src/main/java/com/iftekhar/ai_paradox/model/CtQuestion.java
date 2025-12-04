package com.iftekhar.ai_paradox.model;


import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ct_question")
public class CtQuestion {
    @Id
    @Column(name = "id", length = 50)
    private String id;               // e.g. CTA-AI-01.A1

    @Column(name = "section", nullable = false, length = 1)
    private String section;          // A, B, C, D

    @Column(name = "number", nullable = false, length = 10)
    private String number;           // A1, B2, ...

    @Column(name = "title", length = 255)
    private String title;

    @Lob
    @Column(name = "text", nullable = false)
    private String text;             // question text

    @Lob
    @Column(name = "rubric", nullable = false)
    private String rubric;           // marking guideline

    @Column(name = "max_score", nullable = false)
    private Integer maxScore;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "created_at", nullable = false,
            updatable = false, columnDefinition = "timestamp")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false,
            columnDefinition = "timestamp")
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ct_question_skill",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<CtSkill> skills = new HashSet<>();

    // --- lifecycle hooks (optional but nice) ---

    @PrePersist
    public void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // --- getters & setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRubric() {
        return rubric;
    }

    public void setRubric(String rubric) {
        this.rubric = rubric;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<CtSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<CtSkill> skills) {
        this.skills = skills;
    }
}
