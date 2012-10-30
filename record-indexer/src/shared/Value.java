package shared;

import java.util.Map;
import javax.xml.bind.annotation.*;

public class Value extends Item {
	public Value() { }
	
	public Value(Map<String, String> properties) {
		super(properties);
	}

	public String getTable() { return "values"; }
	
	@XmlElement
	public Integer getFieldid() {
		return Integer.parseInt(properties.get("fieldid"));
	}
	
	@XmlElement
	public String getValue() {
		return properties.get("value");
	}

}
