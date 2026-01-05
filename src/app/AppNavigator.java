package app;

import javax.swing.JFrame;
import view.login.LoginFrame;
import view.student.*;
import view.evaluator.*;
import view.coordinator.*;
import model.*;
import util.SecurityUtil;
import util.DialogUtil;

public class AppNavigator {

    public static void openDashboard(User user){
        if (user.getRole().equals("Student") && SecurityUtil.hasRole("Student")) {
            new StudentDashboard((Student) user);
        }else if (user.getRole().equals("Evaluator") && SecurityUtil.hasRole("Evaluator")) {
            new EvaluatorDashboard((Evaluator) user);
        }else if( user.getRole().equals("Coordinator") && SecurityUtil.hasRole("Coordinator")){
            new CoordinatorDashboard((Coordinator) user);
        }else{
            DialogUtil.showErrorDialog(null, "Unauthorized Access", "You do not have permission to access this dashboard.");
        }
    }

    public static void openAssignedEvaluations(JFrame frame, Evaluator evaluator) {
        frame.dispose();
        new AssignedEvaluationsFrame(evaluator);
    }

    public static void logout(JFrame frame) {
        frame.dispose();
        UserSession.clearSession();
        new LoginFrame();
    }
}