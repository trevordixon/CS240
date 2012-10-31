package client.server_communicator;

import com.sun.jersey.api.client.ClientResponse;

public class ReqRes {
	public final String request;
	public final String body;
	public final ClientResponse response;
	
	public ReqRes(String req, String body, ClientResponse response) {
		this.request = req;
		this.body = body;
		this.response = response;
	}
}
