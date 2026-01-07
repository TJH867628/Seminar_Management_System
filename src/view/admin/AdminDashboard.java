package view.admin;

import javax.swing.*;
import app.AppNavigator;

import java.awt.*;
import model.Admin;

public class AdminDashboard extends JFrame {
    private Admin admin;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        initialize();
    }

    private void initialize() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + admin.getName() + " (Admin)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton manageUserButton = new JButton("Manage Users");
        manageUserButton.addActionListener(e -> {
            AppNavigator.openManageUser(this, admin);
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> AppNavigator.logout(this));

        JPanel panel = new JPanel(new GridLayout(3,1, 10, 10));
        setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.add(welcomeLabel);
        panel.add(manageUserButton);
        panel.add(logoutButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}