package foodPanda.service.utils;

import java.util.regex.Pattern;

/**
 * A singleton class that validates inputs such as email and password
 */
public class Validator {

    private static Validator instance = new Validator();

    private Validator() {
    }

    /**
     * The only who can help us use the Validator class
     *
     * @return The singleton instance of the Validator class
     */
    public static Validator getInstance() {
        return instance;
    }

    /**
     * The regex that is used to validate an email
     */
    String emailRegex = "^(.+)@(.+)$";
    Pattern emailPattern = Pattern.compile(emailRegex);

    /**
     * The regex that is used to validate a password
     */
    String passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$";
    Pattern passwordPattern = Pattern.compile(passwordRegex);

    /**
     * Validates an email
     *
     * @param email The email that is checked for validation
     * @return true if the email is valid of false otherwise
     */
    public boolean isEmailValid(String email) {
        return emailPattern.matcher(email).matches();
    }

    /**
     * Validates a password
     *
     * @param password The password that is checked for validation
     * @return true if the password is valid of false otherwise
     */
    public boolean isPasswordValid(String password) {
        return passwordPattern.matcher(password).matches();
    }
}
