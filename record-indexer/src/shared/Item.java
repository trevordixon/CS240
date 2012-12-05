package shared;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;

import database.DB;

@SuppressWarnings("serial")
public class Item implements java.io.Serializable {
	protected final Map<String, String> properties;
	
	public Item() {
		properties = new HashMap<String, String>();
	}
	
	public Item(Map<String, String> properties) {
		this.properties = properties;
	}

	@XmlAttribute
	public Integer getId() {
		String id = properties.get("rowid");
		if (id == null) return null;
		return Integer.parseInt(id);
	}
	
	public void setId(String id) {
		properties.put("rowid", id);
	}
	
	public void setId(Integer id) {
		setId(id.toString());
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
	public int save() {
		return DB.insertOrReplace(getTable(), properties);
	}
	
	public String getTable() {
		return "";
	}

}
