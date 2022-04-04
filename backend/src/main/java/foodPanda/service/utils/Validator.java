package foodPanda.service.utils;

import java.util.regex.Pattern;

public class Validator {

    private static Validator instance = new Validator();

    private Validator() {
    }

    public static Validator getInstance() {
        return instance;
    }

    String emailRegex = "^(.+)@(.+)$";
    Pattern emailPattern = Pattern.compile(emailRegex);

    String passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$";
    Pattern passwordPattern = Pattern.compile(passwordRegex);

    public boolean isEmailValid(String email) {
        return emailPattern.matcher(email).matches();
    }

    public boolean isPasswordValid(String password) {
        return passwordPattern.matcher(password).matches();
    }
}
