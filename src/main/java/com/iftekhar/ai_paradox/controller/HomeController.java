package com.iftekhar.ai_paradox.controller;

import com.iftekhar.ai_paradox.dto.SurveyFormDTO;
import com.iftekhar.ai_paradox.service.SurveyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final SurveyService surveyService;


    /**
     * Display the survey form page
     * GET /survey
     */
    @GetMapping
    public String showSurveyForm(Model model) {
        // Add empty DTO for form binding
        if (!model.containsAttribute("surveyForm")) {
            model.addAttribute("surveyForm", new SurveyFormDTO());
        }

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
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Received survey submission from student: {}", surveyFormDTO.getStudentId());

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors found: {}", bindingResult.getAllErrors());
            model.addAttribute("surveyForm", surveyFormDTO);
            model.addAttribute("error", "Please correct the errors in the form.");
            return "student-survey-form";
        }

        // Custom validation for word counts
        if (!surveyFormDTO.isCoreProblemSummaryValid()) {
            log.warn("Core problem summary exceeds 120 words");
            model.addAttribute("surveyForm", surveyFormDTO);
            model.addAttribute("error", "Question 15: Please limit your answer to 120 words. You have " +
                    surveyFormDTO.getCoreProblemSummaryWordCount() + " words.");
            return "student-survey-form";
        }

        if (!surveyFormDTO.isLearningBenefitValid()) {
            log.warn("Learning benefit answer not within 120-150 word range");
            model.addAttribute("surveyForm", surveyFormDTO);
            model.addAttribute("error", "Question 22: Please write between 120 and 150 words. You have " +
                    surveyFormDTO.getLearningBenefitWordCount() + " words.");
            return "student-survey-form";
        }

        // Validate "Others" device specification
        if (!surveyFormDTO.isDevicesOthersValid()) {
            log.warn("Others device selected but not specified");
            model.addAttribute("surveyForm", surveyFormDTO);
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

            log.info("Survey submitted successfully, ID: {}, Student: {}",
                    savedSurvey.getId(), savedSurvey.getStudentId());

            redirectAttributes.addFlashAttribute("success",
                    "Thank you! Your survey has been submitted successfully. Survey ID: " + savedSurvey.getId());
            return "redirect:/survey/success";

        } catch (IllegalArgumentException e) {
            log.error("Duplicate student ID: {}", surveyFormDTO.getStudentId());
            model.addAttribute("surveyForm", surveyFormDTO);
            model.addAttribute("error", "This Student ID has already been used. Please use a unique Student ID.");
            return "student-survey-form";

        } catch (Exception e) {
            log.error("Error saving survey", e);
            model.addAttribute("surveyForm", surveyFormDTO);
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
