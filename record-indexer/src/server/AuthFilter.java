package server;

import java.util.Collections;
import java.util.List;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;
import com.sun.jersey.core.util.Base64;

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
			String auth = request.getHeaderValue("Authorization");
			
			if (auth == null || !auth.startsWith("Basic ")) {
		        throw new NotAuthorizedException("FAILED\n");
			}

			try {
				auth = Base64.base64Decode(auth.substring("Basic ".length()));
				String[] vals = auth.split(":");
				String username = vals[0];
				String password = vals[1];
				
				boolean validUser = database.Users.validate(username, password);
				
				if (!validUser) {
			        throw new NotAuthorizedException("FAILED\n");
				}
				
				request.setSecurityContext(new Authorizer(username));
				
				return request;
			} catch (Exception e) {
				throw new NotAuthorizedException("FAILED\n");
			}
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
