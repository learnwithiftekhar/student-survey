-- V8__insert_20_sample_surveys.sql
-- Insert 20 sample survey submissions with varied data

-- Sample Survey 1
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester, location,
    devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai,
    submitted_by_ip, is_completed, created_at, updated_at
) VALUES (
             'Fatima Rahman', 'STU-2024-001', 20, 'Bachelor''s', '131', 'Urban',
             NULL, 'Yes', '4-8 hours', 'Yes', 'Yes',
             'Daily', 'I learned about AI tools from my classmates and YouTube tutorials. They helped me understand how to use ChatGPT for studying.',
             '192.168.1.101', TRUE, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 15 DAY
         );

SET @survey1_id = LAST_INSERT_ID();

INSERT INTO survey_devices (survey_id, device) VALUES
                                                   (@survey1_id, 'Mobile devices (Smartphone, Smartwatches)'),
                                                   (@survey1_id, 'Laptop');

INSERT INTO survey_study_tools (survey_id, tool) VALUES
                                                     (@survey1_id, 'ChatGPT'),
                                                     (@survey1_id, 'Perplexity AI');

INSERT INTO survey_writing_tools (survey_id, tool) VALUES
    (@survey1_id, 'Grammarly');

-- Insert answers for Survey 1
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey1_id, 'CTA-AI-01.A1', 'The case presents a paradox where AI tools help students achieve higher grades but simultaneously weaken their critical thinking abilities. Students can write better assignments with AI assistance, but they struggle to explain their own arguments in discussions.', NOW() - INTERVAL 15 DAY),
                                                                                   (@survey1_id, 'CTA-AI-01.A2', 'Peripheral higher education refers to universities that are geographically and institutionally distant from major academic centers. These institutions often face resource constraints and may adopt AI tools as a way to modernize quickly, but the impact may differ from central universities due to limited infrastructure and teaching capacity.', NOW() - INTERVAL 15 DAY),
                                                                                   (@survey1_id, 'CTA-AI-01.B1', 'Three assumptions: 1) Higher grades equal better learning - problematic because grades may reflect AI output rather than student understanding. 2) AI use represents modernization - ignores how technology can reinforce existing inequalities. 3) Students need to catch up with global standards - assumes Western models are universally applicable.', NOW() - INTERVAL 15 DAY),
                                                                                   (@survey1_id, 'CTA-AI-01.B2', 'Premise 1: AI democratizes access to knowledge. Premise 2: Peripheral universities need higher grades for job market competition. Conclusion: Critical thinking is a luxury. Critique: This argument uses meritocracy and neoliberal logic to justify surface learning. It ignores how credentialism without genuine skills creates long-term disadvantages.', NOW() - INTERVAL 15 DAY),
                                                                                   (@survey1_id, 'CTA-AI-01.B3', 'Evidence: Grade improvement from B- to A-, student reports of writing faster. Opinions: Teachers believe students rely too much on AI, management views higher grades as success. Confusion leads to policy based on anecdotal impressions rather than systematic data.', NOW() - INTERVAL 15 DAY),
                                                                                   (@survey1_id, 'CTA-AI-01.C1', 'The data suggests AI helps with written assignments but students struggle with oral participation and confidence. Alternative explanation: Traditional teaching methods emphasize memorization over discussion skills, so the problem may preexist AI use.', NOW() - INTERVAL 15 DAY),
                                                                                   (@survey1_id, 'CTA-AI-01.C2', 'Research question: Does AI use reduce critical thinking skills? Hypothesis: Students using AI will score lower on CT tests than non-users. Variables: Independent - AI usage frequency; Dependent - CT test scores; Confounders - prior academic performance, language proficiency. Design: Pre-test/post-test with control group. Ethics: Ensure students are not disadvantaged by participation.', NOW() - INTERVAL 15 DAY),
                                                                                   (@survey1_id, 'CTA-AI-01.D1', 'I believe guided critical use of AI would benefit my learning more. Using Bourdieu concept of habitus, I can develop better academic practices by learning to evaluate AI outputs critically rather than avoiding technology entirely. The key is developing reflexivity about when and how to use AI.', NOW() - INTERVAL 15 DAY),
                                                                                   (@survey1_id, 'CTA-AI-01.D2', 'I used ChatGPT to help brainstorm ideas for questions about assumptions and arguments. It genuinely helped me think of different sociological concepts to apply. However, for the research design question, I relied too heavily on AI structure and did not think through the details myself.', NOW() - INTERVAL 15 DAY);

-- Sample Survey 2
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester, location,
    devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai,
    submitted_by_ip, is_completed, created_at, updated_at
) VALUES (
             'Karim Ahmed', 'STU-2024-002', 22, 'Bachelor''s', '141', 'Rural',
             NULL, 'No', '1-3 hours', 'Yes', 'No',
             'Rarely', 'I heard about AI tools in university seminars but I do not have reliable internet access to use them regularly.',
             '192.168.1.102', TRUE, NOW() - INTERVAL 14 DAY, NOW() - INTERVAL 14 DAY
         );

SET @survey2_id = LAST_INSERT_ID();

INSERT INTO survey_devices (survey_id, device) VALUES
    (@survey2_id, 'Mobile devices (Smartphone, Smartwatches)');

-- Insert answers for Survey 2
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey2_id, 'CTA-AI-01.A1', 'The problem is that students are getting better grades with AI help but they cannot explain their work properly when asked. This creates a gap between written performance and actual understanding.', NOW() - INTERVAL 14 DAY),
                                                                                   (@survey2_id, 'CTA-AI-01.A2', 'Peripheral education means universities that are not in the main cities. These universities have less resources so they might use AI differently than big universities.', NOW() - INTERVAL 14 DAY),
                                                                                   (@survey2_id, 'CTA-AI-01.B1', 'Management assumes grades show learning. This is wrong because students might just be copying AI. They also assume AI is always good for education but it might harm independent thinking.', NOW() - INTERVAL 14 DAY),
                                                                                   (@survey2_id, 'CTA-AI-01.B2', 'The argument says AI helps poor students compete. The premise is that grades matter most. But this ignores that real learning is more important than just grades for getting jobs.', NOW() - INTERVAL 14 DAY),
                                                                                   (@survey2_id, 'CTA-AI-01.B3', 'Evidence is the grade increase and student surveys. Opinions are teacher complaints and management celebration. Mixing them up means decisions are based on feelings not facts.', NOW() - INTERVAL 14 DAY),
                                                                                   (@survey2_id, 'CTA-AI-01.C1', 'Students write better but talk worse. This could mean AI helps writing but not thinking. Or maybe students were always bad at speaking and we are just noticing now.', NOW() - INTERVAL 14 DAY),
                                                                                   (@survey2_id, 'CTA-AI-01.C2', 'Question: Does AI hurt critical thinking? Compare students who use AI with those who do not. Measure their test scores and class participation. Need to control for their previous grades.', NOW() - INTERVAL 14 DAY),
                                                                                   (@survey2_id, 'CTA-AI-01.D1', 'No AI use is better for me because I need to develop my own thinking skills first. If I use AI too early, I will become dependent on it and never learn to think independently.', NOW() - INTERVAL 14 DAY),
                                                                                   (@survey2_id, 'CTA-AI-01.D2', 'I did not use AI during this test because I do not have internet access. If I had used it, it might have helped me write longer answers but I am not sure if my thinking would be better.', NOW() - INTERVAL 14 DAY);

-- Sample Survey 3
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester, location,
    devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai,
    submitted_by_ip, is_completed, created_at, updated_at
) VALUES (
             'Nadia Islam', 'STU-2024-003', 21, 'Bachelor''s', '131', 'Urban',
             NULL, 'Yes', '> 8 hours', 'Yes', 'Yes',
             'Daily', 'I discovered AI tools through social media, particularly TikTok and Instagram reels showing how students use ChatGPT for homework.',
             '192.168.1.103', TRUE, NOW() - INTERVAL 13 DAY, NOW() - INTERVAL 13 DAY
         );

SET @survey3_id = LAST_INSERT_ID();

INSERT INTO survey_devices (survey_id, device) VALUES
                                                   (@survey3_id, 'Mobile devices (Smartphone, Smartwatches)'),
                                                   (@survey3_id, 'Laptop'),
                                                   (@survey3_id, 'Tablets');

INSERT INTO survey_study_tools (survey_id, tool) VALUES
                                                     (@survey3_id, 'ChatGPT'),
                                                     (@survey3_id, 'Khanmigo (Khan Academy)'),
                                                     (@survey3_id, 'Perplexity AI');

INSERT INTO survey_writing_tools (survey_id, tool) VALUES
                                                       (@survey3_id, 'Grammarly'),
                                                       (@survey3_id, 'Quillbot');

INSERT INTO survey_note_tools (survey_id, tool) VALUES
    (@survey3_id, 'Notion AI');

