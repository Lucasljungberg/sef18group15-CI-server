package se.kth.sef18.group15;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommandTest {

    @Test
    public void testSuccessfulCase () {
        assertEquals(0, Command.execute("echo hi"));
        assertEquals(0, Command.execute("java -version"));
    }

    @Test
    public void testPositiveExitCode () {
        assertEquals(1, Command.execute("cat nonexistentfile.notfound"));
    }

    @Test
    public void testFailedIOCase () {
        assertEquals(-2, Command.execute("thisisnotavalidcommand"));
    }
}
