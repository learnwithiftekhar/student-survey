package com.iftekhar.ai_paradox.util;

/**
 * Validation utility methods
 */
public final class ValidationUtils {

    private ValidationUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Validate score is within range
     */
    public static boolean isScoreValid(Integer score, Integer maxScore) {
        return score != null && maxScore != null &&
                score >= 0 && score <= maxScore;
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Validate student ID format
     */
    public static boolean isValidStudentId(String studentId) {
        if (isEmpty(studentId)) {
            return false;
        }
        // Example: STU-2024-001
        String studentIdRegex = "^STU-\\d{4}-\\d{3}$";
        return studentId.matches(studentIdRegex);
    }
}
