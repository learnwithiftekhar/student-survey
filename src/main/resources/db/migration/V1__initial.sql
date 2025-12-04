-- =====================================================
-- Database Schema for Student AI Usage Survey
-- MySQL Database
-- =====================================================

-- Create database
-- CREATE DATABASE IF NOT EXISTS student_survey_db
-- CHARACTER SET utf8mb4
-- COLLATE utf8mb4_unicode_ci;
--
-- USE student_survey_db;

-- =====================================================
-- Main Survey Form Table
-- =====================================================
CREATE TABLE IF NOT EXISTS survey_forms (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Basic Information
                                            name VARCHAR(100) NOT NULL,
    student_id VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    level_of_study VARCHAR(50) NOT NULL,
    academic_year_semester VARCHAR(10) NOT NULL,
    location VARCHAR(20) NOT NULL,
    devices_others_specify VARCHAR(200),
    internet_access VARCHAR(10) NOT NULL,
    hours_online VARCHAR(20) NOT NULL,
    familiar_with_ai VARCHAR(10) NOT NULL,
    use_ai_tools VARCHAR(10) NOT NULL,

    -- Section A: Interpretation & Clarification
    ai_usage_frequency VARCHAR(20) NOT NULL,
    learned_about_ai TEXT NOT NULL,
    core_problem_summary TEXT NOT NULL,
    peripheral_education TEXT NOT NULL,

    -- Section B: Analysis of Assumptions & Arguments
    implicit_assumptions TEXT NOT NULL,
    argument_analysis TEXT NOT NULL,
    evidence_vs_opinion TEXT NOT NULL,

    -- Section C: Inference, Causal Reasoning & Alternative Explanations
    table_inferences TEXT NOT NULL,
    research_design TEXT NOT NULL,

    -- Section D: Self-Regulation & Meta-Reflection
    learning_benefit TEXT NOT NULL,
    ai_usage_reflection TEXT NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    submitted_by_ip VARCHAR(45),
    is_completed BOOLEAN DEFAULT FALSE NOT NULL,

    -- Indexes
    INDEX idx_student_id (student_id),
    INDEX idx_level_of_study (level_of_study),
    INDEX idx_location (location),
    INDEX idx_use_ai_tools (use_ai_tools),
    INDEX idx_ai_usage_frequency (ai_usage_frequency),
    INDEX idx_created_at (created_at),
    INDEX idx_is_completed (is_completed),

    -- Unique constraint
    UNIQUE KEY unique_student_id (student_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Devices Collection Table
-- =====================================================
CREATE TABLE IF NOT EXISTS survey_devices (
                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              survey_id BIGINT NOT NULL,
                                              device VARCHAR(100) NOT NULL,

    FOREIGN KEY (survey_id) REFERENCES survey_forms(id) ON DELETE CASCADE,
    INDEX idx_survey_id (survey_id),
    INDEX idx_device (device)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- AI Tools Collection Tables
-- =====================================================

-- Study Tools
CREATE TABLE IF NOT EXISTS survey_study_tools (
                                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                  survey_id BIGINT NOT NULL,
                                                  tool VARCHAR(100) NOT NULL,

    FOREIGN KEY (survey_id) REFERENCES survey_forms(id) ON DELETE CASCADE,
    INDEX idx_survey_id (survey_id),
    INDEX idx_tool (tool)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Writing Tools
CREATE TABLE IF NOT EXISTS survey_writing_tools (
                                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                    survey_id BIGINT NOT NULL,
                                                    tool VARCHAR(100) NOT NULL,

    FOREIGN KEY (survey_id) REFERENCES survey_forms(id) ON DELETE CASCADE,
    INDEX idx_survey_id (survey_id),
    INDEX idx_tool (tool)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Note Tools
CREATE TABLE IF NOT EXISTS survey_note_tools (
                                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                 survey_id BIGINT NOT NULL,
                                                 tool VARCHAR(100) NOT NULL,

    FOREIGN KEY (survey_id) REFERENCES survey_forms(id) ON DELETE CASCADE,
    INDEX idx_survey_id (survey_id),
    INDEX idx_tool (tool)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Research Tools
CREATE TABLE IF NOT EXISTS survey_research_tools (
                                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                     survey_id BIGINT NOT NULL,
                                                     tool VARCHAR(100) NOT NULL,

    FOREIGN KEY (survey_id) REFERENCES survey_forms(id) ON DELETE CASCADE,
    INDEX idx_survey_id (survey_id),
    INDEX idx_tool (tool)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Presentation Tools
CREATE TABLE IF NOT EXISTS survey_presentation_tools (
                                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                         survey_id BIGINT NOT NULL,
                                                         tool VARCHAR(100) NOT NULL,

    FOREIGN KEY (survey_id) REFERENCES survey_forms(id) ON DELETE CASCADE,
    INDEX idx_survey_id (survey_id),
    INDEX idx_tool (tool)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Useful Views for Analytics
-- =====================================================

-- View: Survey statistics by level of study
CREATE OR REPLACE VIEW v_survey_stats_by_level AS
SELECT
    level_of_study,
    COUNT(*) as total_surveys,
    SUM(CASE WHEN use_ai_tools = 'Yes' THEN 1 ELSE 0 END) as ai_users,
    SUM(CASE WHEN use_ai_tools = 'No' THEN 1 ELSE 0 END) as non_ai_users,
    ROUND(AVG(age), 2) as avg_age
FROM survey_forms
WHERE is_completed = TRUE
GROUP BY level_of_study;

-- View: Survey statistics by location
CREATE OR REPLACE VIEW v_survey_stats_by_location AS
SELECT
    location,
    COUNT(*) as total_surveys,
    SUM(CASE WHEN use_ai_tools = 'Yes' THEN 1 ELSE 0 END) as ai_users,
    SUM(CASE WHEN internet_access = 'Yes' THEN 1 ELSE 0 END) as with_internet,
    ROUND(AVG(age), 2) as avg_age
FROM survey_forms
WHERE is_completed = TRUE
GROUP BY location;

-- View: AI usage frequency distribution
CREATE OR REPLACE VIEW v_ai_usage_frequency_dist AS
SELECT
    ai_usage_frequency,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM survey_forms WHERE is_completed = TRUE), 2) as percentage
FROM survey_forms
WHERE is_completed = TRUE
GROUP BY ai_usage_frequency
ORDER BY
    FIELD(ai_usage_frequency, 'Daily', 'Weekly', 'Occasionally', 'Rarely', 'Never');

-- View: Most popular AI tools
CREATE OR REPLACE VIEW v_popular_ai_tools AS
SELECT 'Study' as category, tool, COUNT(*) as usage_count
FROM survey_study_tools sst
         JOIN survey_forms sf ON sst.survey_id = sf.id
WHERE sf.is_completed = TRUE
GROUP BY tool
UNION ALL
SELECT 'Writing' as category, tool, COUNT(*) as usage_count
FROM survey_writing_tools swt
         JOIN survey_forms sf ON swt.survey_id = sf.id
WHERE sf.is_completed = TRUE
GROUP BY tool
UNION ALL
SELECT 'Note-taking' as category, tool, COUNT(*) as usage_count
FROM survey_note_tools snt
         JOIN survey_forms sf ON snt.survey_id = sf.id
WHERE sf.is_completed = TRUE
GROUP BY tool
UNION ALL
SELECT 'Research' as category, tool, COUNT(*) as usage_count
FROM survey_research_tools srt
         JOIN survey_forms sf ON srt.survey_id = sf.id
WHERE sf.is_completed = TRUE
GROUP BY tool
UNION ALL
SELECT 'Presentation' as category, tool, COUNT(*) as usage_count
FROM survey_presentation_tools spt
         JOIN survey_forms sf ON spt.survey_id = sf.id
WHERE sf.is_completed = TRUE
GROUP BY tool
ORDER BY usage_count DESC;

-- =====================================================
-- Sample Queries for Analytics
-- =====================================================

-- Query 1: Get all surveys with AI usage
-- SELECT * FROM survey_forms WHERE use_ai_tools = 'Yes' AND is_completed = TRUE;

-- Query 2: Count surveys by academic year
-- SELECT academic_year_semester, COUNT(*) as count
-- FROM survey_forms
-- WHERE is_completed = TRUE
-- GROUP BY academic_year_semester;

-- Query 3: Get surveys from rural areas using AI daily
-- SELECT * FROM survey_forms
-- WHERE location = 'Rural'
-- AND ai_usage_frequency = 'Daily'
-- AND is_completed = TRUE;

-- Query 4: Average age by level of study and AI usage
-- SELECT level_of_study, use_ai_tools, AVG(age) as avg_age
-- FROM survey_forms
-- WHERE is_completed = TRUE
-- GROUP BY level_of_study, use_ai_tools;

-- Query 5: Get devices distribution
-- SELECT device, COUNT(*) as count
-- FROM survey_devices sd
-- JOIN survey_forms sf ON sd.survey_id = sf.id
-- WHERE sf.is_completed = TRUE
-- GROUP BY device
-- ORDER BY count DESC;

-- =====================================================
-- Stored Procedures (Optional)
-- =====================================================

-- Drop existing procedures if they exist
DROP PROCEDURE IF EXISTS sp_get_survey_summary;
DROP PROCEDURE IF EXISTS sp_get_tools_by_category;

DELIMITER //

-- Procedure: Get survey statistics summary
CREATE PROCEDURE sp_get_survey_summary()
BEGIN
SELECT
    COUNT(*) as total_surveys,
    COUNT(CASE WHEN is_completed = TRUE THEN 1 END) as completed_surveys,
    COUNT(CASE WHEN use_ai_tools = 'Yes' THEN 1 END) as ai_users,
    COUNT(CASE WHEN location = 'Urban' THEN 1 END) as urban_respondents,
    COUNT(CASE WHEN location = 'Rural' THEN 1 END) as rural_respondents,
    ROUND(AVG(age), 2) as avg_age,
    MIN(created_at) as first_submission,
    MAX(created_at) as last_submission
FROM survey_forms;
END //

-- Procedure: Get AI tools usage by category
CREATE PROCEDURE sp_get_tools_by_category(IN p_category VARCHAR(50))
BEGIN
CASE p_category
        WHEN 'study' THEN
SELECT tool, COUNT(*) as usage_count
FROM survey_study_tools sst
         JOIN survey_forms sf ON sst.survey_id = sf.id
WHERE sf.is_completed = TRUE
GROUP BY tool
ORDER BY usage_count DESC;
WHEN 'writing' THEN
SELECT tool, COUNT(*) as usage_count
FROM survey_writing_tools swt
         JOIN survey_forms sf ON swt.survey_id = sf.id
WHERE sf.is_completed = TRUE
GROUP BY tool
ORDER BY usage_count DESC;
WHEN 'note' THEN
SELECT tool, COUNT(*) as usage_count
FROM survey_note_tools snt
         JOIN survey_forms sf ON snt.survey_id = sf.id
WHERE sf.is_completed = TRUE
GROUP BY tool
ORDER BY usage_count DESC;
WHEN 'research' THEN
SELECT tool, COUNT(*) as usage_count
FROM survey_research_tools srt
         JOIN survey_forms sf ON srt.survey_id = sf.id
WHERE sf.is_completed = TRUE
GROUP BY tool
ORDER BY usage_count DESC;
WHEN 'presentation' THEN
SELECT tool, COUNT(*) as usage_count
FROM survey_presentation_tools spt
         JOIN survey_forms sf ON spt.survey_id = sf.id
WHERE sf.is_completed = TRUE
GROUP BY tool
ORDER BY usage_count DESC;
END CASE;
END //

DELIMITER ;

-- =====================================================
-- Insert Sample Data (Optional - for testing)
-- =====================================================

-- You can uncomment and modify this section to insert test data
/*
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester,
    location, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai, core_problem_summary,
    peripheral_education, implicit_assumptions, argument_analysis,
    evidence_vs_opinion, table_inferences, research_design,
    learning_benefit, ai_usage_reflection, is_completed
) VALUES (
    'John Doe', 'STU001', 22, 'Bachelor''s', '131',
    'Urban', 'Yes', '4-8 hours', 'Yes', 'Yes',
    'Daily', 'Learned from social media and YouTube',
    'AI tools are increasingly used by students...',
    'Peripheral education refers to...',
    'Assumption 1: Higher grades equal better learning...',
    'The argument can be reconstructed as follows...',
    'Evidence 1: Grade improvements... Opinion 1: Teachers believe...',
    'From the data, we can infer that...',
    'Research Question: Does AI usage affect critical thinking...',
    'I believe guided AI use would benefit my learning because...',
    'During this test, I used ChatGPT for...',
    TRUE
);
*/

-- =====================================================
-- End of Schema
-- =====================================================