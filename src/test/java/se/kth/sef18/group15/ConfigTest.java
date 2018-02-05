package se.kth.sef18.group15;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.io.FileNotFoundException;

public class ConfigTest {

    private Config config;

    @Before
    public void setUp () {
        this.config = Config.getConfig();
    }

    /**
     * Test-settings is set to login and credentials which should be accessible
     */
    @Test
    public void testLogin () {
        // Username and password should be accessible
        assertNotNull(this.config.getUsername());
        assertNotNull(this.config.getPassword());

        // Username is set to testuser and password is testpass
        assertEquals(this.config.getUsername(), "testuser");
        assertEquals(this.config.getPassword(), "testpass");
    }

    /**
     * The SSH key location should not be accessible because 
     * auth-type is not SSH. It should be null.
     */
    @Test
    public void testInaccessable () {
        try {
            assertNull(this.config.getSshIdLocation());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
     /**
      * The authentication-type should be login
      * as specified in the example settings file
      */
    @Test
    public void testAuthentication(){
        assertEquals(this.config.getAuthenticationType(),Config.AuthenticationType.LOGIN);
    }
     /**
      * The notification-type should be email
      * as specified in the example settings file
      */
    @Test
    public void testNotificationType(){
        assertEquals(this.config.getNotificationType(),Config.NotificationType.EMAIL);
    }
}