-- Insert answers for Survey 3
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey3_id, 'CTA-AI-01.A1', 'Students use AI and grades go up but critical thinking might be going down. AI helps with assignments but students cannot defend their arguments when questioned. Management thinks this is success but teachers are worried.', NOW() - INTERVAL 13 DAY),
                                                                                   (@survey3_id, 'CTA-AI-01.A2', 'Peripheral universities are those outside major cities with limited resources. They adopt AI hoping to modernize but face challenges like poor internet and untrained teachers. The impact of AI might be different here compared to elite universities because of these constraints.', NOW() - INTERVAL 13 DAY),
                                                                                   (@survey3_id, 'CTA-AI-01.B1', 'Assumption 1: Grades equal learning - problematic because AI can inflate grades without real understanding. Assumption 2: Technology is neutral - ignores how AI reflects existing biases. Assumption 3: Quick fixes work - overlooks need for pedagogical change.', NOW() - INTERVAL 13 DAY),
                                                                                   (@survey3_id, 'CTA-AI-01.B2', 'Premise: AI democratizes knowledge. Premise: Grades matter for jobs. Conclusion: CT is luxury. Weakness: Uses neoliberal logic prioritizing credentials over education. Strength: Acknowledges real pressure students face in job market. Sociological critique: Reinforces symbolic violence through credentialism.', NOW() - INTERVAL 13 DAY),
                                                                                   (@survey3_id, 'CTA-AI-01.B3', 'Evidence: Statistical grade improvement, student time reports. Opinions: Teacher beliefs about dependency, management interpretation of success. Confusion leads to policy based on selective data that confirms existing biases rather than comprehensive analysis.', NOW() - INTERVAL 13 DAY),
                                                                                   (@survey3_id, 'CTA-AI-01.C1', 'Pattern shows written work improvement but oral work decline. Could mean AI helps writing but not understanding, or that assessment methods are mismatched. Alternative: Students had weak speaking skills before AI; AI just makes the contrast more visible.', NOW() - INTERVAL 13 DAY),
                                                                                   (@survey3_id, 'CTA-AI-01.C2', 'RQ: Does AI diminish CT in sociology students? Hypothesis: Higher AI use correlates with lower CT scores. IV: AI usage frequency. DV: CT test performance. Confounders: Prior GPA, English proficiency, socioeconomic status. Design: Mixed methods with CT test and interviews. Ethics: Informed consent, no harm to academic standing.', NOW() - INTERVAL 13 DAY),
                                                                                   (@survey3_id, 'CTA-AI-01.D1', 'Guided critical use would benefit me more. Drawing on Foucault concept of disciplinary power, learning to use AI critically helps me resist its controlling effects while still benefiting from its tools. Complete avoidance leaves me unprepared for AI-saturated future workplaces and academic environments.', NOW() - INTERVAL 13 DAY),
                                                                                   (@survey3_id, 'CTA-AI-01.D2', 'I used ChatGPT extensively for all sections. It helped me structure arguments and recall sociological concepts quickly. However, I notice I relied on AI-generated examples rather than thinking of my own, which may have weakened the originality and personal voice in my answers.', NOW() - INTERVAL 13 DAY);

-- Sample Survey 4
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester, location,
    devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai,
    submitted_by_ip, is_completed, created_at, updated_at
) VALUES (
             'Rashid Hasan', 'STU-2024-004', 23, 'Master''s', '211', 'Urban',
             NULL, 'Yes', '4-8 hours', 'Yes', 'Yes',
             'Weekly', 'I learned about AI tools from academic conferences and reading research papers about educational technology.',
             '192.168.1.104', TRUE, NOW() - INTERVAL 12 DAY, NOW() - INTERVAL 12 DAY
         );

SET @survey4_id = LAST_INSERT_ID();

INSERT INTO survey_devices (survey_id, device) VALUES
                                                   (@survey4_id, 'Laptop'),
                                                   (@survey4_id, 'Desktop');

INSERT INTO survey_study_tools (survey_id, tool) VALUES
                                                     (@survey4_id, 'ChatGPT'),
                                                     (@survey4_id, 'Perplexity AI'),
                                                     (@survey4_id, 'Elicit');

INSERT INTO survey_writing_tools (survey_id, tool) VALUES
                                                       (@survey4_id, 'Grammarly'),
                                                       (@survey4_id, 'Jenni AI');

INSERT INTO survey_research_tools (survey_id, tool) VALUES
                                                        (@survey4_id, 'Elicit Research'),
                                                        (@survey4_id, 'Zotero AI');

-- Insert answers for Survey 4
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey4_id, 'CTA-AI-01.A1', 'The case reveals a tension between performative success measured by grades and substantive learning measured by critical thinking capacity. AI tools enable students to produce higher-quality written outputs while simultaneously potentially atrophying their independent analytical capabilities, creating a paradoxical situation where metrics of success diverge from actual educational outcomes.', NOW() - INTERVAL 12 DAY),
                                                                                   (@survey4_id, 'CTA-AI-01.A2', 'Peripheral higher education refers to institutions operating at geographic, economic, and symbolic distance from prestigious academic centers. This peripherality shapes AI adoption through resource scarcity, infrastructure limitations, and aspirational mimicry of central institutions. The impact differs because contextual factors like digital literacy, pedagogical training, and institutional support systems mediate how AI tools are integrated and experienced.', NOW() - INTERVAL 12 DAY),
                                                                                   (@survey4_id, 'CTA-AI-01.B1', 'First, management assumes grades transparently represent learning, ignoring the black box of assessment and the possibility of grade inflation through AI assistance. Second, they assume technological adoption equals modernization, reflecting techno-solutionism that overlooks pedagogical transformation. Third, they assume peripheral universities must emulate central institutions, perpetuating colonial knowledge hierarchies rather than developing contextually appropriate approaches.', NOW() - INTERVAL 12 DAY),
                                                                                   (@survey4_id, 'CTA-AI-01.B2', 'Reconstructed argument: P1: AI democratizes knowledge access. P2: Peripheral students need competitive credentials. P3: Job markets prioritize grades over skills. C: Critical thinking is a dispensable luxury. Strengths: Acknowledges structural inequality and credential inflation. Weaknesses: Reproduces neoliberal logic that treats education as human capital investment rather than public good. Uses meritocratic rhetoric while ignoring how credentialism reinforces class reproduction. Bourdieu concept of cultural capital reveals how this argument naturalizes symbolic violence.', NOW() - INTERVAL 12 DAY),
                                                                                   (@survey4_id, 'CTA-AI-01.B3', 'Evidence: Quantitative grade improvement, time efficiency reports, participation decline data. Opinions: Teacher attributions about causality, management interpretations of success, student self-assessments of dependency. Confusing these leads to confirmation bias, where decision-makers select data that supports preferred conclusions while dismissing contradictory evidence. This results in policy legitimized by selective empiricism rather than comprehensive evaluation.', NOW() - INTERVAL 12 DAY),
                                                                                   (@survey4_id, 'CTA-AI-01.C1', 'Table 1 suggests correlation between AI use and divergent performance across modalities - written work improves while oral participation declines. First interpretation: AI scaffolds writing but does not develop underlying conceptual understanding. Second interpretation: Assessment types measure different cognitive processes, and AI selectively enhances surface production over deep processing. Alternative explanation: Pedagogical emphasis on written assignments over discussion predates AI, which merely reveals rather than causes this imbalance. Hidden curriculum factors and language anxiety may also contribute independently of AI use.', NOW() - INTERVAL 12 DAY),
                                                                                   (@survey4_id, 'CTA-AI-01.C2', 'Research question: To what extent does AI tool usage mediate the relationship between study time and critical thinking skills among sociology students? Hypothesis: Higher AI usage frequency will correlate with lower gains in CT skills relative to study time invested. Independent variable: AI usage patterns (frequency, type, purpose). Dependent variable: CT skills measured through standardized assessment and argumentative essay analysis. Confounders: Prior academic achievement, socioeconomic status, language proficiency, metacognitive awareness. Design: Longitudinal mixed methods study tracking cohort over one semester with pre/post CT testing, learning analytics, and qualitative interviews. Control group receives traditional instruction. Ethical concern: Potential stigmatization of AI users or creation of educational inequality through differential access in research design.', NOW() - INTERVAL 12 DAY),
                                                                                   (@survey4_id, 'CTA-AI-01.D1', 'Guided critical use would benefit my learning more substantively. Drawing on Weber rationalization thesis and Ritzer McDonaldization concept, uncritical AI adoption risks epistemic deskilling through routinized efficiency. However, complete abstinence ignores AI embedded reality of contemporary knowledge work. Critical pedagogy approach developing metacognitive awareness about AI affordances and constraints builds adaptive expertise. This aligns with Freire concept of conscientization - developing critical consciousness about technological mediation of learning rather than either techno-utopianism or Luddite rejection.', NOW() - INTERVAL 12 DAY),
                                                                                   (@survey4_id, 'CTA-AI-01.D2', 'I used AI strategically for literature review synthesis and theoretical framework recall in questions requiring sociological concepts. This genuinely enhanced my analytical capacity by offloading memory work to focus on conceptual integration. However, for questions requiring original inference from data, I notice AI-generated interpretations may have constrained my imaginative reasoning by anchoring to its initial suggestions. The challenge lies in using AI as thinking partner rather than thinking substitute - maintaining agentive direction while leveraging computational assistance.', NOW() - INTERVAL 12 DAY);

