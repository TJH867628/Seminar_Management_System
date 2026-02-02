package service;

import model.Session;
import model.Award;
import model.EvaluationResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.CoordinatorDAO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReportService {

    // Generate Excel report (2 sheets: Report Summary + Top 3 Students)
    public static void generateExcelReport(List<Session> sessions, String folderName) {
        try {
            // Ensure folder exists
            Files.createDirectories(Paths.get(folderName));

            Workbook workbook = new XSSFWorkbook();

            // Sheet 1: Report Summary
            Sheet summarySheet = workbook.createSheet("Report Summary");
            int rowNum = 0;
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);

            for (Session s : sessions) {
                Row titleRow = summarySheet.createRow(rowNum++);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("Session " + s.getSessionID() + " - " + s.getSessionType() + " (" + s.getDate() + ")");
                rowNum++;

                Row headerRow = summarySheet.createRow(rowNum++);
                String[] headers = {"Submission ID", "Evaluator ID", "Total Score", "Comments"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(borderStyle);
                }

                for (EvaluationResult er : s.getEvaluationResults()) {
                    Row row = summarySheet.createRow(rowNum++);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue(er.getSubmissionID());
                    cell0.setCellStyle(borderStyle);

                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(er.getEvaluatorID());
                    cell1.setCellStyle(borderStyle);

                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(er.getTotalScore());
                    cell2.setCellStyle(borderStyle);

                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(er.getComments());
                    cell3.setCellStyle(borderStyle);
                }
                rowNum += 2; // spacing between sessions
            }

            // Sheet 2: Top 3 Students
            Sheet top3Sheet = workbook.createSheet("Top 3 Students");
            List<EvaluationResult> allResults = sessions.stream()
                    .flatMap(s -> s.getEvaluationResults().stream())
                    .sorted((a, b) -> b.getTotalScore() - a.getTotalScore())
                    .limit(3)
                    .toList();

            Row headerRow = top3Sheet.createRow(0);
            String[] headers = {"Submission ID", "Evaluator ID", "Total Score", "Comments"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(borderStyle);
            }

            int rowNumTop = 1;
            for (EvaluationResult er : allResults) {
                Row row = top3Sheet.createRow(rowNumTop++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(er.getSubmissionID());
                cell0.setCellStyle(borderStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(er.getEvaluatorID());
                cell1.setCellStyle(borderStyle);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(er.getTotalScore());
                cell2.setCellStyle(borderStyle);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(er.getComments());
                cell3.setCellStyle(borderStyle);
            }

            // Auto-size columns
            for (Sheet sheet : List.of(summarySheet, top3Sheet)) {
                for (int i = 0; i < 4; i++) sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(folderName + "/seminar_schedule_report.xlsx")) {
                workbook.write(fos);
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // New method: Export Award Nomination Results to Excel
    public static void generateAwardReport(String folderName) {
        try {
            // Ensure folder exists
            Files.createDirectories(Paths.get(folderName));

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Award Nomination Results");
            int rowNum = 0;

            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);

            // Header row
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"Award ID", "Award Name", "Category", "Criteria",
                    "Submission ID", "Student Name", "Presentation Type", "Score/Votes"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(borderStyle);
            }

            // Fetch data from DAO
            CoordinatorDAO dao = new CoordinatorDAO();
            List<Award> awards = dao.getAwardWinners();

            for (Award a : awards) {
                Row row = sheet.createRow(rowNum++);
                Cell cell0 = row.createCell(0); cell0.setCellValue(a.getAwardID()); cell0.setCellStyle(borderStyle);
                Cell cell1 = row.createCell(1); cell1.setCellValue(a.getAwardName()); cell1.setCellStyle(borderStyle);
                Cell cell2 = row.createCell(2); cell2.setCellValue(a.getCategory()); cell2.setCellStyle(borderStyle);
                Cell cell3 = row.createCell(3); cell3.setCellValue(a.getCriteria()); cell3.setCellStyle(borderStyle);
                Cell cell4 = row.createCell(4); cell4.setCellValue(a.getSubmissionID()); cell4.setCellStyle(borderStyle);
                Cell cell5 = row.createCell(5); cell5.setCellValue(a.getStudentName()); cell5.setCellStyle(borderStyle);
                Cell cell6 = row.createCell(6); cell6.setCellValue(a.getPresentationType()); cell6.setCellStyle(borderStyle);
                Cell cell7 = row.createCell(7); cell7.setCellValue(a.getScoreOrVotes()); cell7.setCellStyle(borderStyle);
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            try (FileOutputStream fos = new FileOutputStream(folderName + "/award_nomination_results.xlsx")) {
                workbook.write(fos);
            }

            workbook.close();
            System.out.println("Award report generated successfully in folder: " + folderName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

