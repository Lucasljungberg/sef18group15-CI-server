package se.kth.sef18.group15;

public class GitInfo {
    public final String sshUrl;
    public final String branch;
    public final String buildHash;

    public GitInfo (String sshUrl, String branch, String buildHash) {
        this.sshUrl = sshUrl;
        this.branch = branch;
        this.buildHash = buildHash;
    }
}
