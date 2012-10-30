package server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class BadParameterException extends WebApplicationException {
    
	public BadParameterException(String message, Response.Status status) {
    	super(
    		Response
    			.status(status)
    			.entity(message)
    			.type(MediaType.TEXT_PLAIN)
    			.build()
        );
    }

    public BadParameterException(String message) {
    	this(message, Response.Status.NOT_ACCEPTABLE);
    }
    public BadParameterException(Response.Status status) {
    	this("FAILED\n", status);
    }

    public BadParameterException() {
    	this("FAILED\n", Response.Status.NOT_ACCEPTABLE);
    }

}
