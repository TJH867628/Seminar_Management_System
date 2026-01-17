package app;

import javax.swing.JFrame;
import model.*;
import util.DialogUtil;
import util.SecurityUtil;
import view.admin.*;
import view.coordinator.*;
import view.evaluator.*;
import view.login.*;
import view.student.*;
import view.user.*;

public class AppNavigator{

    public static void openLoginFrame(){
        new LoginFrame();
    }
    
    public static void openDashboard(User user){
        if (user.getRole().equals("Student") && SecurityUtil.hasRole("Student")){
            new StudentDashboard((Student) user);
        }else if (user.getRole().equals("Evaluator") && SecurityUtil.hasRole("Evaluator")){
            new EvaluatorDashboard((Evaluator) user);
        }else if( user.getRole().equals("Coordinator") && SecurityUtil.hasRole("Coordinator")){
            new CoordinatorDashboard((Coordinator) user);
        }else if( user.getRole().equals("Admin") && SecurityUtil.hasRole("Admin")){
            new AdminDashboard((Admin) user);
        }else{
            DialogUtil.showErrorDialog(null, "Unauthorized Access", "You do not have permission to access this dashboard.");
        }
    }

    public static void openRegisterFrame(JFrame frame){
        frame.dispose();
        new RegisterFrame();
    }

    public static void openAssignedEvaluations(JFrame frame, Evaluator evaluator){
        frame.dispose();
        new AssignedEvaluationsFrame(evaluator);
    }

    public static void openEvaluationForm(JFrame frame, AssignedEvaluation assignedEvaluation, Evaluator evaluator){
        frame.dispose();
        new EvaluationForm(assignedEvaluation, evaluator);
    }

    public static void openSeminarSessions(JFrame frame, Coordinator coordinator){
        frame.dispose();
        new SeminarSessions(coordinator);
    }

    public static void openAddSeminarSession(JFrame frame, Coordinator coordinator){
        frame.dispose();
        new AddNewSeminarSession(coordinator);
    }

    public static void openEditSeminarSession(JFrame frame, Coordinator coordinator, int sessionID){
        frame.dispose();
        new EditSeminarSession(coordinator, sessionID);
    }

    public static void openManageUser(JFrame frame, Admin admin){
        frame.dispose();
        new ManageUser(admin);
    }

    public static void openAddNewUser(JFrame frame,Admin admin){
        frame.dispose();
        new AddNewUser(admin);
    }

    public static void openManageSeminarSessionsAssignments(JFrame frame, Coordinator coordinator){
        frame.dispose();
        new SessionAssignmentTable(coordinator);
    }

    public static void openManageSeminarSessionsAssignmentsDetails(JFrame frame, Coordinator coordinator, int sessionID){
        frame.dispose();
        new SessionAssignmentDetails(coordinator, sessionID);
    }

    public static void logout(JFrame frame) {
        frame.dispose();
        UserSession.clearSession();
        new LoginFrame();
    }

    public static void openManageAccount(JFrame frame, User user) {
        frame.dispose();
        new ManageAccount(user);
    }

    public static void openCreateSubmission(JFrame frame, Student student) {
        frame.dispose();
        new CreateSubmission(student);
    }

    public static void openManageSubmission(JFrame frame, Student student) {
        frame.dispose();
        new ManageSubmission(student);
    }
}