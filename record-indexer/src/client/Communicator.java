package client;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class Communicator {
	private static String username = "sheila";
	private static String password = "parker";
	private static Client c = Client.create();
	static WebResource resource = c.resource("http://localhost:39640");

	static {
		c.addFilter(new HTTPBasicAuthFilter(username, password));
		Authenticator.setDefault(new Authenticator() {
			@Override
			 protected PasswordAuthentication getPasswordAuthentication() {
				 return new PasswordAuthentication(username, password.toCharArray());
			 }
		});
	}
	
	public static String getUsername() {
		return username;
	}
}