-- Sample Survey 5
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester, location,
    devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai,
    submitted_by_ip, is_completed, created_at, updated_at
) VALUES (
             'Ayesha Begum', 'STU-2024-005', 19, 'Bachelor''s', '112', 'Rural',
             'Basic feature phone', 'No', '< 1 hour', 'No', 'No',
             'Never', 'I have not learned much about AI tools because we do not have proper internet in my village and I only use basic phone.',
             '192.168.1.105', TRUE, NOW() - INTERVAL 11 DAY, NOW() - INTERVAL 11 DAY
         );

SET @survey5_id = LAST_INSERT_ID();

INSERT INTO survey_devices (survey_id, device) VALUES
    (@survey5_id, 'Mobile devices (Smartphone, Smartwatches)');

-- Insert answers for Survey 5
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey5_id, 'CTA-AI-01.A1', 'Students grades improved but they cannot explain properly. AI helps writing but thinking is weak.', NOW() - INTERVAL 11 DAY),
                                                                                   (@survey5_id, 'CTA-AI-01.A2', 'Peripheral means far from city universities. These places do not have good facilities.', NOW() - INTERVAL 11 DAY),
                                                                                   (@survey5_id, 'CTA-AI-01.B1', 'They think grades mean students learned. But maybe AI just made the assignment look better.', NOW() - INTERVAL 11 DAY),
                                                                                   (@survey5_id, 'CTA-AI-01.B2', 'Argument is AI helps poor students. But real learning is more important than just marks.', NOW() - INTERVAL 11 DAY),
                                                                                   (@survey5_id, 'CTA-AI-01.B3', 'Evidence is grades went up. Opinion is teachers think AI is bad. Should look at both properly.', NOW() - INTERVAL 11 DAY),
                                                                                   (@survey5_id, 'CTA-AI-01.C1', 'Students write good but speak bad. Maybe AI helps only writing not thinking.', NOW() - INTERVAL 11 DAY),
                                                                                   (@survey5_id, 'CTA-AI-01.C2', 'Study if AI makes thinking worse. Compare students with and without AI. Check their test marks.', NOW() - INTERVAL 11 DAY),
                                                                                   (@survey5_id, 'CTA-AI-01.D1', 'No AI is better because I should learn to think myself first. Later when I am good at thinking then I can use AI.', NOW() - INTERVAL 11 DAY),
                                                                                   (@survey5_id, 'CTA-AI-01.D2', 'I did not use AI because I do not know how. I wrote everything from my own thinking.', NOW() - INTERVAL 11 DAY);

-- Continue with remaining 15 surveys...
-- Sample Survey 6
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester, location,
    devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai,
    submitted_by_ip, is_completed, created_at, updated_at
) VALUES (
             'Mohammad Ali', 'STU-2024-006', 21, 'Bachelor''s', '122', 'Urban',
             NULL, 'Yes', '4-8 hours', 'Yes', 'Yes',
             'Occasionally', 'Friends showed me how to use ChatGPT for homework help. I also saw videos on YouTube about AI tools for students.',
             '192.168.1.106', TRUE, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 10 DAY
         );

SET @survey6_id = LAST_INSERT_ID();

INSERT INTO survey_devices (survey_id, device) VALUES
                                                   (@survey6_id, 'Mobile devices (Smartphone, Smartwatches)'),
                                                   (@survey6_id, 'Laptop');

INSERT INTO survey_study_tools (survey_id, tool) VALUES
    (@survey6_id, 'ChatGPT');

INSERT INTO survey_writing_tools (survey_id, tool) VALUES
                                                       (@survey6_id, 'Grammarly'),
                                                       (@survey6_id, 'Quillbot');

-- Insert answers for Survey 6
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey6_id, 'CTA-AI-01.A1', 'The main problem is students are using AI to get better grades but they are not really learning. When teachers ask questions, students cannot answer because they did not understand what they wrote. The university thinks higher grades mean success but teachers worry about critical thinking.', NOW() - INTERVAL 10 DAY),
                                                                                   (@survey6_id, 'CTA-AI-01.A2', 'Peripheral higher education means universities that are not in the main cities and do not have many resources. They might adopt AI quickly to try to be modern like big universities but they face problems like bad internet and teachers who do not know how to use AI properly.', NOW() - INTERVAL 10 DAY),
                                                                                   (@survey6_id, 'CTA-AI-01.B1', 'Three assumptions are: First, they assume higher grades always mean better learning which is wrong because AI can make assignments look good without real understanding. Second, they think using new technology is always progress but technology needs good teaching too. Third, they assume peripheral universities should copy what big universities do.', NOW() - INTERVAL 10 DAY),
                                                                                   (@survey6_id, 'CTA-AI-01.B2', 'The argument is that AI helps students compete for jobs by getting good grades and critical thinking can wait. This has some truth because grades are important for jobs. But the weakness is that employers want people who can actually think not just people with good marks. If students only learn to use AI they will fail at work.', NOW() - INTERVAL 10 DAY),
                                                                                   (@survey6_id, 'CTA-AI-01.B3', 'Evidence includes the actual grade data showing improvement from B- to A- and students saying they write faster. Opinions are when teachers think students are too dependent or when management says this proves success. If we mix them up we might make wrong decisions based on what people feel instead of real facts.', NOW() - INTERVAL 10 DAY),
                                                                                   (@survey6_id, 'CTA-AI-01.C1', 'From the table we can see students do better on written work but worse on participation. This could mean AI helps with writing but not with thinking and speaking. Another explanation is maybe students were always weak at discussion and AI just makes this more obvious now because their writing looks so much better in comparison.', NOW() - INTERVAL 10 DAY),
                                                                                   (@survey6_id, 'CTA-AI-01.C2', 'Question: Does using AI reduce critical thinking? Hypothesis: Students who use AI more will have weaker critical thinking. Independent variable is how much they use AI. Dependent variable is critical thinking measured by tests and class discussions. Confounders are things like how smart they already were and how good their English is. Design: Compare AI users with non-users over one semester. Ethics: Make sure students know they can refuse to participate.', NOW() - INTERVAL 10 DAY),
                                                                                   (@survey6_id, 'CTA-AI-01.D1', 'I think guided use of AI is better. If I never use AI I will not know how to work with it in the future. But if I use it too much without thinking I will become lazy. The best way is to learn when AI is helpful and when I should think for myself. This is like learning any tool - you need to understand it properly.', NOW() - INTERVAL 10 DAY),
                                                                                   (@survey6_id, 'CTA-AI-01.D2', 'I used ChatGPT to help me understand what some questions were asking and to give me ideas about sociological concepts. It helped me remember things I learned before. But I think for some questions I just copied the AI structure instead of making my own organization which made my answers less original.', NOW() - INTERVAL 10 DAY);

-- Sample Survey 7
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester, location,
    devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai,
    submitted_by_ip, is_completed, created_at, updated_at
) VALUES (
             'Sumaiya Khatun', 'STU-2024-007', 20, 'Bachelor''s', '131', 'Urban',
             NULL, 'Yes', '1-3 hours', 'Yes', 'Yes',
             'Weekly', 'My teacher mentioned AI tools in class and I searched online to learn more about them.',
             '192.168.1.107', TRUE, NOW() - INTERVAL 9 DAY, NOW() - INTERVAL 9 DAY
         );

SET @survey7_id = LAST_INSERT_ID();

INSERT INTO survey_devices (survey_id, device) VALUES
    (@survey7_id, 'Mobile devices (Smartphone, Smartwatches)');

INSERT INTO survey_study_tools (survey_id, tool) VALUES
                                                     (@survey7_id, 'ChatGPT'),
                                                     (@survey7_id, 'Wolfram Alpha');

