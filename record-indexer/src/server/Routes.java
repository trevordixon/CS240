package server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Routes {
	
	@POST
	@Path("/login")
	//@Produces(MediaType.APPLICATION_JSON)
	public String login() {
		return "Welcome";
	}
	
	@GET
	@Path("/hello")
	public String sayHello() {
		return "HI!";
	}
	
	@GET
	public String toString() {
		return "Record Indexer Server";
	}
}
