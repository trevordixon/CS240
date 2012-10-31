package client.server_communicator;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.UriBuilder;

public class HTTP {
	private String host = "localhost";
	private int port = 39640;
	
	public HTTP() { }
	
	public HTTP(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public String getUrl(String resource) {
		return "http://" + host + ":" + port + "/" + resource;
	}

	/*
	 * Send a POST request to the indexer server.
	 * 
	 * @param resource The server endpoint to query. E.g. if resource is "/login", a request will be made to http(s)://[server]/login
	 * @param params A Map of query string parameters to be sent
	 */
	public ReqRes post(String resource, Map<String, String> params) {
		Client client = Client.create();
		WebResource webResource = client.resource(getUrl(resource));
		
		UriBuilder dataUri = UriBuilder.fromUri("");
		for (Entry<String, String> entry : params.entrySet()) {
			dataUri.queryParam(entry.getKey(), entry.getValue());
		}
		
		String data = dataUri.build().toString().substring(1);
		ClientResponse response = webResource
			.accept("text/plain")
			.header("Content-Type", "application/x-www-form-urlencoded")
			.post(ClientResponse.class, data)
		;

		HttpURLConnection u;
		return new ReqRes(data, response.getEntity(String.class), response);
	}
	
	public static void main(String[] args) {
		HTTP test = new HTTP("localhost", 39640);
		
		@SuppressWarnings("serial")
		Map<String, String> params = new HashMap<String, String>(){{
			put("username", "sheila");
			put("password", "parkers");
		}};
		
		System.out.println(test.post("user/validate", params).body);
	}
	
}