-- Insert answers for Survey 7 (Medium quality answers)
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey7_id, 'CTA-AI-01.A1', 'Students get higher grades using AI but their critical thinking is getting worse. They write good assignments but cannot explain them in class. Management is happy about grades but teachers are concerned about learning quality.', NOW() - INTERVAL 9 DAY),
                                                                                   (@survey7_id, 'CTA-AI-01.A2', 'Peripheral universities are far from main academic centers with less resources. They adopt AI to modernize but face infrastructure problems. The impact is different because these universities have different contexts than elite institutions.', NOW() - INTERVAL 9 DAY),
                                                                                   (@survey7_id, 'CTA-AI-01.B1', 'Management assumes grades equal learning which ignores that AI can produce good work without understanding. They assume AI adoption means progress. They assume peripheral schools should follow central models.', NOW() - INTERVAL 9 DAY),
                                                                                   (@survey7_id, 'CTA-AI-01.B2', 'Argument: AI democratizes knowledge, grades matter for jobs, so critical thinking is luxury. Strength: Recognizes job market pressure. Weakness: Prioritizes credentials over real learning, reinforces inequality through credential focus.', NOW() - INTERVAL 9 DAY),
                                                                                   (@survey7_id, 'CTA-AI-01.B3', 'Evidence: Grade statistics, time reports. Opinions: Teacher concerns, management celebration. Mixing them causes policy decisions based on bias not data.', NOW() - INTERVAL 9 DAY),
                                                                                   (@survey7_id, 'CTA-AI-01.C1', 'Written work improves but oral participation declines. AI helps writing but not understanding. Alternative: Students always struggled with speaking, AI makes contrast clearer.', NOW() - INTERVAL 9 DAY),
                                                                                   (@survey7_id, 'CTA-AI-01.C2', 'Question: Does AI harm critical thinking? Compare AI users with non-users. Measure CT through tests. Control for prior grades and language skills. Ethics: Ensure informed consent and no academic penalty.', NOW() - INTERVAL 9 DAY),
                                                                                   (@survey7_id, 'CTA-AI-01.D1', 'Guided AI use is better because complete avoidance leaves me unprepared for AI-using workplaces. But I need to learn critical evaluation to avoid dependency. This develops better long-term skills than total rejection.', NOW() - INTERVAL 9 DAY),
                                                                                   (@survey7_id, 'CTA-AI-01.D2', 'I used AI to brainstorm and structure arguments. It helped with organization but I tried to add my own examples. Sometimes I relied too much on AI suggestions which reduced originality.', NOW() - INTERVAL 9 DAY);

-- Sample Survey 8
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester, location,
    devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai,
    submitted_by_ip, is_completed, created_at, updated_at
) VALUES (
             'Tanvir Hassan', 'STU-2024-008', 22, 'Bachelor''s', '142', 'Rural',
             NULL, 'Yes', '1-3 hours', 'Yes', 'Yes',
             'Rarely', 'I learned about AI from online forums and tech blogs. I use it sometimes when stuck on difficult concepts.',
             '192.168.1.108', TRUE, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY
         );

SET @survey8_id = LAST_INSERT_ID();

INSERT INTO survey_devices (survey_id, device) VALUES
                                                   (@survey8_id, 'Mobile devices (Smartphone, Smartwatches)'),
                                                   (@survey8_id, 'Desktop');

INSERT INTO survey_study_tools (survey_id, tool) VALUES
    (@survey8_id, 'ChatGPT');

-- Shorter, less sophisticated answers for Survey 8
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey8_id, 'CTA-AI-01.A1', 'AI makes grades better but thinking worse. Students cannot explain their work. Problem is performance looks good but learning is bad.', NOW() - INTERVAL 8 DAY),
                                                                                   (@survey8_id, 'CTA-AI-01.A2', 'Peripheral means not central. These universities have less money and resources so AI impact is different than rich universities.', NOW() - INTERVAL 8 DAY),
                                                                                   (@survey8_id, 'CTA-AI-01.B1', 'They think grades show learning. They think AI is always good. They think copying other universities is right. All wrong because context matters.', NOW() - INTERVAL 8 DAY),
                                                                                   (@survey8_id, 'CTA-AI-01.B2', 'Says AI helps poor students get jobs. True that jobs need grades but also need real skills. Weak because ignores that work needs thinking not just certificates.', NOW() - INTERVAL 8 DAY),
                                                                                   (@survey8_id, 'CTA-AI-01.B3', 'Evidence is numbers. Opinion is feelings. Should separate them to make good decisions not based on emotions.', NOW() - INTERVAL 8 DAY),
                                                                                   (@survey8_id, 'CTA-AI-01.C1', 'Writing good, speaking bad. AI helps one not other. Maybe students were bad at speaking before too.', NOW() - INTERVAL 8 DAY),
                                                                                   (@survey8_id, 'CTA-AI-01.C2', 'Test if AI hurts thinking. Compare users and non-users. Measure thinking ability. Control other factors like previous grades.', NOW() - INTERVAL 8 DAY),
                                                                                   (@survey8_id, 'CTA-AI-01.D1', 'Some AI use is okay but not too much. Need balance to learn properly but also know modern tools.', NOW() - INTERVAL 8 DAY),
                                                                                   (@survey8_id, 'CTA-AI-01.D2', 'Used AI little bit for ideas. Helped some questions not others. Tried to think myself mostly.', NOW() - INTERVAL 8 DAY);

-- Continue with more surveys (9-20)
-- I'll add a few more with varying quality levels...

-- Sample Survey 9
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester, location,
    devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai,
    submitted_by_ip, is_completed, created_at, updated_at
) VALUES (
             'Rifat Akter', 'STU-2024-009', 21, 'Master''s', '212', 'Urban',
             NULL, 'Yes', '4-8 hours', 'Yes', 'Yes',
             'Daily', 'I learned through research methodology courses and academic workshops on digital tools for research.',
             '192.168.1.109', TRUE, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY
         );

SET @survey9_id = LAST_INSERT_ID();

INSERT INTO survey_devices (survey_id, device) VALUES
                                                   (@survey9_id, 'Laptop'),
                                                   (@survey9_id, 'Tablets');

INSERT INTO survey_study_tools (survey_id, tool) VALUES
                                                     (@survey9_id, 'ChatGPT'),
                                                     (@survey9_id, 'Perplexity AI'),
                                                     (@survey9_id, 'SciSpace');

INSERT INTO survey_writing_tools (survey_id, tool) VALUES
                                                       (@survey9_id, 'Grammarly'),
                                                       (@survey9_id, 'Jenni AI'),
                                                       (@survey9_id, 'Trinka AI');

INSERT INTO survey_research_tools (survey_id, tool) VALUES
                                                        (@survey9_id, 'Elicit Research'),
                                                        (@survey9_id, 'Scite.ai');

-- High quality answers for Survey 9
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey9_id, 'CTA-AI-01.A1', 'The case presents a fundamental paradox in contemporary higher education where artificial intelligence tools facilitate improved academic performance metrics while potentially undermining the development of critical thinking capacities. Students demonstrate enhanced written output quality through AI assistance yet exhibit diminished ability to articulate and defend arguments independently in oral contexts, revealing a disconnect between performative achievement and substantive learning.', NOW() - INTERVAL 7 DAY),
                                                                                   (@survey9_id, 'CTA-AI-01.A2', 'Peripheral higher education designates institutions positioned at geographic, economic, and symbolic margins of the academic field. This peripherality mediates AI adoption through resource constraints, infrastructure deficits, and aspirational positioning relative to elite institutions. The impact diverges from central universities due to contextual factors including digital literacy levels, pedagogical capacity for technology integration, and institutional support structures that shape implementation and effectiveness.', NOW() - INTERVAL 7 DAY),
                                                                                   (@survey9_id, 'CTA-AI-01.B1', 'First assumption: Grade inflation transparently signals improved learning, ignoring how AI can decouple assessment metrics from conceptual mastery. Second: Technological modernization inherently advances educational quality, reflecting techno-determinism that overlooks pedagogical transformation requirements. Third: Peripheral institutions should emulate central university models, perpetuating epistemic colonialism rather than developing contextually grounded approaches. Each assumption is problematic through sociological lens of cultural capital, institutional isomorphism, and symbolic violence.', NOW() - INTERVAL 7 DAY),
                                                                                   (@survey9_id, 'CTA-AI-01.B2', 'Reconstructed: P1: AI democratizes knowledge access. P2: Labor markets prioritize credentials over competencies. P3: Peripheral students face intensified competition. C: Critical thinking development is dispensable luxury. Strength: Acknowledges structural constraints and credential inflation. Weakness: Reproduces neoliberal instrumentalization of education while obscuring how credentialism without capability compounds marginalization. Bourdieu concept of cultural capital reveals how this logic naturalizes class reproduction through educational stratification disguised as meritocracy.', NOW() - INTERVAL 7 DAY),
                                                                                   (@survey9_id, 'CTA-AI-01.B3', 'Evidence: Quantified grade trajectories, temporal efficiency metrics, participation frequency data. Opinions: Causal attributions by teachers, success interpretations by administration, self-assessed dependency by students. Confusion enables confirmation bias where stakeholders selectively deploy evidence supporting predetermined positions. This produces policy legitimized through performative empiricism rather than systematic evaluation, exemplifying how institutional decision-making often privileges political expediency over analytical rigor.', NOW() - INTERVAL 7 DAY),
                                                                                   (@survey9_id, 'CTA-AI-01.C1', 'Table data indicates divergent performance trajectories across assessment modalities - written work improvement concurrent with oral participation decline. First interpretation: AI scaffolds surface production without cultivating deep processing. Second: Modality-specific assessment captures different cognitive dimensions, with AI selectively enhancing explicit knowledge demonstration over implicit understanding. Alternative explanations: Pedagogical emphasis historically privileged written over oral engagement; language anxiety and classroom power dynamics independently constrain participation; assessment design misalignment predates AI introduction. Hidden curriculum and sociolinguistic factors warrant consideration alongside technological mediation.', NOW() - INTERVAL 7 DAY),
                                                                                   (@survey9_id, 'CTA-AI-01.C2', 'Research question: How does AI tool usage mediate relationships between study engagement and critical thinking development among sociology students in peripheral higher education? Hypothesis: Intensive AI usage correlates with attenuated CT skill gains controlling for study time. Independent variables: AI usage patterns (frequency, type, purpose). Dependent variables: CT skills via standardized assessment and argumentative essay evaluation. Confounders: Prior achievement, SES, language proficiency, metacognitive awareness, institutional support. Design: Longitudinal mixed-methods tracking cohort across semester with pre/post CT testing, learning analytics, and interpretive interviews. Quasi-experimental comparison with historical cohort. Ethical concern: Risk of stigmatizing AI users or creating educational inequality through differential treatment in research protocol. Requires robust informed consent and protection against academic penalty.', NOW() - INTERVAL 7 DAY),
                                                                                   (@survey9_id, 'CTA-AI-01.D1', 'Guided critical use offers superior developmental trajectory. Drawing on Weber rationalization thesis and Ritzer McDonaldization framework, uncritical AI adoption risks epistemic deskilling through efficiency-driven routinization. However, technological abstinence ignores embedded AI reality of knowledge work. Critical pedagogy developing metacognitive awareness about AI affordances and constraints builds adaptive expertise. This aligns with Freire conscientization concept - cultivating critical consciousness about technological mediation rather than either techno-utopianism or reactionary rejection. Reflexive engagement enables appropriation of tools while maintaining epistemic agency.', NOW() - INTERVAL 7 DAY),
                                                                                   (@survey9_id, 'CTA-AI-01.D2', 'I deployed AI strategically for literature synthesis and theoretical framework recall, offloading memory work to focus on conceptual integration and argument construction. This genuinely enhanced analytical capacity for questions requiring sociological concept application. However, for data interpretation questions, AI-generated readings may have constrained imaginative inference by anchoring to initial suggestions. Critical challenge lies in maintaining agentive direction while leveraging computational assistance - using AI as thinking partner rather than substitute. Metacognitive monitoring of when AI augments versus supplants reasoning proves essential but cognitively demanding.', NOW() - INTERVAL 7 DAY);

