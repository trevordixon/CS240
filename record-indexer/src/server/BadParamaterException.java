package server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class BadParamaterException extends WebApplicationException {
    
	public BadParamaterException(String message, Response.Status status) {
    	super(
    		Response
    			.status(status)
    			.entity(message)
    			.type(MediaType.TEXT_PLAIN)
    			.build()
        );
    }

    public BadParamaterException(String message) {
    	this(message, Response.Status.NOT_ACCEPTABLE);
    }
    public BadParamaterException(Response.Status status) {
    	this("FAILED\n", status);
    }

    public BadParamaterException() {
    	this("FAILED\n", Response.Status.NOT_ACCEPTABLE);
    }

}
