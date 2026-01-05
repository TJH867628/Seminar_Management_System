package util;

import app.UserSession;
import model.User;

public class SecurityUtil {
    public static boolean hasRole(String requiredRole) {
        User currentUser = UserSession.getCurrentUser();
        return currentUser != null && currentUser.getRole().equals(requiredRole);
    }
}