-- Sample Survey 10
INSERT INTO survey_forms (
    name, student_id, age, level_of_study, academic_year_semester, location,
    devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools,
    ai_usage_frequency, learned_about_ai,
    submitted_by_ip, is_completed, created_at, updated_at
) VALUES (
             'Jamal Uddin', 'STU-2024-010', 20, 'Bachelor''s', '121', 'Rural',
             NULL, 'No', '< 1 hour', 'No', 'No',
             'Never', 'I do not know much about AI tools. Our area does not have good internet connection.',
             '192.168.1.110', TRUE, NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 6 DAY
         );

SET @survey10_id = LAST_INSERT_ID();

INSERT INTO survey_devices (survey_id, device) VALUES
    (@survey10_id, 'Mobile devices (Smartphone, Smartwatches)');

-- Very basic answers for Survey 10
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey10_id, 'CTA-AI-01.A1', 'Students use AI and get good marks but cannot think properly. Teachers worry about this.', NOW() - INTERVAL 6 DAY),
                                                                                   (@survey10_id, 'CTA-AI-01.A2', 'Peripheral means poor universities far from cities with no facilities.', NOW() - INTERVAL 6 DAY),
                                                                                   (@survey10_id, 'CTA-AI-01.B1', 'Management thinks high marks are good. But AI might be cheating.', NOW() - INTERVAL 6 DAY),
                                                                                   (@survey10_id, 'CTA-AI-01.B2', 'Says AI helps students. But thinking is also important for jobs.', NOW() - INTERVAL 6 DAY),
                                                                                   (@survey10_id, 'CTA-AI-01.B3', 'Evidence is data. Opinion is what people think. Should use evidence not opinion.', NOW() - INTERVAL 6 DAY),
                                                                                   (@survey10_id, 'CTA-AI-01.C1', 'Writing is better but talking is worse. AI helps writing only.', NOW() - INTERVAL 6 DAY),
                                                                                   (@survey10_id, 'CTA-AI-01.C2', 'Check if AI is bad for thinking. Test students who use AI and who do not.', NOW() - INTERVAL 6 DAY),
                                                                                   (@survey10_id, 'CTA-AI-01.D1', 'No AI is better so I can learn to think myself properly first.', NOW() - INTERVAL 6 DAY),
                                                                                   (@survey10_id, 'CTA-AI-01.D2', 'I did not use AI. I do not have internet. I wrote everything myself.', NOW() - INTERVAL 6 DAY);

-- Sample Survey 11-20 (I'll add them in a more condensed format)
-- Survey 11
INSERT INTO survey_forms (name, student_id, age, level_of_study, academic_year_semester, location, devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools, ai_usage_frequency, learned_about_ai, submitted_by_ip, is_completed, created_at, updated_at)
VALUES ('Sabina Yasmin', 'STU-2024-011', 21, 'Bachelor''s', '132', 'Urban', NULL, 'Yes', '4-8 hours', 'Yes', 'Yes', 'Daily', 'Social media and friends taught me about AI tools for studying.', '192.168.1.111', TRUE, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY);

SET @survey11_id = LAST_INSERT_ID();
INSERT INTO survey_devices (survey_id, device) VALUES (@survey11_id, 'Mobile devices (Smartphone, Smartwatches)'), (@survey11_id, 'Laptop');
INSERT INTO survey_study_tools (survey_id, tool) VALUES (@survey11_id, 'ChatGPT'), (@survey11_id, 'Khanmigo (Khan Academy)');
INSERT INTO survey_writing_tools (survey_id, tool) VALUES (@survey11_id, 'Quillbot');

INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey11_id, 'CTA-AI-01.A1', 'AI helps students get better grades but hurts their ability to think critically. They can write well but struggle to explain arguments verbally, creating a paradox between performance and learning.', NOW() - INTERVAL 5 DAY),
                                                                                   (@survey11_id, 'CTA-AI-01.A2', 'Peripheral universities lack resources and infrastructure. They adopt AI hoping to modernize but face challenges different from elite universities due to limited support and training.', NOW() - INTERVAL 5 DAY),
                                                                                   (@survey11_id, 'CTA-AI-01.B1', 'Assumes grades equal learning, technology equals progress, and peripheral schools should copy central models. All problematic because context and real understanding matter more than surface metrics.', NOW() - INTERVAL 5 DAY),
                                                                                   (@survey11_id, 'CTA-AI-01.B2', 'Argument: AI levels playing field, jobs need grades, so CT can wait. Strength: acknowledges real pressure. Weakness: treats education as credential factory not genuine learning. Reinforces inequality.', NOW() - INTERVAL 5 DAY),
                                                                                   (@survey11_id, 'CTA-AI-01.B3', 'Evidence: grade data and time reports. Opinions: teacher worries and admin celebration. Mixing them leads to decisions based on feelings not facts.', NOW() - INTERVAL 5 DAY),
                                                                                   (@survey11_id, 'CTA-AI-01.C1', 'Writing improves but discussion suffers. AI helps production but not comprehension. Alternative: maybe teaching always focused on writing over speaking, AI just reveals this.', NOW() - INTERVAL 5 DAY),
                                                                                   (@survey11_id, 'CTA-AI-01.C2', 'Question: Does AI reduce CT? Compare AI users vs non-users on CT tests. Control for prior grades and language ability. Ethics: ensure no punishment for participation.', NOW() - INTERVAL 5 DAY),
                                                                                   (@survey11_id, 'CTA-AI-01.D1', 'Guided use better because avoiding AI completely leaves me unprepared for future work. Critical use develops both AI literacy and independent thinking simultaneously.', NOW() - INTERVAL 5 DAY),
                                                                                   (@survey11_id, 'CTA-AI-01.D2', 'Used ChatGPT for brainstorming and structure. Helped with organization but sometimes limited my creativity by following AI suggestions too closely.', NOW() - INTERVAL 5 DAY);

-- Survey 12
INSERT INTO survey_forms (name, student_id, age, level_of_study, academic_year_semester, location, devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools, ai_usage_frequency, learned_about_ai, submitted_by_ip, is_completed, created_at, updated_at)
VALUES ('Imran Khan', 'STU-2024-012', 22, 'Bachelor''s', '141', 'Urban', NULL, 'Yes', '> 8 hours', 'Yes', 'Yes', 'Daily', 'Tech blogs and YouTube channels about productivity and studying with AI.', '192.168.1.112', TRUE, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY);

SET @survey12_id = LAST_INSERT_ID();
INSERT INTO survey_devices (survey_id, device) VALUES (@survey12_id, 'Mobile devices (Smartphone, Smartwatches)'), (@survey12_id, 'Laptop'), (@survey12_id, 'Tablets');
INSERT INTO survey_study_tools (survey_id, tool) VALUES (@survey12_id, 'ChatGPT'), (@survey12_id, 'Perplexity AI');
INSERT INTO survey_writing_tools (survey_id, tool) VALUES (@survey12_id, 'Grammarly'), (@survey12_id, 'Quillbot');
INSERT INTO survey_note_tools (survey_id, tool) VALUES (@survey12_id, 'Notion AI'), (@survey12_id, 'Otter.ai');

INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey12_id, 'CTA-AI-01.A1', 'Core issue: AI increases grades but decreases critical thinking. Students produce better written work but cannot defend arguments orally, revealing gap between performance and understanding in peripheral university context.', NOW() - INTERVAL 4 DAY),
                                                                                   (@survey12_id, 'CTA-AI-01.A2', 'Peripheral HE refers to under-resourced institutions at geographic and symbolic margins. Peripherality affects both AI adoption through infrastructure limits and impact through differential capacity for pedagogical integration compared to elite institutions.', NOW() - INTERVAL 4 DAY),
                                                                                   (@survey12_id, 'CTA-AI-01.B1', 'Three assumptions: 1) Grades transparently represent learning - ignores AI can inflate metrics without understanding. 2) Technology adoption equals educational modernization - overlooks need for pedagogical change. 3) Peripheral universities should mimic central models - reinforces colonial knowledge hierarchies.', NOW() - INTERVAL 4 DAY),
                                                                                   (@survey12_id, 'CTA-AI-01.B2', 'Premises: AI democratizes knowledge, grades crucial for jobs, students need competitive edge. Conclusion: CT is luxury. Analysis: Uses neoliberal logic treating education as credentialing. Strength: acknowledges real structural pressure. Weakness: perpetuates symbolic capital accumulation without substance, reproducing class inequality.', NOW() - INTERVAL 4 DAY),
                                                                                   (@survey12_id, 'CTA-AI-01.B3', 'Evidence: Statistical grade trends, time efficiency data, participation metrics. Opinions: Teacher attributions about causation, management success narratives. Confusion enables confirmation bias and policy based on selective interpretation rather than comprehensive analysis.', NOW() - INTERVAL 4 DAY),
                                                                                   (@survey12_id, 'CTA-AI-01.C1', 'Table shows writing improvement with participation decline. Interpretation 1: AI scaffolds production not comprehension. Interpretation 2: Different assessment modes measure distinct competencies. Alternative: Pedagogical bias toward written work predates AI; language anxiety factors separately.', NOW() - INTERVAL 4 DAY),
                                                                                   (@survey12_id, 'CTA-AI-01.C2', 'RQ: Does AI mediate study time and CT development? Hypothesis: High AI use reduces CT gains per study hour. IV: AI usage patterns. DV: CT skills via tests. Confounders: Prior achievement, SES, language proficiency. Design: Longitudinal with pre/post testing plus interviews. Ethics: Avoid stigmatization and ensure informed consent.', NOW() - INTERVAL 4 DAY),
                                                                                   (@survey12_id, 'CTA-AI-01.D1', 'Guided critical use preferable. Drawing on McDonaldization concept, uncritical AI adoption risks routinized thinking. But complete avoidance ignores reality of AI-embedded knowledge work. Critical engagement develops metacognitive awareness and adaptive expertise for AI-saturated futures.', NOW() - INTERVAL 4 DAY),
                                                                                   (@survey12_id, 'CTA-AI-01.D2', 'Used AI extensively for all questions - helped with structure, concept recall, and example generation. Enhanced efficiency and conceptual coverage. Risk: may have constrained original thinking by anchoring to AI-suggested frameworks. Need better metacognitive monitoring of when AI augments vs replaces reasoning.', NOW() - INTERVAL 4 DAY);

-- Surveys 13-20 (condensed further for brevity)
-- Survey 13
INSERT INTO survey_forms (name, student_id, age, level_of_study, academic_year_semester, location, devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools, ai_usage_frequency, learned_about_ai, submitted_by_ip, is_completed, created_at, updated_at)
VALUES ('Nasrin Sultana', 'STU-2024-013', 19, 'Bachelor''s', '112', 'Rural', NULL, 'Yes', '1-3 hours', 'Yes', 'Yes', 'Occasionally', 'University orientation program introduced AI tools.', '192.168.1.113', TRUE, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY);

SET @survey13_id = LAST_INSERT_ID();
INSERT INTO survey_devices (survey_id, device) VALUES (@survey13_id, 'Mobile devices (Smartphone, Smartwatches)');
INSERT INTO survey_study_tools (survey_id, tool) VALUES (@survey13_id, 'ChatGPT');

-- Medium quality answers for survey 13
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey13_id, 'CTA-AI-01.A1', 'Students grades improved with AI but critical thinking declined. They write well but cannot explain verbally. Management happy with grades, teachers concerned about learning.', NOW() - INTERVAL 3 DAY),
                                                                                   (@survey13_id, 'CTA-AI-01.A2', 'Peripheral means far from main universities with less resources. Affects AI use because poor infrastructure and untrained teachers.', NOW() - INTERVAL 3 DAY),
                                                                                   (@survey13_id, 'CTA-AI-01.B1', 'Assumes grades mean learning, AI means progress, should copy big universities. Wrong because real understanding matters more than grades.', NOW() - INTERVAL 3 DAY),
                                                                                   (@survey13_id, 'CTA-AI-01.B2', 'Says AI helps poor students compete for jobs. True grades matter but real skills also needed. Focuses too much on credentials.', NOW() - INTERVAL 3 DAY),
                                                                                   (@survey13_id, 'CTA-AI-01.B3', 'Evidence is grade data. Opinions are teacher and admin views. Should separate to make good policy not based on bias.', NOW() - INTERVAL 3 DAY),
                                                                                   (@survey13_id, 'CTA-AI-01.C1', 'Writing better but speaking worse. AI helps writing only. Maybe students were always weak at speaking.', NOW() - INTERVAL 3 DAY),
                                                                                   (@survey13_id, 'CTA-AI-01.C2', 'Test if AI reduces thinking. Compare users with non-users using CT tests. Control for previous grades.', NOW() - INTERVAL 3 DAY),
                                                                                   (@survey13_id, 'CTA-AI-01.D1', 'Guided AI use better because complete avoidance leaves me unprepared for modern work environment.', NOW() - INTERVAL 3 DAY),
                                                                                   (@survey13_id, 'CTA-AI-01.D2', 'Used AI for some questions to get ideas. Helped with structure but tried to add own thinking too.', NOW() - INTERVAL 3 DAY);

-- Survey 14
INSERT INTO survey_forms (name, student_id, age, level_of_study, academic_year_semester, location, devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools, ai_usage_frequency, learned_about_ai, submitted_by_ip, is_completed, created_at, updated_at)
VALUES ('Habib Rahman', 'STU-2024-014', 23, 'Master''s', '212', 'Urban', NULL, 'Yes', '4-8 hours', 'Yes', 'Yes', 'Weekly', 'Graduate seminars and research methodology courses covered AI tools.', '192.168.1.114', TRUE, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY);

SET @survey14_id = LAST_INSERT_ID();
INSERT INTO survey_devices (survey_id, device) VALUES (@survey14_id, 'Laptop'), (@survey14_id, 'Desktop');
INSERT INTO survey_study_tools (survey_id, tool) VALUES (@survey14_id, 'ChatGPT'), (@survey14_id, 'Elicit');
INSERT INTO survey_writing_tools (survey_id, tool) VALUES (@survey14_id, 'Grammarly'), (@survey14_id, 'Jenni AI');
INSERT INTO survey_research_tools (survey_id, tool) VALUES (@survey14_id, 'Elicit Research'), (@survey14_id, 'Mendeley');

