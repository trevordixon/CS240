package shared;

import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;

import database.DB;

public class Item {
	protected final Map<String, String> properties;
	
	public Item() {
		properties = null;
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
