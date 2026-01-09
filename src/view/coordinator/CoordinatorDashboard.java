package view.coordinator;

import javax.swing.*;
import java.awt.*;
import model.Coordinator;
import app.AppNavigator;

public class CoordinatorDashboard extends JFrame {

    public CoordinatorDashboard(Coordinator coordinator) {
        setTitle("Coordinator Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel(new GridLayout(3, 1));

        JLabel lblTitle = new JLabel("Coordinator Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel lblName = new JLabel("Welcome, " + coordinator.getName(), SwingConstants.CENTER);
        lblName.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel lblDepartment = new JLabel("Department: " + coordinator.getDepartment(), SwingConstants.CENTER);
        lblDepartment.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnManageSeminarSession = new JButton("Manage Seminars Session");
        btnManageSeminarSession.addActionListener(e -> AppNavigator.openSeminarSessions(this, coordinator));
        JButton btnAssignES = new JButton("Assign Evaluators & Presenters");
        btnAssignES.addActionListener(e -> {
            AppNavigator.openManageSeminarSessionsAssignments(this, coordinator);
        });
        JButton btnGenerateSchduleAndReport = new JButton("Generate Seminar Schedule & Report");
        JButton btnManageAward = new JButton("Manage Awards");
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            AppNavigator.logout(this);
        });

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.add(btnManageSeminarSession);
        panel.add(btnAssignES);
        panel.add(btnGenerateSchduleAndReport);
        panel.add(btnManageAward);
        panel.add(btnLogout);

        setLayout(new BorderLayout());
        headerPanel.add(lblTitle);
        headerPanel.add(lblName);
        headerPanel.add(lblDepartment);
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }
}
