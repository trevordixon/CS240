package server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class NotAuthorizedException extends WebApplicationException {
    public NotAuthorizedException(String message) {
    	super(
    		Response
    			.status(Response.Status.UNAUTHORIZED)
    			.entity(message)
    			.type(MediaType.TEXT_PLAIN)
    			.build()
        );
    }

}
