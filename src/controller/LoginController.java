package controller;

import model.*;
import dao.UserDAO;

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
}