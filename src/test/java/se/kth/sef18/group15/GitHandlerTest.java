package se.kth.sef18.group15;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.util.FS;

import java.io.File;

public class GitHandlerTest {

    private GitInfo info;
    private GitHandler gh;
    
    @Before
    public void setUp () {
        this.info = new GitInfo(
            "git@github.com:Lucasljungberg/sef18group15-decide.git",
            "master",
            String.valueOf(System.nanoTime()));
        this.gh = new GitHandler(info);
    }

    @Test
    public void testGitFetch () {
        System.out.println("Testing git fetch... may take a few seconds...");
        assertTrue(this.gh.fetch());
    }
}
