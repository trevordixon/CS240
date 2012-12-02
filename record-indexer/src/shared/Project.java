package shared;

import java.util.Map;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "project")
public class Project extends Item {
	public Project() { super(); }
	
	public Project(Map<String, String> properties) {
		super(properties);
	}

	public String getTable() { return "projects"; }
	
	@XmlElement
	public String getTitle() {
		return properties.get("title");
	}
	
	public void setTitle(String title) {
		properties.put("title", title);
	}
	
	@XmlElement
	public Integer getRecordsperimage() {
		return Integer.parseInt(properties.get("recordsperimage"));
	}
	
	public void setRecordsperimage(Integer recordsPerImage) {
		properties.put("recordsperimage", recordsPerImage.toString());
	}
	
	@XmlElement
	public Integer getFirstycoord() {
		return Integer.parseInt(properties.get("firstycoord"));
	}
	
	public void setFirstycoord(Integer firstYCoord) {
		properties.put("firstycoord", firstYCoord.toString());
	}
	
	@XmlElement
	public Integer getRecordheight() {
		return Integer.parseInt(properties.get("recordheight"));
	}

	public void setRecordheight(Integer recordHeight) {
		properties.put("recordheight", recordHeight.toString());
	}
	
	public String toString() {
		return getTitle();
	}
}
