package controller;

import model.*;
import dao.UserDAO;
import javax.swing.JFrame;
import view.student.StudentDashboard;
import util.PasswordUtil;
import util.EmailUtil;

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

        String role = UserDAO.validateUser(email, hashedPassword);
        System.out.println("Login role: " + role);
        if(role == null || role.equals("invalid"))
        {
            javax.swing.JOptionPane.showMessageDialog(parentFrame,
                    "Invalid email or password",
                    "Login Failed",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        parentFrame.dispose();

        switch(role) {
            case "Student":
                // return new Student(email);
                new StudentDashboard();
            case "Evaluator":
                // return new Evaluator(email);
            case "Coordinator":
                // return new Coordinator(email);
            default:
                return;
        }
        
    }
}