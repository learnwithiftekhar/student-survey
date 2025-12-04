package com.iftekhar.ai_paradox.controller;

import com.iftekhar.ai_paradox.dto.SurveyFormDTO;
import com.iftekhar.ai_paradox.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard")
@Slf4j
@RequiredArgsConstructor
public class DashboardController {
    private final SurveyService surveyService;

    @GetMapping
    public String dashboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        log.info("Listing surveys - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SurveyFormDTO> surveyPage = surveyService.getAllSurveys(pageable);

        model.addAttribute("surveys", surveyPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", surveyPage.getTotalPages());
        model.addAttribute("totalItems", surveyPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        return "dashboard";
    }

    /**
     * View a specific survey by ID
     * GET /survey/view/{id}
     */
    @GetMapping("/view/{id}")
    public String viewSurvey(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Viewing survey with ID: {}", id);

        try {
            SurveyFormDTO survey = surveyService.getSurveyById(id);
            model.addAttribute("survey", survey);
            return "survey-view";

        } catch (Exception e) {
            log.error("Error viewing survey with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Survey not found: " + e.getMessage());
            return "redirect:/survey/list";
        }
    }
}
