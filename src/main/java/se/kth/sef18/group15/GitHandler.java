package se.kth.sef18.group15;

import java.io.File;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
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
     * Creates a new Git-handler for given repository
     * @param info a Github info object for the repository/branch to check
     */
    public GitHandler (GitInfo info) {
        this.info = info;
        this.gitSetup();
    }

    private void gitSetup () {
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
                JSch defaultJSch;
                try {
                    defaultJSch = super.createDefaultJSch(fs);
                    defaultJSch.addIdentity((new File(root)).toPath().resolve("config").resolve("id_rsa").toString());
                } catch (JSchException e) {
                    defaultJSch = null;
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

        this.cc.setDirectory(new File(targetDir.toString(), this.info.after));
        try {
            cc.call();
            return true;
        } catch (GitAPIException e) {
            return false;
        }
    }
}
