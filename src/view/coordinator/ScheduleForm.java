package view.coordinator;

import controller.ScheduleController;
import model.Coordinator;
import model.EvaluationResult;
import model.Session;
import service.ReportService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


// View schedule & report (shows schedule table), Generate TXT report, Analytics (Bar Chart + Pie Chart tabs),Report Summary button (Report Summary + Top 3 students)
 
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
        btnGenerateReport = new JButton("Generate Report ( .TXT)");
        btnAnalytics = new JButton("Analytics");
        btnReportSummary = new JButton("Report Summary");
        btnBack = new JButton("Back");

        // Button Actions 
        btnViewSchedule.addActionListener(e -> viewSchedule());

        btnGenerateReport.addActionListener(e -> generateReport());

        btnAnalytics.addActionListener(e -> {
            if (currentSessions == null || currentSessions.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please view the schedule first before opening analytics.");
                return;
            }
            new AnalyticsChartsForm(currentSessions); // Bar Chart + Pie Chart only
        });

        btnReportSummary.addActionListener(e -> {
            if (currentSessions == null || currentSessions.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please view the schedule first before opening report summary.");
                return;
            }
            new ReportSummaryForm(currentSessions); // Report Summary + Top 3 Students
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
        currentSessions = controller.generateScheduleAndReport();

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

    // Generate TXT Report 
    private void generateReport() {
        if (currentSessions == null || currentSessions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please view the schedule first before generating the report.");
            return;
        }

        ReportService.generateReport(currentSessions);

        JOptionPane.showMessageDialog(this,
                "TXT report generated successfully: seminar_schedule_report.txt");
    }

    //  Multiline Cell Renderer 
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

    //  Analytics Charts Form (Bar + Pie) 
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

    //  Report Summary Form (Report Summary + Top 3 Students) 
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
