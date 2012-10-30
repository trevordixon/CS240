package shared;

import java.util.Map;
import javax.xml.bind.annotation.*;

import database.DB;

@XmlRootElement(name = "project")
public class Project {
	private final Map<String, String> properties;
	
	public Project() {
		properties = null;
	}
	
	public Project(Map<String, String> properties) {
		this.properties = properties;
	}
	
	@XmlAttribute
	public Integer getId() {
		return Integer.parseInt(properties.get("rowid"));
	}
	
	@XmlElement
	public String getTitle() {
		return properties.get("title");
	}
	
	@XmlElement
	public Integer getRecordsperimage() {
		return Integer.parseInt(properties.get("recordsperimage"));
	}
	
	@XmlElement
	public Integer getFirstycoord() {
		return Integer.parseInt(properties.get("firstycoord"));
	}
	
	@XmlElement
	public Integer getRecordheight() {
		return Integer.parseInt(properties.get("recordheight"));
	}
	
	public void save() {
		DB.insertOrReplace("projects", properties);
	}
}
