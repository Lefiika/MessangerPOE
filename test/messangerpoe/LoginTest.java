package messangerpoe;

import org.junit.Test;
import static org.junit.Assert.*;


public class LoginTest {

    // Test (assertTrue/False) — the three boolean checker methods

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkUserName("kyle!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexity() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testLoginSuccess() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailure() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertFalse(login.loginUser("kyl_1", "wrongPassword"));
    }

    // Test (assertEquals) — Login flow: returnLoginStatus()

    @Test
    public void testReturnLoginStatus_UsernameCorrectlyFormatted() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean loggedIn = login.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kyle, Smith it is great to see you.", login.returnLoginStatus(loggedIn));
    }

    @Test
    public void testReturnLoginStatus_UsernameIncorrectlyFormatted() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean loggedIn = login.loginUser("kyle!!!!!!", "Ch&&sec@ke99!");
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(loggedIn));
    }

    // Test (assertEquals) — Registration flow: registerUser()

    @Test
    public void testRegisterUser_UsernameIncorrectlyFormatted() {
        Login login = new Login("kyle!!!!!!", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals(
            "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
            login.registerUser()
        );
    }

    @Test
    public void testRegisterUser_PasswordDoesNotMeetComplexity() {
        Login login = new Login("kyl_1", "password", "+27838968976", "Kyle", "Smith");
        assertEquals(
            "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
            login.registerUser()
        );
    }

    @Test
    public void testRegisterUser_CellPhoneIncorrectlyFormatted() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "08966553", "Kyle", "Smith");
        assertEquals(
            "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
            login.registerUser()
        );
    }

    @Test
    public void testRegisterUser_AllFieldsValid_ReturnsSuccessMessage() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals(
            "Username, password, and cell phone number successfully captured.",
            login.registerUser()
        );
    }
}
