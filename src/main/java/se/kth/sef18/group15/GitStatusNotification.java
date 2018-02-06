package se.kth.sef18.group15;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Base64;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Sends a POST request with status of commit to Github.
 *
 */
public class GitStatusNotification
{
    /**
     * Sends a commit status notification.
     * Only works with user-pass credentials.
     * @param info   GitInfo object with webhook information
     * @param status one of ["success", "pending", "error", "failure"]
     */
    public static void SendStatusNotification (GitInfo info, String status)
    {
        Config config = Config.getConfig();
        try {
            URL obj = new URL(info.repository.statuses_url.replace("{sha}", info.after));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            String auth = config.getUsername() + ":" + config.getPassword();
            String authEnc = new String(Base64.getEncoder().encode(auth.getBytes()));
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Basic " + authEnc);
            con.setRequestProperty("Content-Type", "application/json; charset=utf8");
            con.setRequestProperty("Content-Length", String.valueOf( authEnc.length() ));
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write((new String("{\"state\": \""+ String.valueOf(status) +"\"}")).getBytes());
            os.flush();
            os.close();
            int response = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot find given url");
        }

    }
}
