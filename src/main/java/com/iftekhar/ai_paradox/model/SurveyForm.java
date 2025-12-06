package com.iftekhar.ai_paradox.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "survey_forms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Basic Information =====

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "student_id", nullable = false, length = 100)
    private String studentId;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Lob
    @Column(name = "ethical_considerations", nullable = false, columnDefinition = "TEXT")
    private String ethicalConsiderations; // NEW: Replaces levelOfStudy

    @Column(name = "academic_year_semester", nullable = false, length = 20)
    private String academicYearSemester; // B_Y1_S1, B_Y1_S2, B_Y2_S1, ..., M_Y1_S1, M_Y1_S2

    @Column(name = "location", nullable = false, length = 20)
    private String location; // Rural or Urban

    // Devices - stored as comma-separated values or use ElementCollection
    @ElementCollection
    @CollectionTable(name = "survey_devices", joinColumns = @JoinColumn(name = "survey_id"))
    @Column(name = "device")
    private Set<String> devices = new HashSet<>();

    @Column(name = "devices_others_specify", length = 200)
    private String devicesOthersSpecify;

    @Column(name = "internet_access", nullable = false, length = 10)
    private String internetAccess; // Yes or No

    @Column(name = "hours_online", nullable = false, length = 20)
    private String hoursOnline; // < 1 hour, 1-3 hours, 4-8 hours, > 8 hours

    @Column(name = "familiar_with_ai", nullable = false, length = 10)
    private String familiarWithAI; // Yes or No

    @Column(name = "use_ai_tools", nullable = false, length = 10)
    private String useAITools; // Yes or No

    // ===== AI Tools Used =====

    @ElementCollection
    @CollectionTable(name = "survey_study_tools", joinColumns = @JoinColumn(name = "survey_id"))
    @Column(name = "tool")
    private Set<String> studyTools = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "survey_writing_tools", joinColumns = @JoinColumn(name = "survey_id"))
    @Column(name = "tool")
    private Set<String> writingTools = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "survey_note_tools", joinColumns = @JoinColumn(name = "survey_id"))
    @Column(name = "tool")
    private Set<String> noteTools = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "survey_research_tools", joinColumns = @JoinColumn(name = "survey_id"))
    @Column(name = "tool")
    private Set<String> researchTools = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "survey_presentation_tools", joinColumns = @JoinColumn(name = "survey_id"))
    @Column(name = "tool")
    private Set<String> presentationTools = new HashSet<>();

    // ===== Section A: Interpretation & Clarification =====

    @Column(name = "ai_usage_frequency", nullable = false, length = 20)
    private String aiUsageFrequency; // Daily, Weekly, Occasionally, Rarely, Never

    @Column(name = "learned_about_ai", nullable = false, columnDefinition = "TEXT")
    private String learnedAboutAI;

    // ===== Audit Fields =====

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "submitted_by_ip", length = 45)
    private String submittedByIp;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    /**
     * Add a device to the devices set
     */
    public void addDevice(String device) {
        if (this.devices == null) {
            this.devices = new HashSet<>();
        }
        this.devices.add(device);
    }

    /**
     * Add a study tool to the studyTools set
     */
    public void addStudyTool(String tool) {
        if (this.studyTools == null) {
            this.studyTools = new HashSet<>();
        }
        this.studyTools.add(tool);
    }

    /**
     * Add a writing tool to the writingTools set
     */
    public void addWritingTool(String tool) {
        if (this.writingTools == null) {
            this.writingTools = new HashSet<>();
        }
        this.writingTools.add(tool);
    }

    /**
     * Add a note tool to the noteTools set
     */
    public void addNoteTool(String tool) {
        if (this.noteTools == null) {
            this.noteTools = new HashSet<>();
        }
        this.noteTools.add(tool);
    }

    /**
     * Add a research tool to the researchTools set
     */
    public void addResearchTool(String tool) {
        if (this.researchTools == null) {
            this.researchTools = new HashSet<>();
        }
        this.researchTools.add(tool);
    }

    /**
     * Add a presentation tool to the presentationTools set
     */
    public void addPresentationTool(String tool) {
        if (this.presentationTools == null) {
            this.presentationTools = new HashSet<>();
        }
        this.presentationTools.add(tool);
    }
}
