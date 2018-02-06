package se.kth.sef18.group15;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.StringReader;

public class JsonPackageTest { 

/*
    * A Stripped down json package to run a test.
    *@json A String that contain the critical information.
*/
    String json ="{ 'ref': 'refs/heads/branch', 'after': '1234567890', 'repository': { 'ssh_url': 'https://github.com/User1/Branch', 'pushed_at': 12345, 'statuses_url': 'https://api.github.com/repos/User1/statuses/1234567890' },  'pusher': {'name': 'User1'  } }";



/*
    *Tests that JsonPackage.readURL return an object of converted jsonpackage to java
 */
    GitInfo info = JsonPackage.readURL(new BufferedReader (new StringReader(json)));


/*
    *Tests that the mapping of the nested objects of the jsonpackage is implemented correctly in GitInfo.
    
*/
	@Test
	public void testRef(){
		assertEquals("refs/heads/branch", info.ref);
	}

	@Test
	public void testAfter(){
		assertEquals("1234567890", info.after);
	}

	@Test
	public void testPusherName(){
		assertEquals("User1", info.pusher.name);
	}
	
	@Test
	public void testRepositoryUrl(){
		assertEquals("https://github.com/User1/Branch", info.repository.ssh_url);
	}

	@Test
	public void testRepositoryTime(){
		assertEquals(new Long(12345), new Long(info.repository.pushed_at));
	}


}
