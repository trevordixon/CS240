package shared;

import java.util.Map;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "user")
public class User extends Item {
	public User(Map<String, String> properties) {
		super(properties);
	}
	
	public String getTable() { return "users"; }

	@XmlElement
	public String getUsername() {
		return properties.get("username");
	}
	
	@XmlElement
	public String getFirstname() {
		return properties.get("firstname");
	}
	
	@XmlElement
	public String getLastname() {
		return properties.get("lastname");
	}
	
	@XmlElement
	public String getEmail() {
		return properties.get("email");
	}
	
	@XmlElement
	public int getIndexedrecords() {
		return Integer.parseInt(properties.get("indexedrecords"));
	}
	
	public String getPassword() {
		return properties.get("password");
	}
	
}
