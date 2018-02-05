package se.kth.sef18.group15;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class GitHandlerTest {

    private GitInfo info;
    private GitHandler gh;

    @Before
    public void setUp () {
        this.info = new GitInfo();

        this.info.ref = "pow";
        this.info.after = "8293ncf972yb9c72b989782y79c2b792";
        this.info.pusher.name = "lucasljungberg";
        this.info.repository.pushed_at = new Long(1778800);
        this.info.repository.ssh_url = "git@github.com:Lucasljungberg/sef18group15-decide.git";
    }

    /**
     * Tests that the jgit setup work fine without exceptions.
     * If an exception is thrown, the test will fail.
     */
    @Test
    public void testGitSetup () {
        this.gh = new GitHandler(info);
    }
}
