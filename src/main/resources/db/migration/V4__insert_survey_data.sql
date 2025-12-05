-- Seed data for survey_forms and related collection tables
-- Note: This migration assumes V1__initial.sql has created all necessary tables.

-- Insert sample surveys (3 rows)
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester, location,
    devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai, core_problem_summary, peripheral_education,
    implicit_assumptions, argument_analysis, evidence_vs_opinion,
    table_inferences, research_design,
    learning_benefit, ai_usage_reflection,
    submitted_by_ip, is_completed
) VALUES
-- 1) Bachelor student, uses AI tools
('Alice Johnson', 'STU-B-001', 21, 'Bachelor\'s', '131', 'Urban',
 NULL, 'Yes', '1-3 hours', 'Yes', 'Yes',
 'Daily',
 'From online courses and peers discussing AI tools in study groups.',
 'Students struggle to balance quick AI help with deep understanding of concepts.',
 'Formal classes rarely teach critical use of AI; most learning is self-directed.',
 'AI outputs may reflect hidden biases and training data limitations.',
 'While AI accelerates answers, the argument that it harms learning ignores guided usage models.',
 'Evidence from controlled studies vs. anecdotal opinions on social media are often conflated.',
 'Increased speed may correlate with shallow understanding unless students are prompted to explain steps.',
 'A mixed-method study comparing AI-assisted vs. traditional problem solving with reflection prompts.',
 'Helps generate practice questions and explanations; useful when followed by self-explanation.',
 'I set rules for when to consult AI and always restate the solution in my own words.',
 '127.0.0.1', TRUE),

-- 2) Master student, familiar but does not use AI tools
('Brian Kim', 'STU-M-002', 24, 'Master\'s', '212', 'Rural',
 'Feature phone', 'No', '< 1 hour', 'Yes', 'No',
 'Occasionally',
 'University seminars and reading tech news.',
 'Limited connectivity reduces access to online resources and tools.',
 'Workshops provide overview but limited hands-on time to practice responsibly.',
 'Assumes all AI requires constant internet and high-end devices.',
 'Non-usage may protect against overreliance; counterpoint: missed accessibility support.',
 'Distinguish case studies with measured outcomes from opinion pieces.',
 'Non-usage can confound with other factors such as prior preparation.',
 'Quasi-experimental comparison controlling for baseline knowledge and access.',
 'Reflection journals reveal where AI could help without replacing reasoning.',
 'I focus on planning and outlining manually; I note where AI might slot in later.',
 NULL, FALSE),

-- 3) Bachelor student, limited use
('Chitra Rao', 'STU-B-003', 19, 'Bachelor\'s', '112', 'Urban',
 NULL, 'Yes', '4-8 hours', 'Yes', 'Yes',
 'Weekly',
 'YouTube channels demonstrating study workflows with AI.',
 'Time management and confidence in debugging code exercises.',
 'Clubs discuss ethics but rarely emphasize verification strategies.',
 'Assumes AI always improves productivity; ignores learning curve and verification time.',
 'Argues for selective use with checkpoints; counters blanket bans in some classes.',
 'Surveys claiming improved grades often lack control groups.',
 'Improved drafting speed may trade off with editing thoroughness.',
 'AB test lab assignments with rubric-based grading and think-aloud protocols.',
 'AI helps brainstorm approaches; I still implement from scratch and compare.',
 'I document when I consulted AI and how I validated outputs.',
 '192.168.1.10', TRUE);

-- Devices for Alice (phone, laptop)
INSERT INTO survey_devices (survey_id, device)
SELECT id, 'Mobile devices (Smartphone, Smartwatches)'
FROM survey_forms WHERE student_id = 'STU-B-001';

INSERT INTO survey_devices (survey_id, device)
SELECT id, 'Laptop' FROM survey_forms WHERE student_id = 'STU-B-001';

-- Devices for Brian (desktop)
INSERT INTO survey_devices (survey_id, device)
SELECT id, 'Desktop' FROM survey_forms WHERE student_id = 'STU-M-002';

-- Devices for Chitra (phone, tablet, laptop)
INSERT INTO survey_devices (survey_id, device)
SELECT id, 'Mobile devices (Smartphone, Smartwatches)'
FROM survey_forms WHERE student_id = 'STU-B-003';

INSERT INTO survey_devices (survey_id, device)
SELECT id, 'Tablets' FROM survey_forms WHERE student_id = 'STU-B-003';

INSERT INTO survey_devices (survey_id, device)
SELECT id, 'Laptop' FROM survey_forms WHERE student_id = 'STU-B-003';

-- AI tools for Alice (study + writing + research)
INSERT INTO survey_study_tools (survey_id, tool)
SELECT id, 'ChatGPT' FROM survey_forms WHERE student_id = 'STU-B-001';

INSERT INTO survey_study_tools (survey_id, tool)
SELECT id, 'Khanmigo (Khan Academy)' FROM survey_forms WHERE student_id = 'STU-B-001';

INSERT INTO survey_writing_tools (survey_id, tool)
SELECT id, 'Grammarly' FROM survey_forms WHERE student_id = 'STU-B-001';

INSERT INTO survey_research_tools (survey_id, tool)
SELECT id, 'Perplexity AI' FROM survey_forms WHERE student_id = 'STU-B-001';

-- AI tools for Chitra (study + note)
INSERT INTO survey_study_tools (survey_id, tool)
SELECT id, 'Wolfram Alpha' FROM survey_forms WHERE student_id = 'STU-B-003';

INSERT INTO survey_note_tools (survey_id, tool)
SELECT id, 'Notion AI' FROM survey_forms WHERE student_id = 'STU-B-003';

-- No tools for Brian since use_ai_tools = 'No'
