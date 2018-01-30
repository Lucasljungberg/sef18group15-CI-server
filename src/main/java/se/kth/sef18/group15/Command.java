package se.kth.sef18.group15;

import java.lang.Runtime;
import java.lang.Process;
import java.lang.InterruptedException;
import java.io.IOException;

public class Command {

    public static void main (String[] args) {
        System.out.println(Command.execute("asd"));
    }

    /**
     * Executes a terminal command in a seperate process.
     * A non-found command results in an IOException.
     * @param  line the command line to be executed
     * @return      the exit value of the command. -1 if the process was interrupted. 
     *              -2 in case of IOException.
     */
    public static int execute (String line) {
        Runtime rt = Runtime.getRuntime();
        Process pr;
        try {
            pr = rt.exec(line);
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
