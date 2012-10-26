package server;

import javax.ws.rs.*;

@Path("/testing")
public class TestHandler {

	@GET
	public String test() {
		return "It works.";
	}

}
