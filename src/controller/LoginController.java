package controller;

import model.*;
import dao.UserDAO;
import app.AppNavigator;

import java.awt.Dialog;

import javax.swing.JFrame;
import util.PasswordUtil;
import util.EmailUtil;
import util.DialogUtil;
import app.*;

public class LoginController {

    public static void login(JFrame parentFrame, String email, String password) {
        if(email == null || email.isEmpty()) return;
        if(!EmailUtil.isValidEmail(email)){
            DialogUtil.showErrorDialog(parentFrame,
                    "Invalid Email",
                    "Please enter a valid email address.");
            return;
        };

        if(password == null || password.isEmpty()) return;

        String hashedPassword = PasswordUtil.hashPassword(password);

        User user = UserDAO.validateUser(email, hashedPassword);
        if(user == null || user.getRole() == null || user.getRole().equals("invalid"))
        {
            DialogUtil.showErrorDialog(parentFrame,
                    "Login Failed",
                    "Invalid email or password. Please try again.");
            return;
        }

        parentFrame.dispose();

        UserSession.setCurrentUser(user);
        AppNavigator.openDashboard(user);
        
    }

    public static boolean register(JFrame parentFrame, String name, String email, String program, int year, String password) {
        String hashedPassword = PasswordUtil.hashPassword(password);

        Student newStudent = new Student(0, email, name, program, year);

        parentFrame.dispose();
        boolean success = UserDAO.createStudent(newStudent, hashedPassword);
        if(success) {
            DialogUtil.showInfoDialog(parentFrame,
                    "Registration Successful",
                    "Your account has been created successfully. You can now log in.");
            parentFrame.dispose();
            AppNavigator.openLoginFrame();
            return true;
        } else {
            DialogUtil.showErrorDialog(parentFrame,
                    "Registration Failed",
                    "An error occurred while creating your account. Please try again.");
            return false;
        }

    }

}