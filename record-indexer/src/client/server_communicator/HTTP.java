package client.server_communicator;

import java.util.Map;

public class HTTP {

	/*
	 * Send a GET request to the indexer server.
	 * 
	 * @param resource The server endpoint to query. E.g. if resource is "/users", a request will be made to http(s)://[server]/users?[params]
	 * @param params A Map of query string parameters to be sent
	 */
	public boolean get(String resource, Map<String, String> params) {
		return false;
	}
	
	/*
	 * Send a GET request to the indexer server.
	 * 
	 * @param resource The server endpoint to query. E.g. if resource is "/users", a request will be made to http(s)://[server]/users?[params]
	 */
	public String get(String resource) {
		return "";
	}
	
	/*
	 * Send a POST request to the indexer server.
	 * 
	 * @param resource The server endpoint to query. E.g. if resource is "/login", a request will be made to http(s)://[server]/login
	 * @param params A Map of query string parameters to be sent
	 */
	public String post(String resource, Map<String, String> params) {
		return "";
	}
	
}
