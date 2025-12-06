package com.iftekhar.ai_paradox.util;

/**
 * Application-wide constants
 */
public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Critical Thinking Score Ranges
    public static final class CTScoreRanges {
        public static final int ADVANCED_MIN = 48;
        public static final int COMPETENT_MIN = 36;
        public static final int EMERGING_MIN = 24;
        public static final int MAX_POSSIBLE_SCORE = 60;

        private CTScoreRanges() {}
    }

    // CT Level Colors
    public static final class CTLevelColors {
        public static final String GREEN = "green";
        public static final String BLUE = "blue";
        public static final String YELLOW = "yellow";
        public static final String RED = "red";

        private CTLevelColors() {}
    }

    // CT Level Labels
    public static final class CTLevelLabels {
        public static final String ADVANCED = "Advanced / High CT";
        public static final String COMPETENT = "Competent / Developing CT";
        public static final String EMERGING = "Emerging / Basic CT";
        public static final String AT_RISK = "At Risk / Limited CT";

        private CTLevelLabels() {}
    }

    // CT Level Descriptions
    public static final class CTLevelDescriptions {
        public static final String ADVANCED = "Strong ability to interpret, analyse, evaluate, infer, design studies, and reflect on own learning and AI use.";
        public static final String COMPETENT = "Demonstrates consistent, though uneven, critical thinking. Can analyse and infer but may miss complexity.";
        public static final String EMERGING = "Shows basic understanding of case and some ability to identify assumptions or draw inferences.";
        public static final String AT_RISK = "Struggles to interpret the problem, recognise assumptions, or construct coherent arguments.";

        private CTLevelDescriptions() {}
    }

    // Pagination
    public static final class Pagination {
        public static final int DEFAULT_PAGE_SIZE = 10;
        public static final int MAX_PAGE_SIZE = 100;
        public static final String DEFAULT_SORT_BY = "createdAt";
        public static final String DEFAULT_SORT_DIR = "desc";

        private Pagination() {}
    }
}
