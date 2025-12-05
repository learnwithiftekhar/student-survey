INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at)
SELECT
    id,
    'CTA-AI-01.A1',
    core_problem_summary,
    created_at
FROM survey_forms
WHERE core_problem_summary IS NOT NULL AND core_problem_summary != '';

INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at)
SELECT
    id,
    'CTA-AI-01.A2',
    peripheral_education,
    created_at
FROM survey_forms
WHERE peripheral_education IS NOT NULL AND peripheral_education != '';

INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at)
SELECT
    id,
    'CTA-AI-01.B1',
    implicit_assumptions,
    created_at
FROM survey_forms
WHERE implicit_assumptions IS NOT NULL AND implicit_assumptions != '';

INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at)
SELECT
    id,
    'CTA-AI-01.B2',
    argument_analysis,
    created_at
FROM survey_forms
WHERE argument_analysis IS NOT NULL AND argument_analysis != '';

INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at)
SELECT
    id,
    'CTA-AI-01.B3',
    evidence_vs_opinion,
    created_at
FROM survey_forms
WHERE evidence_vs_opinion IS NOT NULL AND evidence_vs_opinion != '';

INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at)
SELECT
    id,
    'CTA-AI-01.C1',
    table_inferences,
    created_at
FROM survey_forms
WHERE table_inferences IS NOT NULL AND table_inferences != '';

INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at)
SELECT
    id,
    'CTA-AI-01.C2',
    research_design,
    created_at
FROM survey_forms
WHERE research_design IS NOT NULL AND research_design != '';

INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at)
SELECT
    id,
    'CTA-AI-01.D1',
    learning_benefit,
    created_at
FROM survey_forms
WHERE learning_benefit IS NOT NULL AND learning_benefit != '';

INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at)
SELECT
    id,
    'CTA-AI-01.D2',
    ai_usage_reflection,
    created_at
FROM survey_forms
WHERE ai_usage_reflection IS NOT NULL AND ai_usage_reflection != '';