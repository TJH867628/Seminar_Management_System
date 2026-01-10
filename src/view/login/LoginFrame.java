package view.login;

import javax.swing.*;

import app.AppNavigator;

import java.awt.*;
import controller.UserController;
import model.User;

public class LoginFrame extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    public LoginFrame() {
        setTitle("Seminar Management System - Login");
        setSize(400, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(e -> {
            AppNavigator.openRegisterFrame(this);
        });
        btnLogin.addActionListener(e -> handleLogin());
        panel.add(btnRegister);
        panel.add(btnLogin);

        JLabel lblLoginNotice = new JLabel("Please enter your email and password registered to login", SwingConstants.CENTER);
        lblLoginNotice.setFont(new Font("Arial", Font.ITALIC, 12));
        lblLoginNotice.setForeground(Color.RED);
        add(lblLoginNotice, BorderLayout.NORTH);
        add(panel);
        setVisible(true);
    }

    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        UserController.login(this, email, password);
    }
}