package com.iftekhar.ai_paradox.service;

import com.iftekhar.ai_paradox.dto.QuestionDto;
import com.iftekhar.ai_paradox.mapper.CtQuestionMapper;
import com.iftekhar.ai_paradox.model.CtQuestion;
import com.iftekhar.ai_paradox.repository.CtQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final CtQuestionRepository questionRepository;
    private final CtQuestionMapper questionMapper;

    public QuestionService(CtQuestionRepository questionRepository,
                           CtQuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    public QuestionDto getQuestionById(String questionId) {
        CtQuestion entity = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown questionId: " + questionId));
        return questionMapper.toDto(entity);
    }

    public List<QuestionDto> getActiveQuestions() {
        return questionRepository.findByActiveTrue()
                .stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<QuestionDto> getQuestionsBySection(String section) {
        return questionRepository.findBySectionOrderByNumberAsc(section)
                .stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }
}
