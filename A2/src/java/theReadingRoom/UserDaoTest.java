package theReadingRoom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDaoTest {
    private UserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        userDAO = new UserDAO();
        userDAO.registerUser("testUser", "pass123", "Sithira", "Jayasinghe");
    }

    @Test
    public void testValidLogin() {
        boolean result = userDAO.validateLogin("testUser", "pass123");
        assertTrue("Login should be valid for correct credentials", result);
    }

    @Test
    public void testInvalidLogin() {
        boolean result = userDAO.validateLogin("invalidUser", "wrongPassword");
        assertFalse("Login should be invalid for incorrect credentials", result);
    }

    @Test
    public void testInsertUser() {
        boolean result = userDAO.registerUser("newUser", "newPass123", "New", "User");
        assertTrue("User should be successfully registered", result);
    }

    @Test
    public void testCheckUsernameExists() {
        boolean result = userDAO.checkUsernameExists("testUser");
        assertTrue("Username 'testUser' should exist", result);
    }

    @Test
    public void testCheckUsernameDoesNotExist() {
        boolean result = userDAO.checkUsernameExists("nonExistentUser");
        assertFalse("Username 'nonExistentUser' should not exist", result);
    }

    @After
    public void tearDown() throws Exception {
        userDAO.deleteUser("testUser");
        userDAO.deleteUser("newUser");
    }
}