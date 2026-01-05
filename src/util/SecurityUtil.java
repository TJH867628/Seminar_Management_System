package util;

import app.Session;
import model.User;

public class SecurityUtil {
    public static boolean hasRole(String requiredRole) {
        User currentUser = Session.getCurrentUser();
        return currentUser != null && currentUser.getRole().equals(requiredRole);
    }
}
