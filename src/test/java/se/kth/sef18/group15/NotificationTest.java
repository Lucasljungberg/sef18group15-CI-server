package se.kth.sef18.group15;

import static org.junit.Assert.*;
import org.junit.Test;

public class NotificationTest {

	/**
	 * Cannot perform this test without valid credentials.
	 * So the test will not be performed in the test suite
	 */
	// @Test
	public void testNotification () {
        GitInfo info = new GitInfo();
        info.ref = "master";
        info.after = "c30e09f916e7a2f2a947abc1fb8deb08c91cf72a";
        info.repository.pushed_at = new Long(123123);
        info.repository.ssh_url = "";
        info.repository.statuses_url = "https://api.github.com/repos/lucasljungberg/sef18group15-CI-server/statuses/{sha}";
        info.pusher.name = "";
        GitStatusNotification.SendStatusNotification(info, "failure");
	}
}