ALTER TABLE survey_forms
ADD COLUMN group_type VARCHAR(20) NOT NULL DEFAULT 'GROUP_B'
AFTER id;

CREATE INDEX idx_survey_forms_group_type ON survey_forms(group_type);