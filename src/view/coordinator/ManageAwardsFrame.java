package view.coordinator;

import model.Award;
import model.Coordinator;
import service.ReportService;
import dao.CoordinatorDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageAwardsFrame extends JFrame {

    private Coordinator coordinator;
    private CoordinatorDAO dao;
    private DefaultTableModel model;
    private JTable table;

    public ManageAwardsFrame(Coordinator coordinator) {
        this.coordinator = coordinator;
        this.dao = new CoordinatorDAO();

        setTitle("Manage Awards");
        setSize(1600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("Awards Nomination Results", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        // Table for Nomination Results
        String[] columns = {"Award ID", "Award Name", "Category", "Criteria",
                "Submission ID", "Student Name", "Presentation Type", "Score/Votes"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel();

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadAwardWinners());

        JButton generateReportBtn = new JButton("Generate Award Report (Excel)");
        generateReportBtn.addActionListener(e -> {
            try {
                ReportService.generateAwardReport("Report");
                JOptionPane.showMessageDialog(this,
                        "Award report generated successfully in the Report folder!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error generating award report: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // View Award Agenda Button 
        JButton viewAgendaBtn = new JButton("View Award Agenda");
        viewAgendaBtn.addActionListener(e -> new AwardAgendaFrame(coordinator));

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> backToDashboard());

        bottomPanel.add(refreshBtn);
        bottomPanel.add(generateReportBtn);
        bottomPanel.add(viewAgendaBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initial load
        loadAwardWinners();

        // Auto-refresh every 10 seconds
        Timer timer = new Timer(10000, e -> loadAwardWinners());
        timer.start();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadAwardWinners() {
        model.setRowCount(0); // clear first
        try {
            List<Award> winners = dao.getAwardWinners(); // fetch updated results
            for (Award a : winners) {
                model.addRow(new Object[]{
                        a.getAwardID(),
                        a.getAwardName(),
                        a.getCategory(),
                        a.getCriteria(),
                        a.getSubmissionID(),
                        a.getStudentName(),
                        a.getPresentationType(),
                        a.getScoreOrVotes()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error fetching award winners: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToDashboard() {
        dispose();
        new CoordinatorDashboard(coordinator);
    }

    public static class AwardAgendaFrame extends JFrame {

        public AwardAgendaFrame(Coordinator coordinator) {
            setTitle("Award Agenda");
            setSize(900, 400);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            String[] columns = {"Time", "Activity", "Award/Category", "Presenter", "Venue"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable table = new JTable(model);
            table.setRowHeight(30);

            //  Opening Ceremony
            model.addRow(new Object[]{"09:00", "Opening Ceremony", "-", "Coordinator", "Multipurpose Hall"});

            //  Award Presentation (fetch dynamically)
            try {
                CoordinatorDAO dao = new CoordinatorDAO();
                List<Award> awards = dao.getAllAwards(); // fetch all awards

                int hour = 9, minute = 15;
                for (Award award : awards) {
                    String time = String.format("%02d:%02d", hour, minute);
                    String awardCategory = award.getAwardName();
                    model.addRow(new Object[]{time, "Award Presentation", awardCategory, "Coordinator", "Multipurpose Hall"});

                    minute += 15;
                    if (minute >= 60) {
                        minute -= 60;
                        hour += 1;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error loading awards: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            //  Closing Ceremony
            model.addRow(new Object[]{"10:00", "Closing Ceremony", "-", "Coordinator", "Multipurpose Hall"});

            add(new JScrollPane(table), BorderLayout.CENTER);

            // Bottom panel with Close + Generate Report button
            JPanel bottomPanel = new JPanel();

            JButton generateReportBtn = new JButton("Generate Award Agenda Report (Excel)");
            generateReportBtn.addActionListener(e -> {
                try {
                    ReportService.generateAwardAgendaReport(model, "Report"); 
                    JOptionPane.showMessageDialog(this,
                            "Award agenda report generated successfully in the Report folder!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Error generating award agenda report: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton closeBtn = new JButton("Close");
            closeBtn.addActionListener(e -> dispose());

            bottomPanel.add(generateReportBtn);
            bottomPanel.add(closeBtn);

            add(bottomPanel, BorderLayout.SOUTH);

            setVisible(true);
        }
    }
}