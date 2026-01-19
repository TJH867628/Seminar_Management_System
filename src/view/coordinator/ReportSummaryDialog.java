package view.coordinator;

import model.Session;
import model.EvaluationResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


// ReportSummaryDialog shows: Report summary of all students grouped by session type/date/session，Top 3 students by highest total score

public class ReportSummaryDialog extends JFrame {

    public ReportSummaryDialog(List<Session> sessions) {
        setTitle("Report Summary");
        setSize(1000, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with vertical BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //  Report Summary Tables 
        Map<String, List<Session>> grouped = sessions.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getSessionType() + " " + s.getDate() + " (Session " + s.getSessionID() + ")",
                        LinkedHashMap::new, Collectors.toList()));

        for (String key : grouped.keySet()) {
            mainPanel.add(createReportTablePanel(key, grouped.get(key)));
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // Top 3 Students Table 
        mainPanel.add(new JLabel("Top 3 Students by Total Score"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createTop3StudentsPanel(sessions));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Report Summary Table 
    private JPanel createReportTablePanel(String title, List<Session> sessions) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTitle, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Submission ID", "Evaluator ID", "Total Score", "Comments"}, 0
        );

        for (Session s : sessions) {
            for (EvaluationResult er : s.getEvaluationResults()) {
                model.addRow(new Object[]{
                        er.getSubmissionID(),
                        er.getEvaluatorID(),
                        er.getTotalScore(),
                        er.getComments()
                });
            }
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        panel.setPreferredSize(new Dimension(900, Math.min(200 + sessions.size() * 50, 400)));
        return panel;
    }

    // Top 3 Students Panel 
    private JPanel createTop3StudentsPanel(List<Session> sessions) {
        JPanel panel = new JPanel(new BorderLayout());

        // Collect all evaluation results
        List<EvaluationResult> allResults = new ArrayList<>();
        for (Session s : sessions) {
            allResults.addAll(s.getEvaluationResults());
        }

        // Sort by total score descending
        allResults.sort(Comparator.comparing(EvaluationResult::getTotalScore).reversed());

        // Take top 3
        List<EvaluationResult> top3 = allResults.stream().limit(3).collect(Collectors.toList());

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Submission ID", "Evaluator ID", "Total Score", "Comments"}, 0
        );

        for (EvaluationResult er : top3) {
            model.addRow(new Object[]{
                    er.getSubmissionID(),
                    er.getEvaluatorID(),
                    er.getTotalScore(),
                    er.getComments()
            });
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(900, 200));

        return panel;
    }
}
