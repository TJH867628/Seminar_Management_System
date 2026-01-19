package service;

import model.EvaluationResult;
import model.Session;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportService {

    //**Generates a TXT report for the given list of sessions.
    public static void generateReport(List<Session> sessions) {

        try (FileWriter fw = new FileWriter("seminar_schedule_report.txt")) {

            fw.write("SEMINAR SCHEDULE REPORT\n");
            fw.write("========================\n\n");

            for (Session s : sessions) {
                fw.write("Session ID: " + s.getSessionID() + "\n");
                fw.write("Date: " + s.getDate() + "\n");
                fw.write("Venue: " + s.getVenue() + "\n");
                fw.write("Type: " + s.getSessionType() + "\n");
                fw.write("Time Slot: " + (s.getTimeSlot() != null ? s.getTimeSlot() : "") + "\n");
                fw.write("Submission IDs: " + s.getStudentIDsString() + "\n");
                fw.write("Evaluator IDs: " + s.getEvaluatorIDsString() + "\n");

                fw.write("Scores & Comments:\n");
                List<EvaluationResult> evals = s.getEvaluationResults();

                if (evals.isEmpty()) {
                    fw.write("No evaluations available.\n");
                } else {
                    for (EvaluationResult er : evals) {
                        fw.write(er.getTotalScore() + " - " + er.getComments() + "\n");
                    }
                }

                fw.write("------------------------------------------------------\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
