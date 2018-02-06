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
     */
    @Test
    public void testSendEmail () {
        String USER_NAME = "onekaist@gmail.com";  // GMail user name (just the part before "@gmail.com")
        String PASSWORD = "kai123454"; // GMail password
        String[] RECIPIENT = {"olzhas.kadyrakunov@gmail.com"};
        String subject = "SendEmail test";
        String body = "Passed the testcase!";
        this.sendEmail.sendEmail(USER_NAME, PASSWORD, RECIPIENT, subject, body);
    }
}

