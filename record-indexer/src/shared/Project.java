package shared;

import java.util.Map;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "project")
public class Project extends Item {
	public Project(Map<String, String> properties) {
		super(properties);
	}

	public String getTable() { return "projects"; }
	
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
}
