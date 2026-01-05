package util;

public class EmailUtil {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }
}
