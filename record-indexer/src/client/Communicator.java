package client;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class Communicator {
	private static String username;
	private static Client c = Client.create();
	static WebResource resource = c.resource("http://localhost:39640");

	public static void setServer(String host, int port) {
		resource = c.resource("http://" + host + ":" + port);
	}
	
	public static void setUsernameAndPassword(final String username, final String password) {
		Communicator.username = username;
		
		c.removeAllFilters();
		
		if (username == null || password == null) return;

		c.addFilter(new HTTPBasicAuthFilter(username, password));
		Authenticator.setDefault(new Authenticator() {
			@Override
			 protected PasswordAuthentication getPasswordAuthentication() {
				 return new PasswordAuthentication(username, password.toCharArray());
			 }
		});
		
	}
	
	public static void clearCredentials() {
		username = null;
		c.removeAllFilters();		
	}
	
	public static String getUsername() {
		return username;
	}
}
