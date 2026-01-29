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

public class UserController {
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
            return true;
        } else {
            return false;
        }

    }

    public static Student getStudentById(int studentId) {
        return UserDAO.getStudentById(studentId);
    }

    public static Coordinator getCoordinatorById(int coordinatorId) {
        return UserDAO.getCoordinatorById(coordinatorId);
    }

    public static Evaluator getEvaluatorById(int evaluatorId) {
        return UserDAO.getEvaluatorById(evaluatorId);
    }

    public static boolean updateUserInfo(User user) {
        return UserDAO.updateUser(user.getUserID(), user.getName(), user.getEmail(), user.getRole());
    }

    public static boolean updateUserInfo(Student student) {
        if(UserDAO.updateUser(student.getUserID(), student.getName(), student.getEmail(), student.getRole()) &&
        UserDAO.updateStudentInfo(student)){
            return true;
        }

        return false;
    }

    public static boolean updateUserInfo(Coordinator coordinator) {
        UserDAO.updateUser(coordinator.getUserID(), coordinator.getName(), coordinator.getEmail(), coordinator.getRole());
        return true;
    }

    public static boolean updateUserInfo(Evaluator evaluator) {
        UserDAO.updateUser(evaluator.getUserID(), evaluator.getName(), evaluator.getEmail(), evaluator.getRole());
        return true;
    }

    public static boolean changePassword(int userId, String oldPassword, String newPassword) {
        String hashedOldPassword = PasswordUtil.hashPassword(oldPassword);
        String hashedNewPassword = PasswordUtil.hashPassword(newPassword);

        boolean isOldPasswordCorrect = UserDAO.checkPassword(userId, hashedOldPassword);
        if(!isOldPasswordCorrect) {
            return false;
        }

        return UserDAO.updatePassword(userId, hashedNewPassword);
    }
}