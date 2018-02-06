package se.kth.sef18.group15;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.io.File;

public class CommandTest {
    String root;

    @Before
    public void setUp () {
        this.root = System.getProperty("user.dir");
    }


    @Test
    public void testSuccessfulCase () {
        assertEquals(0, Command.execute("echo hi", this.root));
        assertEquals(0, Command.execute("java -version", this.root));
    }

    @Test
    public void testPositiveExitCode () {
        assertEquals(1, Command.execute("cat nonexistentfile.notfound", this.root));
    }

    @Test
    public void testFailedIOCase () {
        assertEquals(-2, Command.execute("thisisnotavalidcommand", this.root));
    }

    @Test
    public void testCommandInDifferentDir () {
        assertEquals(0, Command.execute("ls", ( new File(this.root, "src" ).toString())));
    }

    @Test
    public void testMvnCommand () {
        assertEquals(0, Command.execute("mvn -v", this.root));
    }
}
