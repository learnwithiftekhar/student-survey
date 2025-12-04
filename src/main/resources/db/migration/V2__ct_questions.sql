-- ==========================
-- 1. CT Skills master table
-- ==========================
DROP TABLE IF EXISTS ct_question_skill;
DROP TABLE IF EXISTS ct_question;
DROP TABLE IF EXISTS ct_skill;

CREATE TABLE ct_skill (
                          id   INT AUTO_INCREMENT PRIMARY KEY,
                          code VARCHAR(50)  NOT NULL UNIQUE,  -- e.g. INTERPRETATION
                          name VARCHAR(100) NOT NULL          -- e.g. Interpretation
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO ct_skill (code, name) VALUES
                                      ('INTERPRETATION',  'Interpretation'),
                                      ('ANALYSIS',        'Analysis'),
                                      ('EVALUATION',      'Evaluation'),
                                      ('INFERENCE',       'Inference'),
                                      ('EXPLANATION',     'Explanation'),
                                      ('SELF_REGULATION', 'Self-regulation');

-- ==========================
-- 2. Question table
-- ==========================
CREATE TABLE ct_question (
                             id           VARCHAR(50)  NOT NULL PRIMARY KEY,  -- e.g. CTA-AI-01.A1
                             section      CHAR(1)      NOT NULL,              -- A, B, C, D
                             number       VARCHAR(10)  NOT NULL,              -- A1, B2, etc.
                             title        VARCHAR(255) NULL,
                             text         TEXT         NOT NULL,
                             rubric       TEXT         NOT NULL,
                             max_score    INT          NOT NULL,
                             active       TINYINT(1)   NOT NULL DEFAULT 1,
                             created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================
-- 3. Question–Skill mapping
-- ==========================
CREATE TABLE ct_question_skill (
                                   question_id VARCHAR(50) NOT NULL,
                                   skill_id    INT         NOT NULL,
                                   PRIMARY KEY (question_id, skill_id),
                                   CONSTRAINT fk_qs_question FOREIGN KEY (question_id)
                                       REFERENCES ct_question (id)
                                       ON DELETE CASCADE,
                                   CONSTRAINT fk_qs_skill FOREIGN KEY (skill_id)
                                       REFERENCES ct_skill (id)
                                       ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- 4. Insert questions (9 questions, total 60 marks)
-- ========================================

-- SECTION A – Interpretation & Clarification
-- Q A1 (CTA-AI-01.A1 – Interpretation, 5 marks)
INSERT INTO ct_question (id, section, number, title, text, rubric, max_score)
VALUES (
           'CTA-AI-01.A1',
           'A',
           'A1',
           'Core problem summary',
           'In your own words, summarise the core problem presented in the case in no more than 120 words. '
               'Your summary should clearly show: '
               'a) The role of AI in students’ academic performance, and '
               'b) The concern about students’ critical thinking.',
           'Marking guideline (0–5):\n'
               '0 = Off topic or blank.\n'
               '1–2 = Mentions AI and grades, but misses the paradox (performance vs CT).\n'
               '3–4 = Captures both grade improvement and CT concern, but with minor gaps.\n'
               '5 = Concise, accurate summary that clearly presents the tension between performance and critical thinking in peripheral context.',
           5
       );

-- Q A2 (CTA-AI-01.A2 – Clarifying concepts, 5 marks)
INSERT INTO ct_question (id, section, number, title, text, rubric, max_score)
VALUES (
           'CTA-AI-01.A2',
           'A',
           'A2',
           'Peripheral higher education',
           'Explain what is meant by “peripheral higher education” in this context. '
               'How might “peripherality” shape both the adoption and the impact of AI tools in the sociology classroom?',
           'Marking guideline (0–5):\n'
               '0 = No attempt / irrelevant.\n'
               '1–2 = Vague: merely says “rural” or “less developed” without linking to AI.\n'
               '3–4 = Shows understanding of peripherality (e.g., resource constraints, centre–periphery dynamics) and starts to link with AI adoption.\n'
               '5 = Clearly connects peripherality (e.g., infrastructure, symbolic status, digital divide) to both how AI is adopted and how its impact on learning/CT might differ from central universities.',
           5
       );

-- SECTION B – Analysis of Assumptions & Arguments
-- Q B1 (CTA-AI-01.B1 – Recognition of assumptions, 6 marks)
INSERT INTO ct_question (id, section, number, title, text, rubric, max_score)
VALUES (
           'CTA-AI-01.B1',
           'B',
           'B1',
           'Implicit assumptions about “smart learning”',
           'Identify three implicit assumptions made by university management when they interpret higher grades as evidence of “smart learning” success. '
               'For each assumption, explain why it may be problematic from a sociological perspective. (You may phrase as bullet points.)',
           'Marking guideline (0–6):\n'
               '0–1 = Lists points but they are not clearly assumptions.\n'
               '2–3 = Identifies 1–2 reasonable assumptions but weak explanation.\n'
               '4–5 = Identifies 3 plausible assumptions with sociological justification '
               '(e.g., grade = learning, AI use = modernisation, periphery = must catch up).\n'
               '6 = Three clearly-stated assumptions, each critically explained (e.g., grade inflation, performativity of metrics, techno-solutionism, ignoring hidden curriculum).',
           6
       );

-- Q B2 (CTA-AI-01.B2 – Argument analysis, 7 marks)
INSERT INTO ct_question (id, section, number, title, text, rubric, max_score)
VALUES (
           'CTA-AI-01.B2',
           'B',
           'B2',
           'AI, democratisation and “luxury” of CT',
           'A colleague argues:\n'
               '“AI is democratising knowledge. For peripheral universities, anything that raises grades is good. '
               'Critical thinking is a luxury; our students first need high marks to compete in the job market.”\n'
               'a) Reconstruct this argument in standard form (premises + conclusion).\n'
               'b) Critically analyse the strengths and weaknesses of this argument using at least two sociological concepts '
               '(e.g., meritocracy, symbolic capital, neoliberalism, digital divide, hidden curriculum).',
           'Marking guideline (0–7):\n'
               '0–2 = Very superficial; no clear reconstruction or concept use.\n'
               '3–4 = Partial reconstruction; identifies at least one sociological concept but limited critique.\n'
               '5–6 = Clear premise–conclusion structure and balanced evaluation using two relevant concepts.\n'
               '7 = Accurate reconstruction + nuanced critique showing how the argument prioritises credentialism over genuine learning, '
               'and how power/inequality are embedded.',
           7
       );

-- Q B3 (CTA-AI-01.B3 – Distinguishing evidence & opinion, 7 marks)
INSERT INTO ct_question (id, section, number, title, text, rubric, max_score)
VALUES (
           'CTA-AI-01.B3',
           'B',
           'B3',
           'Evidence vs opinion in the case',
           'From the case, list three pieces of evidence and three opinions / interpretations (either by teachers, students, or management). '
               'Then explain how confusing evidence with opinion can mislead decision-making about AI policy in the department.',
           'Marking guideline (0–7):\n'
               '0–2 = Lists random statements without distinguishing clearly.\n'
               '3–4 = Distinguishes some evidence vs opinion but with minor misclassification; weak explanation.\n'
               '5–6 = Correctly identifies 3+3 items and reasonably explains consequences for policy.\n'
               '7 = Very clear classification with strong explanation of why evidence/opinion confusion matters '
               '(e.g., legitimising policy on anecdote, confirmation bias).',
           7
       );

-- SECTION C – Inference, Causal Reasoning & Alternative Explanations
-- Q C1 (CTA-AI-01.C1 – Inference & correlation, 10 marks)
INSERT INTO ct_question (id, section, number, title, text, rubric, max_score)
VALUES (
           'CTA-AI-01.C1',
           'C',
           'C1',
           'Inferences from AI–performance–CT pattern',
           'Based on Table 1, what inferences can you reasonably draw about the relationship between AI use, academic performance, '
               'and critical thinking (as proxied by participation and confidence)? '
               'Your answer should:\n'
               'a) Identify at least two possible interpretations of the pattern (e.g., AI helps writing but weakens oral argument), and\n'
               'b) Discuss at least one alternative explanation that does not blame AI alone.',
           'Marking guideline (0–10):\n'
               '0–3 = Describes numbers but does not interpret critically.\n'
               '4–6 = Offers some plausible inferences but tends to assume simple cause-effect (“AI → bad CT”).\n'
               '7–8 = Gives multiple plausible interpretations, recognises that correlation ≠ causation, brings in contextual factors in peripheral HE.\n'
               '9–10 = Sophisticated reasoning; balances AI effect with structural explanations (teaching style, assessment formats, language issues, etc.).',
           10
       );

-- Q C2 (CTA-AI-01.C2 – Confounding & research design, 10 marks)
INSERT INTO ct_question (id, section, number, title, text, rubric, max_score)
VALUES (
           'CTA-AI-01.C2',
           'C',
           'C2',
           'Study design: AI and CT in sociology',
           'Suppose you are asked to design a small empirical study within the department to test whether AI use is actually diminishing critical thinking among sociology students.\n'
               'Briefly outline:\n'
               'a) Your research question and hypothesis.\n'
               'b) The key variables (at least one independent and one dependent variable; mention possible confounders).\n'
               'c) Your basic design (e.g., comparison of AI users vs non-users, pre-test/post-test, mixed methods).\n'
               'd) One ethical concern related to AI use in this research.',
           'Marking guideline (0–10):\n'
               '0–3 = Very vague; no clear variables or design.\n'
               '4–6 = Has a sensible question and basic design but limited mention of confounders or ethics.\n'
               '7–8 = Clear hypothesis, variables, confounders (e.g., prior CGPA, language proficiency, access to devices), and a realistic design.\n'
               '9–10 = All of the above plus shows awareness of researcher bias, student vulnerability, or fairness issues in experimental conditions.',
           10
       );

-- SECTION D – Self-Regulation & Meta-Reflection
-- Q D1 (CTA-AI-01.D1 – Reflective judgment, 5 marks)
INSERT INTO ct_question (id, section, number, title, text, rubric, max_score)
VALUES (
           'CTA-AI-01.D1',
           'D',
           'D1',
           'Your own learning and AI',
           'Do you think your own learning in sociology would benefit more from:\n'
               'a) Limited or no AI use, or\n'
               'b) Guided / critical use of AI tools?\n'
               'Choose one and justify your position in about 120–150 words, drawing on at least one sociological theory or concept '
               '(e.g., habitus, disciplinary power, rationalisation, McDonaldization, etc.).',
           'Marking guideline (0–5):\n'
               '0–1 = Pure opinion with no link to sociological thinking.\n'
               '2–3 = Some justification, limited theoretical framing.\n'
               '4 = Clear position with a relevant sociological concept.\n'
               '5 = Strongly reasoned, reflexive, and theoretically grounded justification.',
           5
       );

-- Q D2 (CTA-AI-01.D2 – Meta-assessment of AI, 5 marks)
INSERT INTO ct_question (id, section, number, title, text, rubric, max_score)
VALUES (
           'CTA-AI-01.D2',
           'D',
           'D2',
           'Meta-reflection on AI use during test',
           '(For AI-using group):\n'
               'Briefly describe how you used AI during this test (or why you chose not to). Evaluate:\n'
               'a) In which question(s) did AI genuinely help your critical thinking, and\n'
               'b) In which question(s) do you think AI may have weakened your independent thinking?\n'
               'Be as honest and specific as possible.',
           'Marking guideline (0–5):\n'
               '0–1 = Very superficial (“AI helped everywhere,” no specifics).\n'
               '2–3 = Identifies some helpful/unhelpful aspects but shallow.\n'
               '4 = Concrete examples of where AI helped or hindered thinking.\n'
               '5 = Deeply reflective comparison between AI-generated ideas and own thinking; awareness of over-reliance or blind spots.',
           5
       );

-- ========================================
-- 5. Map questions to skills
-- ========================================

-- Helper: get skill IDs
-- (Run SELECT * FROM ct_skill; to see IDs if needed)
-- For now we assume auto IDs in insert order:
-- 1 INTERPRETATION, 2 ANALYSIS, 3 EVALUATION, 4 INFERENCE, 5 EXPLANATION, 6 SELF_REGULATION

-- CTA-AI-01.A1 – Interpretation only
INSERT INTO ct_question_skill (question_id, skill_id) VALUES
    ('CTA-AI-01.A1', 1);

-- CTA-AI-01.A2 – Interpretation, Analysis
INSERT INTO ct_question_skill (question_id, skill_id) VALUES
                                                          ('CTA-AI-01.A2', 1),
                                                          ('CTA-AI-01.A2', 2);

-- CTA-AI-01.B1 – Recognition of assumptions: Analysis, Evaluation, Interpretation
INSERT INTO ct_question_skill (question_id, skill_id) VALUES
                                                          ('CTA-AI-01.B1', 2),
                                                          ('CTA-AI-01.B1', 3),
                                                          ('CTA-AI-01.B1', 1);

-- CTA-AI-01.B2 – Argument analysis: Analysis, Evaluation, Interpretation
INSERT INTO ct_question_skill (question_id, skill_id) VALUES
                                                          ('CTA-AI-01.B2', 2),
                                                          ('CTA-AI-01.B2', 3),
                                                          ('CTA-AI-01.B2', 1);

-- CTA-AI-01.B3 – Evidence vs opinion: Interpretation, Evaluation, Inference
INSERT INTO ct_question_skill (question_id, skill_id) VALUES
                                                          ('CTA-AI-01.B3', 1),
                                                          ('CTA-AI-01.B3', 3),
                                                          ('CTA-AI-01.B3', 4);

-- CTA-AI-01.C1 – Inference, Analysis, Evaluation
INSERT INTO ct_question_skill (question_id, skill_id) VALUES
                                                          ('CTA-AI-01.C1', 4),
                                                          ('CTA-AI-01.C1', 2),
                                                          ('CTA-AI-01.C1', 3);

-- CTA-AI-01.C2 – Inference, Explanation, Self-regulation
INSERT INTO ct_question_skill (question_id, skill_id) VALUES
                                                          ('CTA-AI-01.C2', 4),
                                                          ('CTA-AI-01.C2', 5),
                                                          ('CTA-AI-01.C2', 6);

-- CTA-AI-01.D1 – Explanation, Evaluation, Self-regulation
INSERT INTO ct_question_skill (question_id, skill_id) VALUES
                                                          ('CTA-AI-01.D1', 5),
                                                          ('CTA-AI-01.D1', 3),
                                                          ('CTA-AI-01.D1', 6);

-- CTA-AI-01.D2 – Self-regulation, Evaluation
INSERT INTO ct_question_skill (question_id, skill_id) VALUES
                                                          ('CTA-AI-01.D2', 6),
                                                          ('CTA-AI-01.D2', 3);
