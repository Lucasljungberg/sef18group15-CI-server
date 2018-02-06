package se.kth.sef18.group15;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.io.FileNotFoundException;

public class SendEmailTest {

    private SendEmail sendEmail;

    @Before
    public void setUp () {
        this.sendEmail = new SendEmail();
    }

    /**
     * Tests the function sendEmail.
     * contract: An email is sent to olzhas.kadyrakunov@gmail.com
     * Credentials were removed when pushing to the repository as
     * it is not good to have credentials uploaded.
     */
    // @Test
    public void testSendEmail () {
        String USER_NAME = "";
        String PASSWORD = "";
        String[] RECIPIENT = {"olzhas.kadyrakunov@gmail.com"};
        String subject = "SendEmail test";
        String body = "Passed the testcase!";
        this.sendEmail.sendEmail(USER_NAME, PASSWORD, RECIPIENT, subject, body);
    }
}
