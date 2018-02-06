package se.kth.sef18.group15;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
public class ContinuousIntegrationServer extends AbstractHandler{



    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println("Parsing request data..");
    	GitInfo info = JsonPackage.readURL(request.getReader());
        File targetDir = new File (
            System.getProperty("user.dir"), "builds/" + info.after);

        System.out.println("Setting up git-handling..");
        GitHandler gh = new GitHandler(info);

        System.out.println("Fetching project");
        gh.fetch();

        System.out.println("Attempting to compile project..");
        int compileCode = Command.execute("mvn compile", targetDir.toString());

        System.out.println("Testing project..");
        int testCode = Command.execute("mvn test", targetDir.toString());

        System.out.println("Done!");

        response.getWriter().println("CI job done.");
        if (compileCode != 0) {
            response.getWriter().println("Compiling failed..");
            System.out.println("Compiling failed..");
        } else {
            response.getWriter().println("Compiling succeeded!");
            System.out.println("Compiling succeeded!");
        }

        if (testCode != 0) {
            response.getWriter().println("Testing failed..");
            System.out.println("Testing failed..");
        } else {
            response.getWriter().println("Testing successful!");
            System.out.println("Testing succeeded!");
        }

        Config config = Config.getConfig();

        switch (config.getNotificationType()) {
            case EMAIL:
                SendEmail se = new SendEmail();
                String message = "";
                if (testCode == 0 && compileCode == 0) {
                    message = "The test results were successful!";
                } else {
                    message = "The tests were unsuccessful";
                }
                se.sendEmail(config.getEmailSender(), config.getEmailPassword(),
                    new String[]{info.pusher.email}, "CI-Server result", message);
                break;
            case COMMITSTATUS:
                GitStatusNotification.SendStatusNotification(info,
                    (testCode == 0 && compileCode == 0) ? "success" : "error");
                break;
        }
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
