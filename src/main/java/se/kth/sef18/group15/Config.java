package se.kth.sef18.group15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Config helper class that loads settings for the server.
 * All settings have been initialized to default values.
 * This class only has one instance.
 */
public class Config {

    /**
     * The single instance of Config
     */
    private static Config instance;

    /**
     * Types of authentication for Git
     */
    public enum AuthenticationType {
        PUBLIC,
        LOGIN,
        SSH
    }
    public enum NotificationType {
        NONE,
        EMAIL,
        COMMITSTATUS   
    }

    /**
     * Chosen authentication-type and notification-type
     */
    private AuthenticationType authType = AuthenticationType.PUBLIC;
    private NotificationType notType = NotificationType.NONE;

    /**
     * File-object targeting the private SSH-identity.
     * Only fetchable if authentication type is SSH.
     */
    private File sshID = new File("~/.ssh/id_rsa");

    /**
     * Given username and password for user-pass authentication method.
     * Only fetchable if authentication type is user-pass pair.
     */
    private String username = "";
    private String password = "";

    /**
     * URL to the Git repo
     */
    private String repoUrl = "";

    /**
     * Path to settings-file
     */
    private final File settingsPath = new File ( System.getProperty("user.dir"), "config/settings.json");

    /**
     * Sets the default constructor to private to prevent new instances
     * from being created.
     * Loads the json-formatted file from config/settings.json
     */
    private Config () {
        if ( this.settingsPath.exists() ) {
            this.loadSettingsFromFile();
        }
    }

    /**
     * @return a file-descriptor of the given SSH private key file.
     *         If the authentication-type is not SSH, null is returned.
     */
    public File getSshIdLocation () throws FileNotFoundException {
        if (this.authType != AuthenticationType.SSH) return null;
        else if (!this.sshID.exists()) throw new FileNotFoundException("Given location of private-key not found");
        else return this.sshID;
    }

    /**
     * Returns the username iff the authType is LOGIN
     * @return username from the settings file
     *         null if authType is not LOGIN
     */
    public String getUsername () {
        if (this.authType == AuthenticationType.LOGIN) return this.username;
        else return null;
    }

    /**
     * Returns the password iff the authType is LOGIN
     * @return password from the settings file
     *         null if authType is not LOGIN
     */
    public String getPassword () {
        if (this.authType == AuthenticationType.LOGIN) return this.password;
        else return null;
    }

    /**
     * Returns the repository URL that is defined in the settings file
     * @return the URL to the repository
     */
    public String getRepoUrl () {
        return this.repoUrl;
    }
    /**
     * Return Authentication type
     * @return returns current set authentication-type or PUBLIC if unspecified
     */
    public AuthenticationType getAuthenticationType(){
        return this.authType;
    }
    /**
     * Return the notification-type
     * @return current set notification type or NONE if unspecified
     */
    public NotificationType getNotificationType(){
        return this.notType;
    }

    /**
     * Loads settings from settings-file
     */
    private void loadSettingsFromFile () {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(this.settingsPath));
        } catch (FileNotFoundException e) {
            System.err.println("Settings file could not be found. Using default values.");
            return;
        }

        Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> json = (new Gson()).fromJson(reader, type );

        this.loadSshLocation(json);
        this.loadAuthenticationType(json);
        try {
            this.loadCredentials(json);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.loadNotificationType(json);
        this.loadGitUrl(json);
    }

    /**
     * Loads the location of the given SSH private key
     * @param json HashMap containing key-value pair from the settings-file
     */
    private void loadSshLocation (HashMap<String, String> json) {
        if (json.containsKey("ssh_id_location")) {
            this.sshID = new File(json.get("ssh_id_location"));
        } 
    }

    /**
     * Loads the given authentication-type
     * @param json HashMap containing key-value pair from the settings-file
     */
    private void loadAuthenticationType (HashMap<String, String> json) {
        if (json.containsKey("auth_type")) {
            switch (json.get("auth_type").toLowerCase()) {
                case "ssh":
                    this.authType = AuthenticationType.SSH;
                    break;
                case "login":
                    this.authType = AuthenticationType.LOGIN;
                    break;
                case "public":
                    this.authType = AuthenticationType.PUBLIC;
                    break;
            }
        }
    }
    /**
     * Loads the given notification-type
     * @param json HashMap containing key-value pair from the settings-file     
     */
    private void loadNotificationType(HashMap<String,String> json) throws IllegalArgumentException {
        if(json.containsKey("notification_type")){
            switch(json.get("notification_type").toLowerCase()){
                case "none":
                    this.notType = NotificationType.NONE;
                    break;
                case "email":
                    this.notType = NotificationType.EMAIL;
                    break;
                case "commitstatus":
                    if (this.getAuthenticationType() != AuthenticationType.LOGIN) {
                        throw new IllegalArgumentException("User-pass credentials needed for commit-status notification");    
                    }
                    this.notType = NotificationType.COMMITSTATUS;
                    break;
            }
        }
    }

    /**
     * Loads login-credentials
     * @param json HashMap containing key-value pair from the settings-file
     */
    private void loadCredentials (HashMap<String, String> json) {
        if (json.containsKey("username") && json.containsKey("password")) {
            this.username = json.get("username");
            this.password = json.get("password");
        }
    }

    /**
     * Loads the given Git repo url
     * @param json HashMap containing key-value pair from the settings-file
     */
    private void loadGitUrl (HashMap<String, String> json) {
        if (json.containsKey("repo_url")) {
            this.repoUrl = json.get("repo_url");
        }
    }

    /**
     * Create the instance at class-load time
     */
    static {
        instance = new Config();
    }

    /**
     * Reloads the config file by re-reading information from 
     * the settings file
     */
    public static void reload () {
        instance = new Config(); 
    }

    /**
     * Retrieve the single config instance
     */
    public static Config getConfig () {
        return instance;
    }
}
