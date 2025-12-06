-- V9__change_level_of_study_to_ethical_considerations.sql
-- Change level_of_study field to ethical_considerations

-- Step 1: Add new column for ethical considerations
ALTER TABLE survey_forms
    ADD COLUMN ethical_considerations TEXT NULL AFTER age;

-- Step 2: Drop the old level_of_study column and its index
DROP INDEX idx_level_of_study ON survey_forms;
ALTER TABLE survey_forms
DROP COLUMN level_of_study;

-- Step 3: Update views that reference level_of_study
DROP VIEW IF EXISTS v_survey_stats_by_level;

-- Note: If you have existing data, you may want to backup first
-- The survey_forms table will no longer have level_of_study field