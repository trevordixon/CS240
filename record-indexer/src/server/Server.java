package server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

@Path("/")
public class Server {

	private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	public static HttpServer start(int port) throws IllegalArgumentException, IOException {
		String URI = "http://0.0.0.0:" + port + "/";
		
		LOGGER.info("Starting server on port " + port);
		
		ResourceConfig rc = new PackagesResourceConfig(
			Server.class.getPackage().getName(),
			database.DB.class.getPackage().getName()
		); 
		
		rc.getResourceFilterFactories().add(AuthFilter.class.getName());
		
		HttpServer server = HttpServerFactory.create(URI, rc);
		server.start();
		
		LOGGER.info("Server listening on port " + port);
		
		return server;
	}
	
	@POST
	@Path("/check_health")
	@Unsecured
	public static String reportHealthy() {
		return "OK";
	}
	
	public static void main(String[] args) {
		int port = 39640;
		
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}

		Logger.getLogger("com.sun.jersey").setLevel(Level.OFF);
		
		try {
			start(port);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Could not start server on port " + port + ": " + e.getMessage());
			//e.printStackTrace();
		}
	}

}
