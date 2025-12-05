-- New migration: V5__refactor_survey_answers.sql

-- 1. Create new answer table
CREATE TABLE survey_answer (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               survey_id BIGINT NOT NULL,
                               question_id VARCHAR(50) NOT NULL,
                               answer_text LONGTEXT NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                               CONSTRAINT fk_answer_survey FOREIGN KEY (survey_id)
                                   REFERENCES survey_forms(id) ON DELETE CASCADE,
                               CONSTRAINT fk_answer_question FOREIGN KEY (question_id)
                                   REFERENCES ct_question(id) ON DELETE RESTRICT,
                               CONSTRAINT uq_survey_question UNIQUE (survey_id, question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Migrate existing data
INSERT INTO survey_answer (survey_id, question_id, answer_text)
SELECT id, 'CTA-AI-01.A1', core_problem_summary FROM survey_forms WHERE core_problem_summary IS NOT NULL;

INSERT INTO survey_answer (survey_id, question_id, answer_text)
SELECT id, 'CTA-AI-01.A2', peripheral_education FROM survey_forms WHERE peripheral_education IS NOT NULL;

INSERT INTO survey_answer (survey_id, question_id, answer_text)
SELECT id, 'CTA-AI-01.B1', implicit_assumptions FROM survey_forms WHERE implicit_assumptions IS NOT NULL;

INSERT INTO survey_answer (survey_id, question_id, answer_text)
SELECT id, 'CTA-AI-01.B2', argument_analysis FROM survey_forms WHERE argument_analysis IS NOT NULL;

INSERT INTO survey_answer (survey_id, question_id, answer_text)
SELECT id, 'CTA-AI-01.B3', evidence_vs_opinion FROM survey_forms WHERE evidence_vs_opinion IS NOT NULL;

INSERT INTO survey_answer (survey_id, question_id, answer_text)
SELECT id, 'CTA-AI-01.C1', table_inferences FROM survey_forms WHERE table_inferences IS NOT NULL;

INSERT INTO survey_answer (survey_id, question_id, answer_text)
SELECT id, 'CTA-AI-01.C2', research_design FROM survey_forms WHERE research_design IS NOT NULL;

INSERT INTO survey_answer (survey_id, question_id, answer_text)
SELECT id, 'CTA-AI-01.D1', learning_benefit FROM survey_forms WHERE learning_benefit IS NOT NULL;

INSERT INTO survey_answer (survey_id, question_id, answer_text)
SELECT id, 'CTA-AI-01.D2', ai_usage_reflection FROM survey_forms WHERE ai_usage_reflection IS NOT NULL;

-- 3. Drop old columns (after confirming migration worked)
-- ALTER TABLE survey_forms
-- DROP COLUMN core_problem_summary,
-- DROP COLUMN peripheral_education,
-- DROP COLUMN implicit_assumptions,
-- DROP COLUMN argument_analysis,
-- DROP COLUMN evidence_vs_opinion,
-- DROP COLUMN table_inferences,
-- DROP COLUMN research_design,
-- DROP COLUMN learning_benefit,
-- DROP COLUMN ai_usage_reflection;