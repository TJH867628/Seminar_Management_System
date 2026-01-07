package controller;

import dao.UserDAO;
import model.Student;
import model.*;
import util.PasswordUtil;

import java.util.List;

public class AdminController {

    private UserDAO userDAO;

    public AdminController() {
        this.userDAO = new UserDAO();
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean updateUser(int userID,String name,String email,String role){
        return userDAO.updateUser(userID,name,email,role);
    }

    public boolean createNewStudent(String name, String email, String password, String program, int year){
        String hashedPassword = PasswordUtil.hashPassword(password);

        Student student = new Student(year, email, name, program, year); 

        return UserDAO.createStudent(student, hashedPassword);
    }

    public boolean createNewEvaluator(String name, String email, String password, String expertise){
        String hashedPassword = PasswordUtil.hashPassword(password);

        Evaluator evaluator = new Evaluator(0, email, name, 0, expertise);

        return UserDAO.createEvaluator(evaluator, hashedPassword);
    }

    public boolean createNewCoordinator(String name, String email, String password, String department){
        String hashedPassword = PasswordUtil.hashPassword(password);

        Coordinator coordinator = new Coordinator(0, email, name, 0 , department);

        return UserDAO.createCoordinator(coordinator, hashedPassword);
    }
}