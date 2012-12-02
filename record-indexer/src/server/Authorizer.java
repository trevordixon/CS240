package server;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class Authorizer implements SecurityContext {
	private Principal principal = null;
	
	public Authorizer(final String username) {
		principal = new Principal() {
			@Override
			public String getName() {
				return username;
			}
		};
	}
	
	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}

	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public boolean isUserInRole(String role) {
		return "user".equals(role);
	}

}
