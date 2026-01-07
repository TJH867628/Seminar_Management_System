package view.login;

import javax.swing.*;
import java.awt.*;
import java.time.Year;

import controller.LoginController;
import util.DialogUtil;

public class RegisterFrame extends JFrame{
    private JTextField txtName,txtEmail;
    private JPasswordField txtPassword,txtConfirmPassword;
    private JButton btnRegister;
    private JComboBox<String> cmbProgram;
    private JSpinner spinnerYear;

    private String[] programs = {
        "Software Engineering",
        "Computer Science",
        "Information Technology",
        "Data Science"
    };

    public RegisterFrame() {
        setTitle("Seminar Management System - Register");
        setSize(400, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        cmbProgram = new JComboBox<>(programs);

        panel.add(new JLabel("Name:"));
        txtName = new JTextField();
        panel.add(txtName);
        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);
        panel.add(new JLabel("Program:"));
        panel.add(cmbProgram);
        panel.add(new JLabel("Year of Study:"));
        spinnerYear = new JSpinner(new SpinnerNumberModel(Year.now().getValue(), 2000, 2100, 1));
        panel.add(spinnerYear);
        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);
        panel.add(new JLabel("Confirm Password:"));
        txtConfirmPassword = new JPasswordField();
        panel.add(txtConfirmPassword);

        panel.add(new JLabel());
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(e -> {
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String program = (String) cmbProgram.getSelectedItem();
            int year = (int) spinnerYear.getValue();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            if(name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                DialogUtil.showErrorDialog(this, "Register Error","Please fill in all required fields.");
                return;
            }

            if(!password.equals(confirmPassword)) {
                DialogUtil.showErrorDialog(this, "Register Error","Passwords do not match.");
                return;
            }

            boolean success = LoginController.register(this, name, email, program, year, password);
        });
        panel.add(btnRegister);

        add(panel);
        setVisible(true);
    }

    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        LoginController.login(this, email, password);
    }
}
