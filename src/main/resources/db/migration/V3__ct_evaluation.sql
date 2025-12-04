CREATE TABLE ct_evaluation (
                               id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                               survey_id       BIGINT      NOT NULL,
                               question_id     VARCHAR(50) NOT NULL,
                               student_answer  TEXT        NOT NULL,

                               score           INT         NULL,
                               on_topic        TINYINT(1)  NULL,
                               skills_json     JSON        NULL,
                               reason          TEXT        NULL,
                               strength        TEXT        NULL,
                               weakness        TEXT        NULL,
                               eval_model      VARCHAR(100) NULL,
                               created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_eval_question FOREIGN KEY (question_id)
                                   REFERENCES ct_question(id) ON DELETE RESTRICT,
                               CONSTRAINT fk_eval_survey FOREIGN KEY (survey_id)
                                   REFERENCES survey_forms(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
