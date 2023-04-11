package testLogin;

import loginMain.UserLogin;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;

class UserLoginTest {

    UserLogin userLogin;

    UserLoginTest() throws SQLException {
    }


    @Test
    public void passwordLengthIsShort() {
        boolean result = userLogin.passwordLength("Mmm");
        assertFalse(result);
    }
    @Test
    public void passwordLengthIsNull() {
        boolean result = userLogin.passwordLength("");
        assertFalse(result);
    }
    @Test
    public void passwordLengthIsNormal() {
        boolean result = userLogin.passwordLength("Mmmmm");
        assertFalse(result);
    }
    @Test
    public void passwordIsLowLetters() {
        boolean result = userLogin.passwordLength("mmmmm");
        assertFalse(result);
    }
    @Test
    public void passwordIsCapitalLetters() {
        boolean result = userLogin.passwordLength("MMMMMM");
        assertFalse(result);
    }
}
