package se.kth.sef18.group15;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

public class ConfigTest {

    private Config config;
    private HashMap<String, String> data;

    @Before
    public void setUp () {
        this.config = Config.getConfig();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                new FileReader(
                    new File(System.getProperty("user.dir"), "config/settings.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        this.data = (new Gson()).fromJson(reader, type);
    }

    /**
     * Test-settings is set to login and credentials which should be accessible
     */
    @Test
    public void testLoginAuth () {
        if (this.data.containsKey("auth_type")) {
            if (this.data.get("auth_type").toLowerCase().equals("login")) {
                // If given auth_type is LOGIN, getAuthenticationType() should
                // return that.
                assertEquals(Config.AuthenticationType.LOGIN, this.config.getAuthenticationType());

                // There should also be accessible username and password fields
                assertEquals(this.config.getUsername(), this.data.get("username"));
                assertEquals(this.config.getPassword(), this.data.get("password"));
            } else {
                // Because given authentication-type is LOGIN, SSH-identity location
                // should be inaccessible. A missing field is also okay,
                // which is indicated by a FileNotFoundException.
                try {
                    assertNull(this.config.getSshIdLocation());
                } catch (FileNotFoundException e) {
                    // passed
                }
            }
        }
    }

    /**
     * The SSH key location should not be accessible because
     * auth-type is not SSH. It should be null.
     */
    @Test
    public void testSshAuth() {
        if (this.data.containsKey("auth_type")) {
            if (this.data.get("auth_type").toLowerCase().equals("ssh")) {
                // If given authentication-type is SSH, the path should either be
                // the default or the given Path
                if (this.data.containsKey("ssh_id_location")) {
                    try {
                        assertEquals(this.config.getSshIdLocation().toString(),
                            this.data.get("ssh_id_location"));
                    } catch (FileNotFoundException e) {
                        // Assert false if file isn't found
                        assertTrue(false);
                    }

                } else {
                    try {
                        assertEquals(this.config.getSshIdLocation().toString(),
                            (new File(System.getProperty("user.home"), ".ssh/id_rsa")));
                    } catch (FileNotFoundException e) {
                        // Assert false if file isn't found
                        assertTrue(false);
                    }

                }
            }
        }
    }

    /**
     * Testing values for email credentials. If the chosen notification-type is
     * email, then email credentials should be defined and accessible.
     */
    @Test
    public void testEmailNotificationType () {
        if (this.data.containsKey("notification_type")) {
            if (this.data.get("notification_type").toLowerCase().equals("email")) {
                // If notification_type is set to email, the returned notification-type
                // should be NotificationType.EMAIL
                assertEquals(this.config.getNotificationType(),
                    Config.NotificationType.EMAIL);

                // The email-credentials should also be accessible and equal to
                // the values in the settings file
                assertEquals(this.config.getEmailSender(),
                    this.data.get("email_sender"));
                assertEquals(this.config.getEmailPassword(),
                    this.data.get("email_password"));
            }
        }
    }

    /**
     * If the given notification-type is COMMITSTATUS, then that should be reflected
     * in the config object. The Email credentials should also not be accessible.
     */
    @Test
    public void testCommitStatusNotificationType () {
        if (this.data.containsKey("notification_type")) {
            if (this.data.get("notification_type").toLowerCase().equals("commitstatus")) {
                assertEquals(this.config.getNotificationType(),
                    Config.NotificationType.COMMITSTATUS);

                // Email credentials should also not be accessible
                assertNull(this.config.getEmailSender());
                assertNull(this.config.getEmailPassword());
            }
        }
    }
}
