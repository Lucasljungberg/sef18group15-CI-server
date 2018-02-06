package se.kth.sef18.group15;

import java.lang.Runtime;
import java.lang.Process;
import java.lang.InterruptedException;
import java.io.IOException;
import java.io.File;

/**
 * Class to handle shell-commands.
 */
public class Command {

    public static void main (String[] args) {
        System.out.println(Command.execute("mvn -v", System.getProperty("user.dir")));
    }

    /**
     * Executes a terminal command in a seperate process.
     * A non-found command results in an IOException.
     * @param  line   the command line to be executed
     * @param  target the directory from which to exectue the command
     * @return      the exit value of the command. -1 if the process was interrupted.
     *              -2 in case of IOException.
     */
    public static int execute (String line, String target) {
        File targetDir = new File(target);
        Runtime rt = Runtime.getRuntime();
        Process pr;
        try {
            pr = rt.exec(line, null, targetDir);
        } catch (IOException e) {
            pr = null;
        }

        try {
            return pr.waitFor();
        } catch (InterruptedException e) {
            System.err.println("InterruptedException was thrown. This may be due to threading-issues "
                + "or the process being manually interrupted." +
                "Run an additional time to make sure it's an actual issue.");
            return -1;
        } catch (NullPointerException e) {
            return -2;
        }
    }

}
