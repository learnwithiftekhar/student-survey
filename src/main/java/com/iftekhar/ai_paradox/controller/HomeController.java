package com.iftekhar.ai_paradox.controller;

import com.iftekhar.ai_paradox.dto.SurveyFormDTO;
import com.iftekhar.ai_paradox.model.GroupType;
import com.iftekhar.ai_paradox.service.SurveyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final SurveyService surveyService;

    /**
     * Root URL - Redirect to group selection
     * GET /
     */
    @GetMapping("/")
    public String redirectToGroupSelection() {
        return "redirect:/group-selection";
    }

    /**
     * Display group selection page
     * GET /group-selection
     */
    @GetMapping("/group-selection")
    public String showGroupSelection(Model model) {
        log.info("Displaying group selection page");
        return "group-selection";
    }

    /**
     * Handle group selection and redirect to survey form
     * POST /select-group
     */
    @PostMapping("/select-group")
    public String selectGroup(
            @RequestParam("groupType") String groupTypeStr,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            GroupType groupType = GroupType.valueOf(groupTypeStr);
            session.setAttribute("selectedGroup", groupType);
            log.info("Group selected: {}", groupType);

            return "redirect:/survey";
        } catch (IllegalArgumentException e) {
            log.error("Invalid group type: {}", groupTypeStr);
            redirectAttributes.addFlashAttribute("error", "Invalid group selection. Please try again.");
            return "redirect:/group-selection";
        }
    }

    /**
     * Display the survey form page
     * GET /survey
     */
    @GetMapping("/survey")
    public String showSurveyForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Check if group is selected
        GroupType selectedGroup = (GroupType) session.getAttribute("selectedGroup");

        if (selectedGroup == null) {
            log.warn("Attempted to access survey without group selection");
            redirectAttributes.addFlashAttribute("error", "Please select your group first.");
            return "redirect:/group-selection";
        }

        // Add empty DTO for form binding
        if (!model.containsAttribute("surveyForm")) {
            SurveyFormDTO dto = new SurveyFormDTO();
            dto.setGroupType(selectedGroup);
            model.addAttribute("surveyForm", dto);
        }

        // Add group info to model for conditional rendering
        model.addAttribute("selectedGroup", selectedGroup);
        model.addAttribute("isAIEnabled", selectedGroup == GroupType.GROUP_A);

        log.info("Displaying survey form for {}", selectedGroup);
        return "student-survey-form";
    }

    /**
     * Submit the survey form
     * POST /survey/submit
     */
    @PostMapping("/survey/submit")
    public String submitSurvey(
            @Valid @ModelAttribute("surveyForm") SurveyFormDTO surveyFormDTO,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Received survey submission from student: {}", surveyFormDTO.getStudentId());

        // Ensure group type is set from session
        GroupType selectedGroup = (GroupType) session.getAttribute("selectedGroup");
        if (selectedGroup == null) {
            log.error("No group selected in session");
            redirectAttributes.addFlashAttribute("error", "Session expired. Please select your group again.");
            return "redirect:/group-selection";
        }
        surveyFormDTO.setGroupType(selectedGroup);

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors found: {}", bindingResult.getAllErrors());
            model.addAttribute("surveyForm", surveyFormDTO);
            model.addAttribute("selectedGroup", selectedGroup);
            model.addAttribute("isAIEnabled", selectedGroup == GroupType.GROUP_A);
            model.addAttribute("error", "Please correct the errors in the form.");
            return "student-survey-form";
        }

        // Custom validation for word counts
        if (!surveyFormDTO.isCoreProblemSummaryValid()) {
            log.warn("Core problem summary exceeds 120 words");
            model.addAttribute("surveyForm", surveyFormDTO);
            model.addAttribute("selectedGroup", selectedGroup);
            model.addAttribute("isAIEnabled", selectedGroup == GroupType.GROUP_A);
            model.addAttribute("error", "Question 15: Please limit your answer to 120 words. You have " +
                    surveyFormDTO.getCoreProblemSummaryWordCount() + " words.");
            return "student-survey-form";
        }

        if (!surveyFormDTO.isLearningBenefitValid()) {
            log.warn("Learning benefit answer not within 120-150 word range");
            model.addAttribute("surveyForm", surveyFormDTO);
            model.addAttribute("selectedGroup", selectedGroup);
            model.addAttribute("isAIEnabled", selectedGroup == GroupType.GROUP_A);
            model.addAttribute("error", "Question 22: Please write between 120 and 150 words. You have " +
                    surveyFormDTO.getLearningBenefitWordCount() + " words.");
            return "student-survey-form";
        }

        // Validate "Others" device specification
        if (!surveyFormDTO.isDevicesOthersValid()) {
            log.warn("Others device selected but not specified");
            model.addAttribute("surveyForm", surveyFormDTO);
            model.addAttribute("selectedGroup", selectedGroup);
            model.addAttribute("isAIEnabled", selectedGroup == GroupType.GROUP_A);
            model.addAttribute("error", "Please specify the 'Others' device.");
            return "student-survey-form";
        }

        try {
            // Get client IP address
            String clientIp = getClientIpAddress(request);
            surveyFormDTO.setSubmittedByIp(clientIp);
            surveyFormDTO.setIsCompleted(true);

            // Save the survey
            SurveyFormDTO savedSurvey = surveyService.saveSurvey(surveyFormDTO);

            log.info("Survey submitted successfully, ID: {}, Student: {}, Group: {}",
                    savedSurvey.getId(), savedSurvey.getStudentId(), selectedGroup);

            // Clear session after successful submission
            session.removeAttribute("selectedGroup");

            redirectAttributes.addFlashAttribute("success",
                    "Thank you! Your survey has been submitted successfully. Survey ID: " + savedSurvey.getId());
            return "redirect:/survey/success";

        } catch (IllegalArgumentException e) {
            log.error("Duplicate student ID: {}", surveyFormDTO.getStudentId());
            model.addAttribute("surveyForm", surveyFormDTO);
            model.addAttribute("selectedGroup", selectedGroup);
            model.addAttribute("isAIEnabled", selectedGroup == GroupType.GROUP_A);
            model.addAttribute("error", "This Student ID has already been used. Please use a unique Student ID.");
            return "student-survey-form";

        } catch (Exception e) {
            log.error("Error saving survey", e);
            model.addAttribute("surveyForm", surveyFormDTO);
            model.addAttribute("selectedGroup", selectedGroup);
            model.addAttribute("isAIEnabled", selectedGroup == GroupType.GROUP_A);
            model.addAttribute("error", "An error occurred while submitting the survey. Please try again.");
            return "student-survey-form";
        }
    }

    @GetMapping("/survey/success")
    public String showSuccessPage() {
        log.info("Displaying success page");
        return "survey-success";
    }

    // =====================================================
    // Helper Methods
    // =====================================================

    /**
     * Get client IP address from request
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
