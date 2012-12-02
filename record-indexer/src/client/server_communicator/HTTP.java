package client.server_communicator;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

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
	
	/*
	 * 
	 */
	private String getUrl(String resource) {
		return "http://" + host + ":" + port + "/" + resource;
	}

	/*
	 * Send a POST request to the indexer server.
	 * 
	 * @param resource The server endpoint to query. E.g. if resource is "user/validate", a request will be made to http(s)://[server]/user/validate
	 * @param params A Map of query string parameters to be sent
	 * @return Information about the request and response.
	 */
	public ReqRes post(String resource, Map<String, String> params) {
		Client client = Client.create();
		WebResource webResource = client.resource(getUrl(resource));
		
		String data = "";
		
		if (params != null) {
			UriBuilder dataUri = UriBuilder.fromUri("");
			for (Entry<String, String> entry : params.entrySet()) {
				dataUri.queryParam(entry.getKey(), entry.getValue());
			}
			
			data = dataUri.build().toString().substring(1);
		}
		
		ClientResponse response = webResource
			.accept("text/plain")
			.header("Content-Type", "application/x-www-form-urlencoded")
			.post(ClientResponse.class, data)
		;

		return new ReqRes(data, response.getEntity(String.class), response);
	}
	
}
