package se.kth.sef18.group15;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

import java.lang.IllegalArgumentException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.util.FS;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class GitHandler {

    /**
     * Github information object
     */
    public final GitInfo info;

    /**
     * The Git clone object to handle the build-fetching
     */
     private CloneCommand cc;

    /**
     * Instance of config object to fetch information about
     * authentication and repo
     */
     private Config config;

    /**
     * Creates a new Git-handler for given repository
     * @param info a Github info object for the repository/branch to check
     */
    public GitHandler (GitInfo info) throws IllegalArgumentException {
        this.info = info;
        this.config = Config.getConfig();
        switch (this.config.getAuthenticationType()) {
            case SSH:
                this.gitSshSetup();
                break;
            case LOGIN:
                this.gitLoginSetup();
                break;
            case PUBLIC:
                this.gitPublicSetup();
            default:
                throw new IllegalArgumentException("No authentication type defined");
        }
    }


    /**
     * Fixes Git-authentication with SSH-keys
     */
    private void gitSshSetup () {
        System.out.println("Using ssh..");
        this.cc = Git.cloneRepository();
        this.cc.setURI(this.info.repository.ssh_url);
        this.cc.setBranch(this.info.ref);

        SshSessionFactory ssh = new JschConfigSessionFactory () {

            /**
             * If any transport configurations is needed, this would be where
             * to put them. In this case, there are none needed.
             */
            @Override
            public void configure ( Host host, Session session ) {}

            /**
             * Because we are not using a default private-key indentity path,
             * we need to specify the location of the identity file.
             * In thise case, it is put inside a config directory in the project.
             * In general, that is probably not recommended.
             */
            @Override
            public JSch createDefaultJSch (FS fs) {
                String root = System.getProperty("user.dir");
                JSch defaultJSch = null;
                try {
                    defaultJSch = super.createDefaultJSch(fs);
                    defaultJSch.addIdentity(config.getSshIdLocation().toString());
                } catch (JSchException e) {
                    defaultJSch = null;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.err.println("Cannot find given SSH-identity!");
                }
                return defaultJSch;
            }
        };

        this.cc.setTransportConfigCallback( new TransportConfigCallback () {

            /**
             * Configure the transport using the previously created SSH-identity
             */
            @Override
            public void configure ( Transport transport ) {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory( ssh );
            }
        });
    }

    /**
     * Sets up Git authentication with user-pass combination
     */
    private void gitLoginSetup () {
        this.cc = Git.cloneRepository();
        this.cc.setURI(this.info.repository.ssh_url);
        this.cc.setBranch(this.info.ref);
        this.cc.setCredentialsProvider(
            new UsernamePasswordCredentialsProvider(
                this.config.getUsername(), this.config.getPassword()));
    }

    /**
     * Sets up Git fetching where no authentication is needed
     */
    private void gitPublicSetup () {
        this.cc = Git.cloneRepository();
        this.cc.setURI(this.info.repository.ssh_url);
        this.cc.setBranch(this.info.ref);
    }

    /**
     * Fetches the build and saves in the target directory as a dir
     * with the GitInfo's hash value as directory name.
     * Will fetch to <project root>/builds/<buildHash>
     * @return true if no errors, false otherwise
     */
    public boolean fetch () {
        String rootDir = System.getProperty("user.dir");
        File buildDir = new File(rootDir, "builds");
        return this.fetch(buildDir);
    }

    /**
     * Fetches the build and saves in the target directory as a dir
     * with the GitInfo's hash value as directory name.
     * @param targetDir the directory to fetch the build to such that
     *                  it is stored in <targetDir>/<buildHash>
     * @return          true if no errors, false otherwise
     */
    public boolean fetch (File targetDir) {
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }

        File buildDir = new File (targetDir.toString(), this.info.after);
        if (buildDir.exists()) {
            buildDir = new File(buildDir.toString() + new String("-" + System.nanoTime()));
        }

        this.cc.setDirectory(buildDir);
        try {
            cc.call();
            return true;
        } catch (GitAPIException e) {
            return false;
        }
    }
}
