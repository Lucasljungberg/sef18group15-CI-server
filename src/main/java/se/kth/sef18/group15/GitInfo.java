
import com.google.gson.Gson;
import java.io.BufferedReader;

public class GitInfo{
	String ref;
	String after;
	Repository repository;
	Pusher pusher;


class Pusher{
	String name;
}


class Repository{
		Long pushed_at;
		String ssh_url;
	}


/**
     * Returns a string representation of the object as key-value pairs
     * @return a string as key-value pairs
     */
    public String toString () {
        return String.format("{'ref': '%s', 'after': '%s', 'ssh_url': '%s', 'pushed_at': %d, 'name': '%s'}",
            this.ref, this.after, this.repository.ssh_url, 
            this.repository.pushed_at, this.pusher.name
        );
    }


}
