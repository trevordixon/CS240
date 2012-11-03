package server;

import static org.junit.Assert.assertEquals;

import server.Server;
import client.server_communicator.HTTP;
import client.server_communicator.ReqRes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {
	HTTP client;
	
	@Before
	public void setup() throws Exception {
		Server.start(39640);
		client = new HTTP("localhost", 39640);
	}
	
	@After
	public void teardown() { }
	
	@Test
	public void checkHealth() {
		ReqRes r = client.post("check_health", null);		
		assertEquals(200, r.response.getStatus());
		assertEquals("OK", r.body);
	}
}
