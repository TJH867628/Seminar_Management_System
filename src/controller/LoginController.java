package controller;

import model.*;
import dao.UserDAO;
import javax.swing.JFrame;
import util.PasswordUtil;
import util.EmailUtil;
import app.AppNavigator;

public class LoginController {

    public static void login(JFrame parentFrame, String email, String password) {
        if(email == null || email.isEmpty()) return;
        if(!EmailUtil.isValidEmail(email)){
            javax.swing.JOptionPane.showMessageDialog(parentFrame,
                    "Please enter a valid email address",
                    "Invalid Email",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        };

        if(password == null || password.isEmpty()) return;

        String hashedPassword = PasswordUtil.hashPassword(password);

        User user = UserDAO.validateUser(email, hashedPassword);
        if(user.getRole() == null || user.getRole().equals("invalid"))
        {
            javax.swing.JOptionPane.showMessageDialog(parentFrame,
                    "Invalid email or password",
                    "Login Failed",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        parentFrame.dispose();
        AppNavigator.openDashboard(user);
        
    }
}