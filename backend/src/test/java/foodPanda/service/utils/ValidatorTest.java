package foodPanda.service.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {

    public final String VALID_EMAIL = "email@restaurant.com";
    public final String INVALID_EMAIL = "in_valid.email1090-+";
    public final String VALID_PASSWORD = "Admin12345678*";
    public final String INVALID_PASSWORD = "1234";

    @Test
    public void validEmailTest() {
        assertTrue(Validator.getInstance().isEmailValid(VALID_EMAIL));
    }

    @Test
    public void invalidEmailTest() {
        assertFalse(Validator.getInstance().isEmailValid(INVALID_EMAIL));
    }

    @Test
    public void validPasswordTest() {
        assertTrue(Validator.getInstance().isPasswordValid(VALID_PASSWORD));
    }

    @Test
    public void invalidPasswordTest() {
        assertFalse(Validator.getInstance().isPasswordValid(INVALID_PASSWORD));
    }
}
