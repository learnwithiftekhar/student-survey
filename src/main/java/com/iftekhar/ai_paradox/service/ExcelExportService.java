package com.iftekhar.ai_paradox.service;

import com.iftekhar.ai_paradox.dto.EvaluationDisplayDto;
import com.iftekhar.ai_paradox.dto.SurveyFormDTO;
import com.iftekhar.ai_paradox.model.SurveyForm;
import com.iftekhar.ai_paradox.repository.SurveyFormRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelExportService {

    private final SurveyService surveyService;
    private final SurveyFormRepository surveyFormRepository;

    public byte[] exportAllSurveysToExcel() throws IOException {
        log.info("Starting Excel export of all surveys");

        // Fetch all surveys from database
        List<SurveyForm> allSurveys = surveyFormRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Create sheet
            Sheet sheet = workbook.createSheet("Survey Data");

            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle wrapTextStyle = createWrapTextStyle(workbook);

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Survey ID", "Student Name", "Student ID", "Group", "Created At",
                    "Q1: Core Problem Summary", "Q1 Score",
                    "Q2: Peripheral Education", "Q2 Score",
                    "Q3: Implicit Assumptions", "Q3 Score",
                    "Q4: Argument Analysis", "Q4 Score",
                    "Q5: Evidence vs Opinion", "Q5 Score",
                    "Q6: Table Inferences", "Q6 Score",
                    "Q7: Research Design", "Q7 Score",
                    "Q8: Learning Benefit", "Q8 Score",
                    "Q9: AI Usage Reflection", "Q9 Score",
                    "Total Score", "Proficiency Level", "Status"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fill data rows
            int rowNum = 1;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (SurveyForm surveyForm : allSurveys) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;

                // Get the full DTO with evaluation data
                SurveyFormDTO dto = surveyService.getSurveyById(surveyForm.getId());

                // Get individual question scores
                Map<String, Integer> questionScores = new HashMap<>();
                if (dto.getEvaluated() != null && dto.getEvaluated()) {
                    List<EvaluationDisplayDto> evaluations = surveyService.getEvaluationsBySurveyId(surveyForm.getId());
                    for (EvaluationDisplayDto eval : evaluations) {
                        questionScores.put(eval.getQuestionId(), eval.getScore());
                    }
                }

                // Survey basic info
                createCell(row, colNum++, dto.getId(), dataStyle);
                createCell(row, colNum++, dto.getName(), dataStyle);
                createCell(row, colNum++, dto.getStudentId(), dataStyle);
                createCell(row, colNum++, dto.getGroupType() != null ? dto.getGroupType().toString() : "", dataStyle);
                createCell(row, colNum++, dto.getCreatedAt() != null ? dto.getCreatedAt().format(dateFormatter) : "", dataStyle);

                // Q1: Core Problem Summary (CTA-AI-01.A1)
                createCell(row, colNum++, dto.getCoreProblemSummary(), wrapTextStyle);
                createCell(row, colNum++, questionScores.get("CTA-AI-01.A1"), dataStyle);

                // Q2: Peripheral Education (CTA-AI-01.A2)
                createCell(row, colNum++, dto.getPeripheralEducation(), wrapTextStyle);
                createCell(row, colNum++, questionScores.get("CTA-AI-01.A2"), dataStyle);

                // Q3: Implicit Assumptions (CTA-AI-01.B1)
                createCell(row, colNum++, dto.getImplicitAssumptions(), wrapTextStyle);
                createCell(row, colNum++, questionScores.get("CTA-AI-01.B1"), dataStyle);

                // Q4: Argument Analysis (CTA-AI-01.B2)
                createCell(row, colNum++, dto.getArgumentAnalysis(), wrapTextStyle);
                createCell(row, colNum++, questionScores.get("CTA-AI-01.B2"), dataStyle);

                // Q5: Evidence vs Opinion (CTA-AI-01.B3)
                createCell(row, colNum++, dto.getEvidenceVsOpinion(), wrapTextStyle);
                createCell(row, colNum++, questionScores.get("CTA-AI-01.B3"), dataStyle);

                // Q6: Table Inferences (CTA-AI-01.C1)
                createCell(row, colNum++, dto.getTableInferences(), wrapTextStyle);
                createCell(row, colNum++, questionScores.get("CTA-AI-01.C1"), dataStyle);

                // Q7: Research Design (CTA-AI-01.C2)
                createCell(row, colNum++, dto.getResearchDesign(), wrapTextStyle);
                createCell(row, colNum++, questionScores.get("CTA-AI-01.C2"), dataStyle);

                // Q8: Learning Benefit (CTA-AI-01.D1)
                createCell(row, colNum++, dto.getLearningBenefit(), wrapTextStyle);
                createCell(row, colNum++, questionScores.get("CTA-AI-01.D1"), dataStyle);

                // Q9: AI Usage Reflection (CTA-AI-01.D2)
                createCell(row, colNum++, dto.getAiUsageReflection(), wrapTextStyle);
                createCell(row, colNum++, questionScores.get("CTA-AI-01.D2"), dataStyle);

                // Total score and proficiency
                createCell(row, colNum++, dto.getOverallScore(), dataStyle);
                createCell(row, colNum++, dto.getCtLevel(), dataStyle);
                createCell(row, colNum++, (dto.getEvaluated() != null && dto.getEvaluated()) ? "Evaluated" : "Pending", dataStyle);
            }

            // Auto-size columns (except long text columns)
            for (int i = 0; i < headers.length; i++) {
                if (i % 2 == 0 && i > 4 && i < headers.length - 3) {
                    // Question columns - set fixed width
                    sheet.setColumnWidth(i, 15000);
                } else {
                    // Other columns - auto-size
                    sheet.autoSizeColumn(i);
                }
            }

            workbook.write(outputStream);
            log.info("Excel export completed. Total surveys: {}", allSurveys.size());
            return outputStream.toByteArray();
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createWrapTextStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setWrapText(true);
        return style;
    }

    private void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);

        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue(value.toString());
        }

        cell.setCellStyle(style);
    }
}
