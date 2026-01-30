package view.coordinator;

import controller.ScheduleController;
import model.Coordinator;
import model.EvaluationResult;
import model.Session;
import service.ReportService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;

// Seminar Schedule & Report dashboard
public class ScheduleForm extends JFrame {

    private JTable scheduleTable;
    private JButton btnViewSchedule;
    private JButton btnGenerateReport;
    private JButton btnAnalytics;
    private JButton btnReportSummary;
    private JButton btnBack;
    private Coordinator coordinator;

    private List<Session> currentSessions;

    public ScheduleForm(Coordinator coordinator) {
        this.coordinator = coordinator;

        setTitle("Seminar Schedule & Report");
        setSize(1200, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JLabel title = new JLabel("Seminar Schedule & Report", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        scheduleTable = new JTable();

        btnViewSchedule = new JButton("View Schedule & Report");
        btnGenerateReport = new JButton("Generate Report (Excel)");
        btnAnalytics = new JButton("Analytics");
        btnReportSummary = new JButton("Report Summary");
        btnBack = new JButton("Back");

        // Button actions
        btnViewSchedule.addActionListener(e -> viewSchedule());

        btnGenerateReport.addActionListener(e -> {
            if (currentSessions == null || currentSessions.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please view the schedule first before generating the report.");
                return;
            }

            // Generate Excel report only (Report folder)
            ReportService.generateExcelReport(currentSessions, "Report");

            JOptionPane.showMessageDialog(this,
                    "Excel report generated successfully in folder 'Report'.");
        });

        btnAnalytics.addActionListener(e -> {
            if (currentSessions == null || currentSessions.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please view the schedule first before opening analytics.");
                return;
            }
            new AnalyticsChartsForm(currentSessions);
        });

        btnReportSummary.addActionListener(e -> {
            if (currentSessions == null || currentSessions.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please view the schedule first before opening report summary.");
                return;
            }
            new ReportSummaryForm(currentSessions);
        });

        btnBack.addActionListener(e -> {
            dispose();
            new CoordinatorDashboard(coordinator);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnViewSchedule);
        buttonPanel.add(btnGenerateReport);
        buttonPanel.add(btnAnalytics);
        buttonPanel.add(btnReportSummary);
        buttonPanel.add(btnBack);

        setLayout(new BorderLayout(10, 10));
        add(title, BorderLayout.NORTH);
        add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // View Schedule
    private void viewSchedule() {
        ScheduleController controller = new ScheduleController();

        List<Session> fetchedSessions = controller.generateScheduleAndReport();
        if (fetchedSessions == null) fetchedSessions = List.of();

        // Remove duplicates based on Session ID
        Map<Integer, Session> uniqueSessions = new LinkedHashMap<>();
        for (Session s : fetchedSessions) {
            uniqueSessions.put(s.getSessionID(), s);
        }

        currentSessions = new ArrayList<>(uniqueSessions.values());

        // Clear existing table
        DefaultTableModel model = new DefaultTableModel(
                new String[]{
                        "Session ID", "Date", "Venue", "Type", "Time Slot",
                        "Submission IDs", "Evaluator IDs", "Scores & Comments"
                }, 0
        );

        for (Session s : currentSessions) {
            String submissionIDs = String.join(", ",
                    s.getStudentIDs().stream().map(String::valueOf).toArray(String[]::new)
            );

            String evaluatorIDs = String.join(", ",
                    s.getEvaluatorIDs().stream().map(String::valueOf).toArray(String[]::new)
            );

            StringBuilder evalSummary = new StringBuilder();
            for (EvaluationResult er : s.getEvaluationResults()) {
                evalSummary.append(er.getTotalScore())
                        .append(" - ")
                        .append(er.getComments())
                        .append("\n");
            }

            model.addRow(new Object[]{
                    s.getSessionID(),
                    s.getDate(),
                    s.getVenue(),
                    s.getSessionType(),
                    s.getTimeSlot() != null ? s.getTimeSlot() : "",
                    submissionIDs,
                    evaluatorIDs,
                    evalSummary.toString().trim()
            });
        }

        scheduleTable.setModel(model);
        scheduleTable.getColumnModel().getColumn(7)
                .setCellRenderer(new MultilineCellRenderer());

        int[] widths = {80, 100, 150, 150, 100, 120, 120, 300};
        for (int i = 0; i < widths.length; i++) {
            TableColumn column = scheduleTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }

    // Multiline cell renderer for comments column
    static class MultilineCellRenderer extends JTextArea implements TableCellRenderer {
        public MultilineCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            setText(value != null ? value.toString() : "");
            setFont(table.getFont());

            int preferredHeight = getPreferredSize().height;
            if (table.getRowHeight(row) != preferredHeight) {
                table.setRowHeight(row, preferredHeight);
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            return this;
        }
    }

    // AnalyticsChartsForm
    public static class AnalyticsChartsForm extends JFrame {
        public AnalyticsChartsForm(List<Session> sessions) {
            setTitle("Analytics");
            setSize(1000, 720);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.add("Bar Chart", new AnalyticsForm.StackedBarChartPanel(sessions));
            tabbedPane.add("Pie Charts", new JScrollPane(new AnalyticsForm.PieChartPanel(sessions)));

            add(tabbedPane);
            setVisible(true);
        }
    }

    // ReportSummaryForm
    public static class ReportSummaryForm extends JFrame {
        public ReportSummaryForm(List<Session> sessions) {
            setTitle("Report Summary");
            setSize(1000, 720);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.add("Report Summary", new JScrollPane(new AnalyticsForm.ReportSummaryPanel(sessions)));
            tabbedPane.add("Top 3 Students", new JScrollPane(new AnalyticsForm.Top3StudentsPanel(sessions)));

            add(tabbedPane);
            setVisible(true);
        }
    }
}
