package com.iftekhar.ai_paradox.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyFormDTO implements Serializable {

    private Long id;

    // ===== Basic Information =====

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Student ID is required")
    @Size(max = 100, message = "Student ID must not exceed 100 characters")
    private String studentId;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 35, message = "Age must not exceed 35")
    private Integer age;

    @NotBlank(message = "Level of study is required")
    @Pattern(regexp = "Bachelor's|Master's", message = "Invalid level of study")
    private String levelOfStudy;

    @NotBlank(message = "Academic year and semester is required")
    @Pattern(
            regexp = "B_Y1_S1|B_Y1_S2|B_Y2_S1|B_Y2_S2|B_Y3_S1|B_Y3_S2|B_Y4_S1|B_Y4_S2|M_Y1_S1|M_Y1_S2",
            message = "Invalid academic year and semester"
    )
    private String academicYearSemester;

    @NotBlank(message = "Location is required")
    @Pattern(regexp = "Rural|Urban", message = "Invalid location")
    private String location;

    @NotEmpty(message = "At least one device must be selected")
    private Set<String> devices = new HashSet<>();

    @Size(max = 200, message = "Device specification must not exceed 200 characters")
    private String devicesOthersSpecify;

    @NotBlank(message = "Internet access status is required")
    @Pattern(regexp = "Yes|No", message = "Invalid internet access status")
    private String internetAccess;

    @NotBlank(message = "Hours online is required")
    @Pattern(regexp = "< 1 hour|1-3 hours|4-8 hours|> 8 hours", message = "Invalid hours online")
    private String hoursOnline;

    @NotBlank(message = "Familiar with AI status is required")
    @Pattern(regexp = "Yes|No", message = "Invalid familiar with AI status")
    private String familiarWithAI;

    @NotBlank(message = "Use AI tools status is required")
    @Pattern(regexp = "Yes|No", message = "Invalid use AI tools status")
    private String useAITools;

    // ===== AI Tools Used (Optional - can be empty if user doesn't use AI) =====

    private Set<String> studyTools = new HashSet<>();
    private Set<String> writingTools = new HashSet<>();
    private Set<String> noteTools = new HashSet<>();
    private Set<String> researchTools = new HashSet<>();
    private Set<String> presentationTools = new HashSet<>();

    // ===== Section A: Interpretation & Clarification =====

    @NotBlank(message = "AI usage frequency is required")
    @Pattern(regexp = "Daily|Weekly|Occasionally|Rarely|Never", message = "Invalid AI usage frequency")
    private String aiUsageFrequency;

    @NotBlank(message = "Please describe how you learned about AI tools")
    @Size(min = 10, message = "Please provide more details (at least 10 characters)")
    private String learnedAboutAI;

    @NotBlank(message = "Core problem summary is required")
    @Size(min = 50, message = "Please write at least 50 characters")
    private String coreProblemSummary;

    @NotBlank(message = "Peripheral education explanation is required")
    @Size(min = 50, message = "Please write at least 50 characters")
    private String peripheralEducation;

    // ===== Section B: Analysis of Assumptions & Arguments =====

    @NotBlank(message = "Implicit assumptions analysis is required")
    @Size(min = 100, message = "Please provide more detailed explanations (at least 100 characters)")
    private String implicitAssumptions;

    @NotBlank(message = "Argument analysis is required")
    @Size(min = 150, message = "Please provide more detailed analysis (at least 150 characters)")
    private String argumentAnalysis;

    @NotBlank(message = "Evidence vs opinion analysis is required")
    @Size(min = 100, message = "Please provide more details (at least 100 characters)")
    private String evidenceVsOpinion;

    // ===== Section C: Inference, Causal Reasoning & Alternative Explanations =====

    @NotBlank(message = "Table inferences are required")
    @Size(min = 100, message = "Please provide more detailed analysis (at least 100 characters)")
    private String tableInferences;

    @NotBlank(message = "Research design is required")
    @Size(min = 150, message = "Please provide more details (at least 150 characters)")
    private String researchDesign;

    // ===== Section D: Self-Regulation & Meta-Reflection =====

    @NotBlank(message = "Learning benefit answer is required")
    private String learningBenefit;

    @NotBlank(message = "AI usage reflection is required")
    @Size(min = 50, message = "Please provide more details (at least 50 characters)")
    private String aiUsageReflection;

    // ===== Audit Fields =====

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String submittedByIp;
    private Boolean isCompleted;

    // ✅ NEW: Evaluation status (calculated, not stored in DB)
    private Boolean evaluated;
    // ✅ NEW: Overall score fields (calculated, not stored in DB)
    private Integer overallScore;
    private Integer maxPossibleScore;
    private Double scorePercentage;
    private String ctLevel;           // e.g., "Advanced / High CT"
    private String ctLevelColor;

    // ===== Custom Validation Methods =====

    /**
     * Validate word count for core problem summary (max 120 words)
     */
    public boolean isCoreProblemSummaryValid() {
        if (coreProblemSummary == null || coreProblemSummary.trim().isEmpty()) {
            return false;
        }
        int wordCount = coreProblemSummary.trim().split("\\s+").length;
        return wordCount <= 120;
    }

    /**
     * Validate word count for learning benefit (120-150 words)
     */
    public boolean isLearningBenefitValid() {
        if (learningBenefit == null || learningBenefit.trim().isEmpty()) {
            return false;
        }
        int wordCount = learningBenefit.trim().split("\\s+").length;
        return wordCount >= 120 && wordCount <= 150;
    }

    /**
     * Get word count for core problem summary
     */
    public int getCoreProblemSummaryWordCount() {
        if (coreProblemSummary == null || coreProblemSummary.trim().isEmpty()) {
            return 0;
        }
        return coreProblemSummary.trim().split("\\s+").length;
    }

    /**
     * Get word count for learning benefit
     */
    public int getLearningBenefitWordCount() {
        if (learningBenefit == null || learningBenefit.trim().isEmpty()) {
            return 0;
        }
        return learningBenefit.trim().split("\\s+").length;
    }

    /**
     * Check if "Others" is selected in devices and specification is provided
     */
    public boolean isDevicesOthersValid() {
        if (devices != null && devices.contains("Others")) {
            return devicesOthersSpecify != null && !devicesOthersSpecify.trim().isEmpty();
        }
        return true;
    }
}