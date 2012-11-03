package server;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.parsers.ParserConfigurationException;

import server.Server;
import client.server_communicator.HTTP;
import client.server_communicator.ReqRes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

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
	
	public void testImport() throws IOException, SAXException, ParserConfigurationException {
		Files.copy(
			new File("database/indexer_server_empty.sqlite").toPath(),
			new File("database/indexer_server.sqlite").toPath()
		);

		server.Import.main(new String[]{"test/server/indexer_data/Records/Records.xml"});
	}
}
