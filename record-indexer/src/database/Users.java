package database;

import server.Unsecured;
import shared.User;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.Map;
import java.util.List;

@Path("/user")
public class Users {
	
	public List<User> get() {
		return null;
	}

	public static User get(String username) {
		String[] values = {username};
		List<Map<String, String>> results = DB.get("SELECT rowid, * FROM users WHERE username= ? LIMIT 1", values);
		
		if (results.size() > 0) {
			return new User(results.get(0));
		} else {
			return null;
		}
	}
	
	public User create(String username, String password) {
		return null;
	}

	public static boolean validate(String username, String password) {
		User user = get(username);
		if (user == null || !user.getPassword().equals(password)) {
			return false;
		}
		
		return true;
	}
	
	@POST
	@Path("validate")
	@Produces(MediaType.TEXT_PLAIN)
	@Unsecured
	public Response HttpValidate(@FormParam("username") String username, @FormParam("password") String password) {
		if (!validate(username, password)) {
			return Response
				.status(401)
				.entity("FALSE\n")
				.build()
			;
		}
		
		User user = get(username);
		StringBuilder response =
			new StringBuilder("TRUE\n")
				.append(user.getFirstname())
				.append("\n")
				.append(user.getLastname())
				.append("\n")
				.append(user.getIndexedrecords())
				.append("\n")
			;
		
		return Response
			.ok(response.toString())
			.build()
		;
	}
	
}
