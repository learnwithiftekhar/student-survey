-- V7__drop_answer_columns_from_survey_forms.sql
-- Remove duplicate answer columns from survey_forms table

ALTER TABLE survey_forms
DROP COLUMN core_problem_summary,
DROP COLUMN peripheral_education,
DROP COLUMN implicit_assumptions,
DROP COLUMN argument_analysis,
DROP COLUMN evidence_vs_opinion,
DROP COLUMN table_inferences,
DROP COLUMN research_design,
DROP COLUMN learning_benefit,
DROP COLUMN ai_usage_reflection;