-- High quality answers for survey 14
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey14_id, 'CTA-AI-01.A1', 'The case illuminates a fundamental tension between assessment metrics and learning outcomes. AI tools enable grade inflation while potentially attenuating critical thinking capacity, manifesting as divergence between written performance and oral articulation ability in peripheral higher education context.', NOW() - INTERVAL 2 DAY),
                                                                                   (@survey14_id, 'CTA-AI-01.A2', 'Peripheral higher education denotes institutions at geographic, economic, and symbolic margins of academic field. Peripherality mediates both AI adoption patterns through resource constraints and impact trajectories through differential institutional capacity for technology integration and pedagogical support.', NOW() - INTERVAL 2 DAY),
                                                                                   (@survey14_id, 'CTA-AI-01.B1', 'First: Grades transparently signal learning - ignores decoupling of metrics from mastery. Second: Technological adoption inherently modernizes education - reflects techno-determinism neglecting pedagogical transformation. Third: Peripheral institutions should emulate central models - perpetuates epistemic colonialism. Each assumption problematic through lenses of cultural capital, institutional isomorphism, symbolic violence.', NOW() - INTERVAL 2 DAY),
                                                                                   (@survey14_id, 'CTA-AI-01.B2', 'Reconstructed argument: P1: AI democratizes knowledge. P2: Labor markets privilege credentials. P3: Peripheral students face intensified competition. C: CT development is dispensable. Strength: acknowledges structural constraints. Weakness: instrumentalizes education through neoliberal logic while obscuring how credentialism without capability compounds marginalization. Bourdieu cultural capital concept reveals class reproduction.', NOW() - INTERVAL 2 DAY),
                                                                                   (@survey14_id, 'CTA-AI-01.B3', 'Evidence: Quantified grade trajectories, temporal metrics, participation data. Opinions: Teacher causal attributions, administrative success interpretations, student self-assessments. Confusion enables confirmation bias producing policy legitimized through selective empiricism rather than systematic evaluation.', NOW() - INTERVAL 2 DAY),
                                                                                   (@survey14_id, 'CTA-AI-01.C1', 'Table indicates divergent trajectories - written improvement with oral decline. Interpretation: AI scaffolds surface production without deep processing. Alternative: Pedagogical emphasis historically privileged written work; language anxiety and classroom dynamics independently constrain participation; assessment design misalignment predates AI introduction.', NOW() - INTERVAL 2 DAY),
                                                                                   (@survey14_id, 'CTA-AI-01.C2', 'RQ: How does AI mediate study engagement and CT development? Hypothesis: Intensive AI usage correlates with attenuated CT gains controlling for time. IV: AI usage patterns. DV: CT skills via standardized assessment. Confounders: Prior achievement, SES, language proficiency, metacognitive awareness. Design: Longitudinal mixed-methods with pre/post testing and interpretive interviews. Ethics: Avoid stigmatization and ensure informed consent.', NOW() - INTERVAL 2 DAY),
                                                                                   (@survey14_id, 'CTA-AI-01.D1', 'Guided critical use superior. Drawing on Weber rationalization and Ritzer McDonaldization, uncritical AI risks epistemic deskilling. Yet technological abstinence ignores embedded reality. Critical pedagogy developing metacognitive awareness builds adaptive expertise. Aligns with Freire conscientization - critical consciousness about technological mediation.', NOW() - INTERVAL 2 DAY),
                                                                                   (@survey14_id, 'CTA-AI-01.D2', 'Deployed AI strategically for literature synthesis and theoretical recall, offloading memory to focus on integration. Enhanced capacity for concept application. However, for inference questions, AI readings may have constrained imaginative reasoning. Challenge: maintaining agentive direction while leveraging assistance - using AI as partner not substitute.', NOW() - INTERVAL 2 DAY);

-- Surveys 15-20 (final batch)
-- Survey 15
INSERT INTO survey_forms (name, student_id, age, level_of_study, academic_year_semester, location, devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools, ai_usage_frequency, learned_about_ai, submitted_by_ip, is_completed, created_at, updated_at)
VALUES ('Shirin Akhter', 'STU-2024-015', 20, 'Bachelor''s', '122', 'Urban', NULL, 'Yes', '4-8 hours', 'Yes', 'Yes', 'Daily', 'Learned from peers and online tutorials about AI study tools.', '192.168.1.115', TRUE, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY);

SET @survey15_id = LAST_INSERT_ID();
INSERT INTO survey_devices (survey_id, device) VALUES (@survey15_id, 'Mobile devices (Smartphone, Smartwatches)'), (@survey15_id, 'Laptop');
INSERT INTO survey_study_tools (survey_id, tool) VALUES (@survey15_id, 'ChatGPT'), (@survey15_id, 'Perplexity AI');
INSERT INTO survey_writing_tools (survey_id, tool) VALUES (@survey15_id, 'Grammarly'), (@survey15_id, 'Quillbot');

-- Medium answers
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey15_id, 'CTA-AI-01.A1', 'Problem is AI helps grades but hurts critical thinking. Students write better but explain worse. Management sees success but teachers see problems.', NOW() - INTERVAL 1 DAY),
                                                                                   (@survey15_id, 'CTA-AI-01.A2', 'Peripheral universities have less resources and are far from big cities. This affects how they use AI and what results they get compared to rich universities.', NOW() - INTERVAL 1 DAY),
                                                                                   (@survey15_id, 'CTA-AI-01.B1', 'They assume grades show learning which is wrong. They assume AI is always good. They assume copying others is right. Context matters more.', NOW() - INTERVAL 1 DAY),
                                                                                   (@survey15_id, 'CTA-AI-01.B2', 'Argument is AI helps competition through grades. True that jobs need credentials but also need actual skills not just papers.', NOW() - INTERVAL 1 DAY),
                                                                                   (@survey15_id, 'CTA-AI-01.B3', 'Evidence is numbers and data. Opinion is what people believe. Mixing them causes bad decisions based on feelings.', NOW() - INTERVAL 1 DAY),
                                                                                   (@survey15_id, 'CTA-AI-01.C1', 'Writing is good but talking is bad. AI helps one not the other. Maybe students were always bad at discussions.', NOW() - INTERVAL 1 DAY),
                                                                                   (@survey15_id, 'CTA-AI-01.C2', 'Question: Does AI hurt thinking? Test AI users versus non-users. Measure thinking skills. Control for other factors.', NOW() - INTERVAL 1 DAY),
                                                                                   (@survey15_id, 'CTA-AI-01.D1', 'Guided use is better than no use. Need to learn AI for future work but also need to think independently.', NOW() - INTERVAL 1 DAY),
                                                                                   (@survey15_id, 'CTA-AI-01.D2', 'Used ChatGPT for help with structure and ideas. Sometimes helpful, sometimes made me lazy in thinking.', NOW() - INTERVAL 1 DAY);

-- Survey 16
INSERT INTO survey_forms (name, student_id, age, level_of_study, academic_year_semester, location, devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools, ai_usage_frequency, learned_about_ai, submitted_by_ip, is_completed, created_at, updated_at)
VALUES ('Mahmud Hasan', 'STU-2024-016', 21, 'Bachelor''s', '131', 'Rural', NULL, 'Yes', '1-3 hours', 'Yes', 'No', 'Rarely', 'Heard about AI tools but do not use them much due to limited internet.', '192.168.1.116', TRUE, NOW(), NOW());

SET @survey16_id = LAST_INSERT_ID();
INSERT INTO survey_devices (survey_id, device) VALUES (@survey16_id, 'Mobile devices (Smartphone, Smartwatches)');

-- Basic answers
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey16_id, 'CTA-AI-01.A1', 'AI gives higher grades but students cannot think well. Writing is good but explaining is bad.', NOW()),
                                                                                   (@survey16_id, 'CTA-AI-01.A2', 'Peripheral means poor universities. They have different problems than rich universities.', NOW()),
                                                                                   (@survey16_id, 'CTA-AI-01.B1', 'Management thinks grades are learning. But AI might be helping too much with cheating.', NOW()),
                                                                                   (@survey16_id, 'CTA-AI-01.B2', 'Says AI helps get jobs through grades. But jobs also need real thinking skills.', NOW()),
                                                                                   (@survey16_id, 'CTA-AI-01.B3', 'Evidence is facts. Opinion is beliefs. Should use facts not opinions for decisions.', NOW()),
                                                                                   (@survey16_id, 'CTA-AI-01.C1', 'Writing better speaking worse. AI helps writing not speaking.', NOW()),
                                                                                   (@survey16_id, 'CTA-AI-01.C2', 'Test if AI is bad. Compare users and non-users. Check their test scores.', NOW()),
                                                                                   (@survey16_id, 'CTA-AI-01.D1', 'No AI better because I need to learn thinking first before using tools.', NOW()),
                                                                                   (@survey16_id, 'CTA-AI-01.D2', 'Did not use AI. Do not have good internet. Wrote everything myself.', NOW());

-- Surveys 17-20 (final four)
INSERT INTO survey_forms (name, student_id, age, level_of_study, academic_year_semester, location, devices_others_specify, internet_access, hours_online, familiar_with_ai, use_ai_tools, ai_usage_frequency, learned_about_ai, submitted_by_ip, is_completed, created_at, updated_at) VALUES
                                                                                                                                                                                                                                                                                           ('Taslima Khatun', 'STU-2024-017', 22, 'Bachelor''s', '141', 'Urban', NULL, 'Yes', '4-8 hours', 'Yes', 'Yes', 'Weekly', 'University library workshops on digital tools.', '192.168.1.117', TRUE, NOW(), NOW()),
                                                                                                                                                                                                                                                                                           ('Rahim Mia', 'STU-2024-018', 20, 'Bachelor''s', '131', 'Rural', NULL, 'No', '< 1 hour', 'No', 'No', 'Never', 'Do not know about AI tools.', '192.168.1.118', TRUE, NOW(), NOW()),
                                                                                                                                                                                                                                                                                           ('Salma Begum', 'STU-2024-019', 21, 'Master''s', '211', 'Urban', NULL, 'Yes', '> 8 hours', 'Yes', 'Yes', 'Daily', 'Research methods course and academic journals.', '192.168.1.119', TRUE, NOW(), NOW()),
                                                                                                                                                                                                                                                                                           ('Kabir Ahmed', 'STU-2024-020', 23, 'Bachelor''s', '142', 'Urban', NULL, 'Yes', '4-8 hours', 'Yes', 'Yes', 'Occasionally', 'Tech YouTube channels and student forums.', '192.168.1.120', TRUE, NOW(), NOW());

-- Quick device and tool entries for final surveys
SET @survey17_id = LAST_INSERT_ID() - 3;
SET @survey18_id = LAST_INSERT_ID() - 2;
SET @survey19_id = LAST_INSERT_ID() - 1;
SET @survey20_id = LAST_INSERT_ID();

