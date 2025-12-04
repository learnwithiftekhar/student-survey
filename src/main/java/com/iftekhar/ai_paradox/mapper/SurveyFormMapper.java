package com.iftekhar.ai_paradox.mapper;

import com.iftekhar.ai_paradox.dto.SurveyFormDTO;
import com.iftekhar.ai_paradox.model.SurveyForm;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class SurveyFormMapper {
    /**
     * Convert SurveyForm entity to SurveyFormDTO
     */
    public SurveyFormDTO toDTO(SurveyForm entity) {
        if (entity == null) {
            return null;
        }

        SurveyFormDTO dto = new SurveyFormDTO();

        // Basic fields
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStudentId(entity.getStudentId());
        dto.setAge(entity.getAge());
        dto.setLevelOfStudy(entity.getLevelOfStudy());
        dto.setAcademicYearSemester(entity.getAcademicYearSemester());
        dto.setLocation(entity.getLocation());

        // Collections - create new HashSet to avoid lazy loading issues
        dto.setDevices(entity.getDevices() != null ? new HashSet<>(entity.getDevices()) : new HashSet<>());
        dto.setDevicesOthersSpecify(entity.getDevicesOthersSpecify());

        dto.setInternetAccess(entity.getInternetAccess());
        dto.setHoursOnline(entity.getHoursOnline());
        dto.setFamiliarWithAI(entity.getFamiliarWithAI());
        dto.setUseAITools(entity.getUseAITools());

        // AI Tools
        dto.setStudyTools(entity.getStudyTools() != null ? new HashSet<>(entity.getStudyTools()) : new HashSet<>());
        dto.setWritingTools(entity.getWritingTools() != null ? new HashSet<>(entity.getWritingTools()) : new HashSet<>());
        dto.setNoteTools(entity.getNoteTools() != null ? new HashSet<>(entity.getNoteTools()) : new HashSet<>());
        dto.setResearchTools(entity.getResearchTools() != null ? new HashSet<>(entity.getResearchTools()) : new HashSet<>());
        dto.setPresentationTools(entity.getPresentationTools() != null ? new HashSet<>(entity.getPresentationTools()) : new HashSet<>());

        // Section A
        dto.setAiUsageFrequency(entity.getAiUsageFrequency());
        dto.setLearnedAboutAI(entity.getLearnedAboutAI());
        dto.setCoreProblemSummary(entity.getCoreProblemSummary());
        dto.setPeripheralEducation(entity.getPeripheralEducation());

        // Section B
        dto.setImplicitAssumptions(entity.getImplicitAssumptions());
        dto.setArgumentAnalysis(entity.getArgumentAnalysis());
        dto.setEvidenceVsOpinion(entity.getEvidenceVsOpinion());

        // Section C
        dto.setTableInferences(entity.getTableInferences());
        dto.setResearchDesign(entity.getResearchDesign());

        // Section D
        dto.setLearningBenefit(entity.getLearningBenefit());
        dto.setAiUsageReflection(entity.getAiUsageReflection());

        // Audit fields
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setSubmittedByIp(entity.getSubmittedByIp());
        dto.setIsCompleted(entity.getIsCompleted());

        return dto;
    }

    /**
     * Convert SurveyFormDTO to SurveyForm entity
     */
    public SurveyForm toEntity(SurveyFormDTO dto) {
        if (dto == null) {
            return null;
        }

        SurveyForm entity = new SurveyForm();

        // Basic fields
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setStudentId(dto.getStudentId());
        entity.setAge(dto.getAge());
        entity.setLevelOfStudy(dto.getLevelOfStudy());
        entity.setAcademicYearSemester(dto.getAcademicYearSemester());
        entity.setLocation(dto.getLocation());

        // Collections
        entity.setDevices(dto.getDevices() != null ? new HashSet<>(dto.getDevices()) : new HashSet<>());
        entity.setDevicesOthersSpecify(dto.getDevicesOthersSpecify());

        entity.setInternetAccess(dto.getInternetAccess());
        entity.setHoursOnline(dto.getHoursOnline());
        entity.setFamiliarWithAI(dto.getFamiliarWithAI());
        entity.setUseAITools(dto.getUseAITools());

        // AI Tools
        entity.setStudyTools(dto.getStudyTools() != null ? new HashSet<>(dto.getStudyTools()) : new HashSet<>());
        entity.setWritingTools(dto.getWritingTools() != null ? new HashSet<>(dto.getWritingTools()) : new HashSet<>());
        entity.setNoteTools(dto.getNoteTools() != null ? new HashSet<>(dto.getNoteTools()) : new HashSet<>());
        entity.setResearchTools(dto.getResearchTools() != null ? new HashSet<>(dto.getResearchTools()) : new HashSet<>());
        entity.setPresentationTools(dto.getPresentationTools() != null ? new HashSet<>(dto.getPresentationTools()) : new HashSet<>());

        // Section A
        entity.setAiUsageFrequency(dto.getAiUsageFrequency());
        entity.setLearnedAboutAI(dto.getLearnedAboutAI());
        entity.setCoreProblemSummary(dto.getCoreProblemSummary());
        entity.setPeripheralEducation(dto.getPeripheralEducation());

        // Section B
        entity.setImplicitAssumptions(dto.getImplicitAssumptions());
        entity.setArgumentAnalysis(dto.getArgumentAnalysis());
        entity.setEvidenceVsOpinion(dto.getEvidenceVsOpinion());

        // Section C
        entity.setTableInferences(dto.getTableInferences());
        entity.setResearchDesign(dto.getResearchDesign());

        // Section D
        entity.setLearningBenefit(dto.getLearningBenefit());
        entity.setAiUsageReflection(dto.getAiUsageReflection());

        // Audit fields
        entity.setSubmittedByIp(dto.getSubmittedByIp());
        entity.setIsCompleted(dto.getIsCompleted() != null ? dto.getIsCompleted() : false);

        return entity;
    }

    /**
     * Update existing entity with DTO data
     */
    public void updateEntityFromDTO(SurveyFormDTO dto, SurveyForm entity) {
        if (dto == null || entity == null) {
            return;
        }

        // Update basic fields
        entity.setName(dto.getName());
        entity.setStudentId(dto.getStudentId());
        entity.setAge(dto.getAge());
        entity.setLevelOfStudy(dto.getLevelOfStudy());
        entity.setAcademicYearSemester(dto.getAcademicYearSemester());
        entity.setLocation(dto.getLocation());

        // Update collections
        entity.setDevices(dto.getDevices() != null ? new HashSet<>(dto.getDevices()) : new HashSet<>());
        entity.setDevicesOthersSpecify(dto.getDevicesOthersSpecify());

        entity.setInternetAccess(dto.getInternetAccess());
        entity.setHoursOnline(dto.getHoursOnline());
        entity.setFamiliarWithAI(dto.getFamiliarWithAI());
        entity.setUseAITools(dto.getUseAITools());

        // Update AI Tools
        entity.setStudyTools(dto.getStudyTools() != null ? new HashSet<>(dto.getStudyTools()) : new HashSet<>());
        entity.setWritingTools(dto.getWritingTools() != null ? new HashSet<>(dto.getWritingTools()) : new HashSet<>());
        entity.setNoteTools(dto.getNoteTools() != null ? new HashSet<>(dto.getNoteTools()) : new HashSet<>());
        entity.setResearchTools(dto.getResearchTools() != null ? new HashSet<>(dto.getResearchTools()) : new HashSet<>());
        entity.setPresentationTools(dto.getPresentationTools() != null ? new HashSet<>(dto.getPresentationTools()) : new HashSet<>());

        // Update Section A
        entity.setAiUsageFrequency(dto.getAiUsageFrequency());
        entity.setLearnedAboutAI(dto.getLearnedAboutAI());
        entity.setCoreProblemSummary(dto.getCoreProblemSummary());
        entity.setPeripheralEducation(dto.getPeripheralEducation());

        // Update Section B
        entity.setImplicitAssumptions(dto.getImplicitAssumptions());
        entity.setArgumentAnalysis(dto.getArgumentAnalysis());
        entity.setEvidenceVsOpinion(dto.getEvidenceVsOpinion());

        // Update Section C
        entity.setTableInferences(dto.getTableInferences());
        entity.setResearchDesign(dto.getResearchDesign());

        // Update Section D
        entity.setLearningBenefit(dto.getLearningBenefit());
        entity.setAiUsageReflection(dto.getAiUsageReflection());

        // Update audit fields
        entity.setSubmittedByIp(dto.getSubmittedByIp());
        if (dto.getIsCompleted() != null) {
            entity.setIsCompleted(dto.getIsCompleted());
        }
    }
}
