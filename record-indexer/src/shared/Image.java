package shared;

import java.util.Map;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "image")
public class Image extends Item {
	public Image() { }
	
	public Image(Map<String, String> properties) {
		super(properties);
	}

	public String getTable() { return "images"; }
	
	@XmlElement
	public Integer getProjectid() {
		return Integer.parseInt(properties.get("projectid"));
	}
	
	@XmlElement
	public String getFile() {
		return properties.get("file");
	}

}