INSERT INTO survey_devices (survey_id, device) VALUES
                                                   (@survey17_id, 'Mobile devices (Smartphone, Smartwatches)'), (@survey17_id, 'Laptop'),
                                                   (@survey18_id, 'Mobile devices (Smartphone, Smartwatches)'),
                                                   (@survey19_id, 'Laptop'), (@survey19_id, 'Desktop'), (@survey19_id, 'Tablets'),
                                                   (@survey20_id, 'Mobile devices (Smartphone, Smartwatches)'), (@survey20_id, 'Laptop');

INSERT INTO survey_study_tools (survey_id, tool) VALUES
                                                     (@survey17_id, 'ChatGPT'), (@survey19_id, 'ChatGPT'), (@survey19_id, 'Perplexity AI'), (@survey19_id, 'Elicit'), (@survey20_id, 'ChatGPT');

INSERT INTO survey_writing_tools (survey_id, tool) VALUES
                                                       (@survey17_id, 'Grammarly'), (@survey19_id, 'Grammarly'), (@survey19_id, 'Jenni AI'), (@survey20_id, 'Quillbot');

-- Add simple answers for surveys 17-20 (varied quality)
-- Survey 17 (Medium quality)
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey17_id, 'CTA-AI-01.A1', 'AI improves grades but reduces critical thinking. Students write better assignments but struggle with oral explanations showing gap between performance and understanding.', NOW()),
                                                                                   (@survey17_id, 'CTA-AI-01.A2', 'Peripheral universities lack resources compared to central institutions. This affects AI adoption and impact due to infrastructure and training limitations.', NOW()),
                                                                                   (@survey17_id, 'CTA-AI-01.B1', 'Assumes grades equal learning, AI equals progress, peripheral should copy central. Problems: ignores context, real understanding, and local needs.', NOW()),
                                                                                   (@survey17_id, 'CTA-AI-01.B2', 'Argument: AI helps grades help jobs. Strength: acknowledges pressure. Weakness: prioritizes credentials over skills and learning.', NOW()),
                                                                                   (@survey17_id, 'CTA-AI-01.B3', 'Evidence: grade statistics. Opinions: stakeholder beliefs. Confusion leads to biased policy decisions.', NOW()),
                                                                                   (@survey17_id, 'CTA-AI-01.C1', 'Writing improves, speaking declines. AI helps production not comprehension. Alternative: teaching always emphasized writing.', NOW()),
                                                                                   (@survey17_id, 'CTA-AI-01.C2', 'Question: Does AI reduce CT? Design: compare users vs non-users controlling for confounders. Ethics: informed consent.', NOW()),
                                                                                   (@survey17_id, 'CTA-AI-01.D1', 'Guided use better. Avoidance leaves me unprepared. Critical engagement develops both skills.', NOW()),
                                                                                   (@survey17_id, 'CTA-AI-01.D2', 'Used AI for structure and concepts. Helped organization but sometimes limited creativity.', NOW());

-- Survey 18 (Very basic)
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey18_id, 'CTA-AI-01.A1', 'Students use AI get good marks but think bad.', NOW()),
                                                                                   (@survey18_id, 'CTA-AI-01.A2', 'Peripheral means poor university far from city.', NOW()),
                                                                                   (@survey18_id, 'CTA-AI-01.B1', 'Think grades are learning. Wrong because AI helps too much.', NOW()),
                                                                                   (@survey18_id, 'CTA-AI-01.B2', 'AI helps students get jobs. Need skills too not just grades.', NOW()),
                                                                                   (@survey18_id, 'CTA-AI-01.B3', 'Use facts not feelings for decisions.', NOW()),
                                                                                   (@survey18_id, 'CTA-AI-01.C1', 'Writing good speaking bad.', NOW()),
                                                                                   (@survey18_id, 'CTA-AI-01.C2', 'Test users and non-users.', NOW()),
                                                                                   (@survey18_id, 'CTA-AI-01.D1', 'No AI better for learning.', NOW()),
                                                                                   (@survey18_id, 'CTA-AI-01.D2', 'No AI used. No internet.', NOW());

-- Survey 19 (High quality)
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey19_id, 'CTA-AI-01.A1', 'The paradox reveals tension between metric-based success and substantive learning. AI enables grade inflation while potentially undermining critical thinking development, manifesting as divergence between written performance and oral articulation in peripheral higher education.', NOW()),
                                                                                   (@survey19_id, 'CTA-AI-01.A2', 'Peripheral higher education operates at geographic and symbolic margins with resource constraints. Peripherality shapes AI adoption through infrastructure limits and impact through differential capacity for pedagogical integration versus central institutions.', NOW()),
                                                                                   (@survey19_id, 'CTA-AI-01.B1', 'Three assumptions: grades transparently represent learning ignoring metric-mastery decoupling; technology inherently modernizes education reflecting techno-determinism; peripheral institutions should emulate central models perpetuating epistemic colonialism. Each problematic through cultural capital and symbolic violence lenses.', NOW()),
                                                                                   (@survey19_id, 'CTA-AI-01.B2', 'Premises: AI democratizes knowledge, markets privilege credentials, peripheral students face intensified competition. Conclusion: CT is dispensable luxury. Critique: neoliberal instrumentalization treating education as credentialing obscuring how capability-less credentials compound marginalization via Bourdieu cultural capital framework.', NOW()),
                                                                                   (@survey19_id, 'CTA-AI-01.B3', 'Evidence: quantified trajectories, efficiency metrics. Opinions: teacher attributions, administrative narratives. Confusion enables confirmation bias producing policy through performative empiricism versus systematic evaluation.', NOW()),
                                                                                   (@survey19_id, 'CTA-AI-01.C1', 'Divergent trajectories suggest AI scaffolds production without deep processing. Alternative explanations: pedagogical bias toward writing predates AI; language anxiety and power dynamics independently constrain participation; assessment misalignment precedes technology introduction.', NOW()),
                                                                                   (@survey19_id, 'CTA-AI-01.C2', 'RQ: AI mediation of study-CT relationship. Hypothesis: intensive usage attenuates CT gains. IV: usage patterns. DV: CT via tests. Confounders: prior achievement, SES, language, metacognition. Design: longitudinal mixed-methods. Ethics: avoid stigmatization, ensure consent.', NOW()),
                                                                                   (@survey19_id, 'CTA-AI-01.D1', 'Guided critical use optimal. Weber rationalization and Ritzer McDonaldization frameworks reveal uncritical adoption risks epistemic deskilling. Yet abstinence ignores embedded reality. Critical pedagogy developing metacognitive awareness per Freire conscientization builds adaptive expertise.', NOW()),
                                                                                   (@survey19_id, 'CTA-AI-01.D2', 'Strategic deployment for synthesis and recall offloaded memory enabling conceptual integration. Enhanced analytical capacity. However, inference questions risk AI-constrained imagination through anchoring. Challenge: maintaining agentive direction using AI as thinking partner not substitute requiring metacognitive monitoring.', NOW());

-- Survey 20 (Medium quality)
INSERT INTO ct_evaluation (survey_id, question_id, student_answer, created_at) VALUES
                                                                                   (@survey20_id, 'CTA-AI-01.A1', 'AI helps grades go up but critical thinking goes down. Students can write but not explain showing performance does not equal understanding.', NOW()),
                                                                                   (@survey20_id, 'CTA-AI-01.A2', 'Peripheral universities are under-resourced institutions far from academic centers. This affects both how they adopt AI and what impact it has due to infrastructure and support differences.', NOW()),
                                                                                   (@survey20_id, 'CTA-AI-01.B1', 'Management assumes: grades show learning, technology means progress, copying elite schools works. All problematic because context matters and real learning involves more than grades.', NOW()),
                                                                                   (@survey20_id, 'CTA-AI-01.B2', 'Argument says AI levels playing field and jobs need grades so thinking can wait. Some truth about pressure but wrong to treat education as just credential factory. Real skills matter for careers.', NOW()),
                                                                                   (@survey20_id, 'CTA-AI-01.B3', 'Evidence is data like grade trends. Opinions are what teachers and admin believe. Mixing them makes policy based on bias not objective analysis.', NOW()),
                                                                                   (@survey20_id, 'CTA-AI-01.C1', 'Writing improves but discussion suffers. AI helps written output not understanding. Alternatively, maybe teaching always focused more on writing than speaking.', NOW()),
                                                                                   (@survey20_id, 'CTA-AI-01.C2', 'Research question: does AI hurt CT? Compare AI users with non-users measuring CT skills. Control for prior grades and language ability. Ethics: get consent and ensure no harm.', NOW()),
                                                                                   (@survey20_id, 'CTA-AI-01.D1', 'Guided use better than total avoidance. Need AI literacy for future work but also independent thinking. Balance develops both competencies.', NOW()),
                                                                                   (@survey20_id, 'CTA-AI-01.D2', 'Used AI occasionally for brainstorming and structure. Helped with organization but sometimes followed AI too closely reducing originality.', NOW());