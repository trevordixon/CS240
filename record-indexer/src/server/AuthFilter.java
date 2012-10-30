package server;

import java.util.Collections;
import java.util.List;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class AuthFilter implements ResourceFilterFactory {

	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		if (!am.isAnnotationPresent(Unsecured.class)) {
	    	return Collections.<ResourceFilter> singletonList(new Filter());
	    }
		return null;
	}
	
	  private class Filter implements ResourceFilter, ContainerRequestFilter {

		@Override
		public ContainerRequest filter(ContainerRequest request) {
			Form f = request.getFormParameters();
			String username = f.getFirst("username");
			String password = f.getFirst("password");
			
			boolean validUser = database.Users.validate(username, password);
			
			if (!validUser) {
		        throw new NotAuthorizedException("FAILED\n");
			}
			
			return request;
		}

		@Override
		public ContainerRequestFilter getRequestFilter() {
			return this;
		}

		@Override
		public ContainerResponseFilter getResponseFilter() {
			return null;
		}
		  
	  }
}