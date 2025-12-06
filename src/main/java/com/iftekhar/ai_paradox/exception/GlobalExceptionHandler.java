package com.iftekhar.ai_paradox.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Global exception handler for the application
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * Handle ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(
            ResourceNotFoundException ex,
            RedirectAttributes redirectAttributes) {

        log.error("Resource not found: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/dashboard";
    }


    /**
     * Handle EvaluationException
     */
    @ExceptionHandler(EvaluationException.class)
    public String handleEvaluationException(
            EvaluationException ex,
            RedirectAttributes redirectAttributes) {
        log.error("Evaluation failed: {}", ex.getMessage(), ex);
        String errorMessage = "Evaluation failed: " + ex.getMessage();
        if (ex.getSurveyId() != null) {
            errorMessage += " (Survey ID: " + ex.getSurveyId() + ")";
        }
        redirectAttributes.addFlashAttribute("error", errorMessage);
        return "redirect:/dashboard";
    }

    /**
     * Handle general exceptions
     */
    @ExceptionHandler(Exception.class)
    public String handleGlobalException(
            Exception ex,
            RedirectAttributes redirectAttributes) {
        log.error("Unexpected error occurred", ex);
        redirectAttributes.addFlashAttribute("error",
                "An unexpected error occurred: " + ex.getMessage());
        return "redirect:/dashboard";
    }
}
