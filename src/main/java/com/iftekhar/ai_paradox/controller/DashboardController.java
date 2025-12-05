package com.iftekhar.ai_paradox.controller;

import com.iftekhar.ai_paradox.dto.BatchEvaluationRequest;
import com.iftekhar.ai_paradox.dto.BatchEvaluationResult;
import com.iftekhar.ai_paradox.dto.EvaluationDisplayDto;
import com.iftekhar.ai_paradox.dto.SurveyFormDTO;
import com.iftekhar.ai_paradox.service.CtScoringService;
import com.iftekhar.ai_paradox.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
@Slf4j
@RequiredArgsConstructor
public class DashboardController {
    private final SurveyService surveyService;
    private final CtScoringService scoringService;  // ✅ Add this


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

        long evaluatedCount = surveyService.getEvaluatedSurveysCount();


        model.addAttribute("surveys", surveyPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", surveyPage.getTotalPages());
        model.addAttribute("totalItems", surveyPage.getTotalElements());
        model.addAttribute("evaluatedCount", evaluatedCount);  // ✅ Add this
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
            // ✅ Add evaluation data if survey is evaluated
            if (survey.getEvaluated()) {
                List<EvaluationDisplayDto> evaluations = surveyService.getEvaluationsBySurveyId(id);
                Map<String, Object> overallScore = surveyService.getOverallEvaluationScore(id);

                model.addAttribute("evaluations", evaluations);
                model.addAttribute("overallScore", overallScore);

                log.info("Loaded evaluation data for survey: {}, total score: {}/{}",
                        id, overallScore.get("totalScore"), overallScore.get("maxPossibleScore"));
            }
            return "survey-view";

        } catch (Exception e) {
            log.error("Error viewing survey with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Survey not found: " + e.getMessage());
            return "redirect:/survey/list";
        }
    }

    /**
     * NEW: Evaluate answers for a survey
     * POST /dashboard/evaluate/{id}
     */
    @PostMapping("/evaluate/{id}")
    public String evaluateSurvey(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Evaluating survey with ID: {}", id);

        try {
            // Create evaluation request
            BatchEvaluationRequest request = new BatchEvaluationRequest();
            request.setSurveyId(id);

            // Call scoring service
            BatchEvaluationResult result = scoringService.evaluateSurvey(request);

            if (result.isSuccess()) {
                redirectAttributes.addFlashAttribute("success",
                        "Survey evaluated successfully! " + result.getEvaluations().size() + " answers were scored.");
                log.info("Successfully evaluated survey: {}", id);
            } else {
                redirectAttributes.addFlashAttribute("error",
                        "Evaluation failed: " + result.getErrorMessage());
                log.error("Failed to evaluate survey: {}, error: {}", id, result.getErrorMessage());
            }

        } catch (Exception e) {
            log.error("Error evaluating survey with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error",
                    "An error occurred while evaluating the survey: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }

    // TODO: REMOVE THIS METHOD
    /**
     * Delete a survey
     * POST /dashboard/delete/{id}
     */
    @PostMapping("/delete/{id}")
    public String deleteSurvey(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Deleting survey with ID: {}", id);

        try {
            surveyService.deleteSurvey(id);
            redirectAttributes.addFlashAttribute("success",
                    "Survey deleted successfully!");
            log.info("Successfully deleted survey: {}", id);
        } catch (Exception e) {
            log.error("Error deleting survey with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error",
                    "An error occurred while deleting the survey: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }

}
