package shared;

import java.util.Map;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "user")
public class User {
	private final String username;
	private final String password;
	private final String first_name;
	private final String last_name;
	private final String email;
	private int indexed_records;
	
	public User(
		String username,
		String password,
		String firstName,
		String lastName,
		String email,
		int indexedRecords
	) {
		this.username = username;
		this.password = password;
		this.first_name = firstName;
		this.last_name = lastName;
		this.email = email;
		this.indexed_records = indexedRecords;
	}
	
	public User(Map<String, String> properties) {
		this.username = properties.get("username");
		this.password = properties.get("password");
		this.first_name = properties.get("first_name");
		this.last_name = properties.get("last_name");
		this.email = properties.get("email");
		this.indexed_records = Integer.parseInt(properties.get("indexed_records"));
	}
	
	@XmlAttribute
	public String getUsername() {
		return username;
	}
	
	@XmlElement
	public String getFirstname() {
		return first_name;
	}
	
	@XmlElement
	public String getLastname() {
		return last_name;
	}
	
	@XmlElement
	public String getEmail() {
		return email;
	}
	
	public int getIndexedrecords() {
		return indexed_records;
	}
	
	public String getPassword() {
		return password;
	}
}
