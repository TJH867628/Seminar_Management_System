package view.user;

import model.*;
import javax.swing.*;

import app.AppNavigator;
import controller.UserController;
import dao.UserDAO;

import java.awt.*;

public class ManageAccount extends JFrame {
    private User user;
    JLabel lblUsername, lblEmail, lblChangeUserInfo,lblChangePassword ,lblCurrentPassword, lblNewPassword, lblConfirmPassword;
    JTextField txtUsername, txtEmail, txtCurrentPassword, txtNewPassword, txtConfirmPassword, txtProgram, txtYear;
    JPanel userInfoPanel;
    JButton btnSave, btnBack, btnSaveChangePassword;

    public ManageAccount(User user) {
        this.user = user;
        switch (user.getRole()) {
            case "Coordinator":
                setTitle("Manage Coordinator Account");
                lblChangeUserInfo = new JLabel("CHANGE COORDINATOR INFO");
                break;
            case "Evaluator":
                setTitle("Manage Evaluator Account");
                lblChangeUserInfo = new JLabel("CHANGE EVALUATOR INFO");
                break;
            case "Student":
                setTitle("Manage Student Account");
                lblChangeUserInfo = new JLabel("CHANGE STUDENT INFO");
                break;
            default:
                setTitle("Manage Account");
                lblChangeUserInfo = new JLabel("CHANGE USER INFO");
        }

        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        userInfoPanel = new JPanel(new GridLayout(12, 2, 10, 10));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        userInfoPanel.add(lblChangeUserInfo);
        userInfoPanel.add(new JLabel());
        lblUsername = new JLabel("Username: ");
        lblEmail = new JLabel("Email: ");
        txtUsername = new JTextField(user.getName());
        txtEmail = new JTextField(user.getEmail());
        txtEmail.setEditable(false);

        userInfoPanel.add(lblUsername);
        userInfoPanel.add(txtUsername);
        userInfoPanel.add(lblEmail);
        userInfoPanel.add(txtEmail);

        btnSave = new JButton("Save Info");
        btnSave.addActionListener(e -> saveInfo());
        btnBack = new JButton("Back");
        btnBack.addActionListener(e -> {
            dispose();
            AppNavigator.openDashboard(user);
        });

        handleRoleSpecificInfo();

        userInfoPanel.add(new JLabel());
        userInfoPanel.add(btnSave);

        lblChangePassword = new JLabel("CHANGE PASSWORD");
        userInfoPanel.add(lblChangePassword);
        userInfoPanel.add(new JLabel());

        lblCurrentPassword = new JLabel("Current Password: ");
        lblNewPassword = new JLabel("New Password: ");
        lblConfirmPassword = new JLabel("Confirm Password: ");
        txtCurrentPassword = new JTextField();
        txtNewPassword = new JTextField();
        txtConfirmPassword = new JTextField();
        userInfoPanel.add(lblCurrentPassword);
        userInfoPanel.add(txtCurrentPassword);
        userInfoPanel.add(lblNewPassword);
        userInfoPanel.add(txtNewPassword);
        userInfoPanel.add(lblConfirmPassword);
        userInfoPanel.add(txtConfirmPassword);
        btnSaveChangePassword = new JButton("Save Password");
        btnSaveChangePassword.addActionListener(e -> savePassword());
        userInfoPanel.add(new JLabel());
        userInfoPanel.add(btnSaveChangePassword);
        userInfoPanel.add(new JLabel());
        userInfoPanel.add(btnBack);

        add(userInfoPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    
    public void handleRoleSpecificInfo() {
        String role = user.getRole();

        switch (role) {
            case "Coordinator":
                Coordinator coordinator = UserController.getCoordinatorById(user.getUserID());
                userInfoPanel.add(new JLabel("Department: "));
                userInfoPanel.add(new JLabel(coordinator.getDepartment()));
                break;
            case "Evaluator":
                Evaluator evaluator = UserController.getEvaluatorById(user.getUserID());
                userInfoPanel.add(new JLabel("Expertise Area: "));
                userInfoPanel.add(new JLabel(evaluator.getExpertise()));
                break;
            case "Student":
                Student student = UserController.getStudentById(user.getUserID());
                txtProgram = new JTextField(student.getProgram());
                txtYear = new JTextField(String.valueOf(student.getYear()));
                userInfoPanel.add(new JLabel("Program: "));
                userInfoPanel.add(txtProgram);
                userInfoPanel.add(new JLabel("Year of Study: "));
                userInfoPanel.add(txtYear);
                break;
            default:
                userInfoPanel.add(new JLabel("No specific role information."));
        }

    }

    public void saveInfo() {
        String name = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name and Email cannot be empty",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.setName(name);
        user.setEmail(email);

        boolean success = false;

        switch (user.getRole()) {
            case "Student":
                Student student = UserController.getStudentById(user.getUserID());
                student.setName(name);
                student.setEmail(email);
                student.setProgram(txtProgram.getText().trim());
                student.setYear(Integer.parseInt(txtYear.getText().trim()));
                success = UserController.updateUserInfo(student);
                break;

            case "Coordinator":
                Coordinator coordinator = UserController.getCoordinatorById(user.getUserID());
                coordinator.setName(name);
                coordinator.setEmail(email);
                success = UserController.updateUserInfo(coordinator);
                break;

            case "Evaluator":
                Evaluator evaluator = UserController.getEvaluatorById(user.getUserID());
                evaluator.setName(name);
                evaluator.setEmail(email);
                success = UserController.updateUserInfo(evaluator);
                break;

            default:
                success = UserController.updateUserInfo(user);
        }

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "User information updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update user information",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void savePassword() {
        String currentPassword = new String(txtCurrentPassword.getText());
        String newPassword = new String(txtNewPassword.getText());
        String confirmPassword = new String(txtConfirmPassword.getText());

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All password fields are required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "New password and confirm password do not match",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = UserController.changePassword(
                user.getUserID(),
                currentPassword,
                newPassword
        );

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Password updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            txtCurrentPassword.setText("");
            txtNewPassword.setText("");
            txtConfirmPassword.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Current password is incorrect",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
