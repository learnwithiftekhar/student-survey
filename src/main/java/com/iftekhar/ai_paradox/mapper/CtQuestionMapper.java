package com.iftekhar.ai_paradox.mapper;

import com.iftekhar.ai_paradox.dto.QuestionDto;
import com.iftekhar.ai_paradox.dto.SkillDto;
import com.iftekhar.ai_paradox.model.CtQuestion;
import com.iftekhar.ai_paradox.model.CtSkill;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CtQuestionMapper {
    public QuestionDto toDto(CtQuestion entity) {
        if (entity == null) return null;

        QuestionDto dto = new QuestionDto();
        dto.setId(entity.getId());
        dto.setSection(entity.getSection());
        dto.setNumber(entity.getNumber());
        dto.setTitle(entity.getTitle());
        dto.setText(entity.getText());
        dto.setRubric(entity.getRubric());
        dto.setMaxScore(entity.getMaxScore());
        dto.setActive(entity.getActive());

        List<SkillDto> skills = entity.getSkills()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        dto.setSkills(skills);

        return dto;
    }

    public SkillDto toDto(CtSkill skill) {
        SkillDto dto = new SkillDto();
        dto.setId(skill.getId());
        dto.setCode(skill.getCode());
        dto.setName(skill.getName());
        return dto;
    }
}
