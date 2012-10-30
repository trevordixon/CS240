package shared;

import java.util.Map;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "field")
public class Field extends Item {
	public Field(Map<String, String> properties) {
		super(properties);
	}

	public String getTable() { return "fields"; }
	
	@XmlElement
	public Integer getProjectid() {
		return Integer.parseInt(properties.get("projectid"));
	}
	
	@XmlElement
	public String getTitle() {
		return properties.get("title");
	}
	
	@XmlElement
	public Integer getXcoord() {
		return Integer.parseInt(properties.get("xcoord"));
	}
	
	@XmlElement
	public Integer getWidth() {
		return Integer.parseInt(properties.get("width"));
	}
	
	@XmlElement
	public String getHelphtml() {
		return properties.get("helphtml");
	}
}
