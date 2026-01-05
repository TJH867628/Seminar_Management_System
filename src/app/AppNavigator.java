package app;

import javax.swing.JFrame;
import view.login.LoginFrame;
import view.student.*;
import view.evaluator.*;
import view.coordinator.*;
import model.*;

public class AppNavigator {

    public static void openDashboard(User user){
        if (user.getRole().equals("Student")) {
            new StudentDashboard((Student) user);
        }else if (user.getRole().equals("Evaluator")) {
            new EvaluatorDashboard((Evaluator) user); // placeholder
        }else if( user.getRole().equals("Coordinator")) {
            new CoordinatorDashboard((Coordinator) user); // placeholder
        }
    }

    public static void logout(JFrame frame) {
        frame.dispose();
        new LoginFrame();
    }
}