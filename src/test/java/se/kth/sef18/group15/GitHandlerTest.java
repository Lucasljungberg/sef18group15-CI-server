package se.kth.sef18.group15;

import org.junit.Test;
public class GitHandlerTest {

    private GitInfo info;
    private GitHandler gh;

    /**
     * Tests that the jgit setup work fine without exceptions.
     * If an exception is thrown, the test will fail.
     */
    @Test
    public void testGitSetup () {
        this.info = new GitInfo(
            "git@github.com:Lucasljungberg/sef18group15-decide.git",
            "master",
            String.valueOf(System.nanoTime()));
        this.gh = new GitHandler(info);
    }
}
