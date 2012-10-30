package shared;

import java.util.Map;
import javax.xml.bind.annotation.*;

import database.DB;

@XmlRootElement(name = "user")
public class User {
	private final Map<String, String> properties;
	
	public User(Map<String, String> properties) {
		this.properties = properties;
	}
	
	@XmlAttribute
	public Integer getId() {
		String id = properties.get("rowid");
		if (id == null) return null;
		return Integer.parseInt(id);
	}
	
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
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
	public void save() {
		DB.insertOrReplace("users", properties);
	}
}
