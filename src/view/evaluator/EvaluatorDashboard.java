package view.evaluator;

import javax.swing.*;
import java.awt.*;

import app.AppNavigator;
import model.Evaluator;

public class EvaluatorDashboard extends JFrame {
    public EvaluatorDashboard(Evaluator evaluator) {
        setTitle("Evaluator Dashboard");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel(new GridLayout(3, 1));

        JLabel lblTitle = new JLabel("Evaluator Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel lblName = new JLabel("Welcome, " + evaluator.getName(), SwingConstants.CENTER);
        lblName.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel lblDepartment = new JLabel("Expertise: " + evaluator.getExpertise(), SwingConstants.CENTER);
        lblDepartment.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnAssignEvaluations = new JButton("Assigned Evaluations");
        btnAssignEvaluations.addActionListener(e -> {
            AppNavigator.openAssignedEvaluations(this, evaluator);
        });

        JButton btnStudentVotes = new JButton("Students Projects Voting");
        btnStudentVotes.addActionListener(e -> {
            // Open the voting frame
            new StudentProjectVotesFrame(evaluator);
            // Close current dashboard to prevent multiple windows
            this.dispose();
        });

        JButton btnManageAccount = new JButton("Manage Account");
        btnManageAccount.addActionListener(e -> {
            AppNavigator.openManageAccount(this, evaluator);
        });

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            AppNavigator.logout(this);
        });

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.add(btnAssignEvaluations);
        panel.add(btnStudentVotes);
        panel.add(btnManageAccount);
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
