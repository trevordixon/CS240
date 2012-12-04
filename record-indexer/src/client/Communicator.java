package client;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class Communicator {
	
	private static Client c = Client.create();
	static WebResource resource = c.resource("http://localhost:39640");

	static {
		c.addFilter(new HTTPBasicAuthFilter("sheila", "parker"));
		Authenticator.setDefault(new Authenticator() {
			@Override
			 protected PasswordAuthentication getPasswordAuthentication() {
				 return new PasswordAuthentication("sheila", "parker".toCharArray());
			 }
		});
	}
	
}
