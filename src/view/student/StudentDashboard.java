package view.student;

import app.AppNavigator;
import java.awt.*;
import javax.swing.*;
import model.Student;

public class StudentDashboard extends JFrame {

    public StudentDashboard(Student student) {

        setTitle("Student Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel(new GridLayout(3, 1));

        JLabel lblTitle = new JLabel("Student Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel lblName = new JLabel("Welcome, " + student.getName(), SwingConstants.CENTER);
        lblName.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel lblProgram = new JLabel("Program: " + student.getProgram(), SwingConstants.CENTER);
        lblProgram.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnSubmission = new JButton("Create Submission");
        btnSubmission.addActionListener(e -> {
            AppNavigator.openCreateSubmission(this, student);
        });
        JButton btnModify = new JButton("Manage Submissions");
        btnModify.addActionListener(e -> {
            AppNavigator.openManageSubmission(this, student);
        });
        JButton btnManageAccount = new JButton("Manage Account");
        btnManageAccount.addActionListener(e -> {
            AppNavigator.openManageAccount(this, student);
        });
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            AppNavigator.logout(this);
        });


        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.add(btnSubmission);
        panel.add(btnModify);
        panel.add(btnManageAccount);
        panel.add(btnLogout);




        setLayout(new BorderLayout());
        headerPanel.add(lblTitle);
        headerPanel.add(lblName);
        headerPanel.add(lblProgram);
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        
        

        setVisible(true);
    }
}