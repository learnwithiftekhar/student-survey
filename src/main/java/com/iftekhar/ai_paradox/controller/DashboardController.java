package com.iftekhar.ai_paradox.controller;

import com.iftekhar.ai_paradox.dto.BatchEvaluationRequest;
import com.iftekhar.ai_paradox.dto.BatchEvaluationResult;
import com.iftekhar.ai_paradox.dto.EvaluationDisplayDto;
import com.iftekhar.ai_paradox.dto.SurveyFormDTO;
import com.iftekhar.ai_paradox.model.GroupType;
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
            @RequestParam(required = false) String group,
            Model model
    ) {
        log.info("Listing surveys - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // ✅ NEW - Filter by group if specified
        Page<SurveyFormDTO> surveyPage;
        if (group != null && !group.isEmpty()) {
            try {
                GroupType groupType = GroupType.valueOf(group);
                surveyPage = surveyService.getAllSurveysByGroup(groupType, pageable);
                model.addAttribute("groupFilter", group);
                log.info("Filtered surveys by {}: {} results", groupType, surveyPage.getTotalElements());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid group filter: {}", group);
                surveyPage = surveyService.getAllSurveys(pageable);
                model.addAttribute("groupFilter", null);
            }
        } else {
            surveyPage = surveyService.getAllSurveys(pageable);
            model.addAttribute("groupFilter", null);
        }


        long evaluatedCount = surveyService.getEvaluatedSurveysCount();

        long groupACount = surveyService.countByGroup(GroupType.GROUP_A);
        long groupBCount = surveyService.countByGroup(GroupType.GROUP_B);


        model.addAttribute("surveys", surveyPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", surveyPage.getTotalPages());
        model.addAttribute("totalItems", surveyPage.getTotalElements());
        model.addAttribute("evaluatedCount", evaluatedCount);
        model.addAttribute("groupACount", groupACount);
        model.addAttribute("groupBCount", groupBCount);
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

    /**
     * Evaluate all pending surveys
     * POST /dashboard/evaluate-all
     */
    @PostMapping("/evaluate-all")
    public String evaluateAllPendingSurveys(RedirectAttributes redirectAttributes) {
        log.info("Starting batch evaluation of all pending surveys");

        try {
            // Get all unevaluated survey IDs
            List<Long> unevaluatedSurveyIds = surveyService.getUnevaluatedSurveyIds();

            if (unevaluatedSurveyIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("info",
                        "No pending surveys to evaluate. All surveys have already been evaluated.");
                log.info("No pending surveys found for evaluation");
                return "redirect:/dashboard";
            }

            log.info("Found {} pending surveys to evaluate", unevaluatedSurveyIds.size());

            int successCount = 0;
            int failureCount = 0;
            StringBuilder errorMessages = new StringBuilder();

            // Evaluate each survey
            for (Long surveyId : unevaluatedSurveyIds) {
                try {
                    log.info("Evaluating survey ID: {}", surveyId);

                    BatchEvaluationRequest request = new BatchEvaluationRequest();
                    request.setSurveyId(surveyId);

                    BatchEvaluationResult result = scoringService.evaluateSurvey(request);

                    if (result.isSuccess()) {
                        successCount++;
                        log.info("Successfully evaluated survey: {}", surveyId);
                    } else {
                        failureCount++;
                        errorMessages.append("Survey ").append(surveyId)
                                .append(": ").append(result.getErrorMessage()).append("; ");
                        log.error("Failed to evaluate survey: {}, error: {}",
                                surveyId, result.getErrorMessage());
                    }

                } catch (Exception e) {
                    failureCount++;
                    errorMessages.append("Survey ").append(surveyId)
                            .append(": ").append(e.getMessage()).append("; ");
                    log.error("Exception while evaluating survey: {}", surveyId, e);
                }
            }

            // Prepare success/error message
            if (failureCount == 0) {
                redirectAttributes.addFlashAttribute("success",
                        String.format("Successfully evaluated all %d pending surveys!", successCount));
                log.info("Batch evaluation completed successfully: {} surveys", successCount);
            } else if (successCount > 0) {
                redirectAttributes.addFlashAttribute("warning",
                        String.format("Evaluated %d surveys successfully, but %d failed. Errors: %s",
                                successCount, failureCount, errorMessages.toString()));
                log.warn("Batch evaluation completed with failures: {} success, {} failed",
                        successCount, failureCount);
            } else {
                redirectAttributes.addFlashAttribute("error",
                        String.format("Failed to evaluate all %d surveys. Errors: %s",
                                failureCount, errorMessages.toString()));
                log.error("Batch evaluation failed completely: {} surveys", failureCount);
            }

        } catch (Exception e) {
            log.error("Error during batch evaluation", e);
            redirectAttributes.addFlashAttribute("error",
                    "An error occurred during batch evaluation: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }

    /**
     * ✅ NEW - Show group comparison page
     */
    @GetMapping("/group-comparison")
    public String showGroupComparison(Model model) {
        log.info("Loading group comparison statistics");

        try {
            Map<String, Object> comparison = surveyService.compareGroups();
            model.addAttribute("comparison", comparison);

            return "group-comparison";
        } catch (Exception e) {
            log.error("Error loading group comparison", e);
            model.addAttribute("error", "Failed to load comparison: " + e.getMessage());
            return "dashboard";
        }
    }
}
