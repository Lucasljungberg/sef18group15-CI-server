package se.kth.sef18.group15;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;

import java.util.HashMap;

public class GitHandlerTest {

    private GitInfo info;
    private GitHandler gh;
    private File settings;
    private String old_settings;

    /**
     * Renames the current settings-file to test the different configurations of 
     * Git-setup. Saves a backup settings file into "<project-root>/config/backup_settings.json"
     */
    @Before
    public void setUp () {
        this.info = new GitInfo();

        this.info.ref = "pow";
        this.info.after = "8293ncf972yb9c72b989782y79c2b792";
        this.info.pusher.name = "lucasljungberg";
        this.info.repository.pushed_at = new Long(1778800);
        this.info.repository.ssh_url = "git@github.com:Lucasljungberg/sef18group15-decide.git";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File (System.getProperty("user.dir"), "config/settings.json")));
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File (System.getProperty("user.dir"), "config/backup_settings.json")));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            this.old_settings = sb.toString();
            writer.write(sb.toString());
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.settings = new File(System.getProperty("user.dir"), "config/settings.json");
    }

    /**
     * Writes the current test-settings to the settings-file.
     * @param data HashMap containing key-value pairs to be written into
     *             the settings file
     */ 
    private void writeHashmapToSettings (HashMap<String, String> data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.settings)); 
            writer.write((new Gson()).toJson(data));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing to temp-file. Make sure to rename settings-file in config/");
        }
    }

    /**
     * Tests that the jgit setup work fine without exceptions.
     * If an exception is thrown, the test will fail.
     */
    @Test
    public void testGitSshSetup () {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("auth_type", "ssh");
        data.put("repo_url", "https://github.com/Lucasljungberg/sef18group15-CI-server");
        data.put("ssh_id_location", System.getProperty("user.dir") + "/config/id_rsa");
        this.writeHashmapToSettings(data);
        this.gh = new GitHandler(info);
    }

    /**
     * Tests if jgit works with no authentication setup.
     * If an exception is thrown, the test will fail.
     */
    @Test
    public void testGitPublicSetup () {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("auth_type", "public");
        data.put("repo_url", "https://github.com/Lucasljungberg/sef18group15-CI-server");
        this.writeHashmapToSettings(data);
        this.gh = new GitHandler(info); 
    }

    /**
     * Tests if jgit setup works with user-pass credentials.
     * If an exception is thrown, the test will fail.
     */
    @Test
    public void testGitLoginSetup () {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("auth_type", "login");
        data.put("repo_url", "https://github.com/Lucasljungberg/sef18group15-CI-server");
        data.put("username", "testuser");
        data.put("password", "testpass");
        this.writeHashmapToSettings(data);
        this.gh = new GitHandler(info); 
    }


    /**
     * After testing is done, rewrite the old settings and delete the backup
     */ 
    @After
    public void tearDown () {
        try {
            BufferedWriter writer = new BufferedWriter( 
                new FileWriter (
                    new File(System.getProperty("user.dir"), "config/settings.json")));
            writer.write(this.old_settings);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to rewrite old settings :(");
        }
        (new File(System.getProperty("user.dir"), "config/backup_settings.json")).delete();
        Config.reload();
    }
}
