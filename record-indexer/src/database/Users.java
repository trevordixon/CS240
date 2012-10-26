package database;
import shared.User;

public class Users {

	public User[] get() {
		return new User[0];
	}

	public User get(String username) {
		return new User();
	}
	
	public User create(String username, String password) {
		return new User();
	}

}
