package app;

import javax.swing.JFrame;
import view.login.*;
import view.student.*;
import view.evaluator.*;
import view.coordinator.*;
import view.admin.*;
import model.*;
import util.SecurityUtil;
import util.DialogUtil;

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

<<<<<<< Updated upstream
    public static void openManageUser(JFrame frame, Admin admin){
        frame.dispose();
        new ManageUser(admin);
    }

    public static void openAddNewUser(JFrame frame,Admin admin){
        frame.dispose();
        new AddNewUser(admin);
    }

=======
>>>>>>> Stashed changes
    public static void assignEvaluatorsandPresenters(JFrame frame, Session seminarSession, Coordinator coordinator) {
        frame.dispose();
        new AssignEvaluatorsAndPresentersFrame(coordinator);
    }

    public static void logout(JFrame frame) {
        frame.dispose();
        UserSession.clearSession();
        new LoginFrame();
    }
}