package shared;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.*;

import database.DB;

@SuppressWarnings("serial")
@XmlRootElement(name = "image")
public class Batch extends Item {
	Project project;
	byte[] data;
	List<Field> fields;
	String url;
	
	public Batch() { }
	
	public Batch(Map<String, String> properties) {
		super(properties);
	}

	public String getTable() { return "images"; }
	
	public void setData(byte[] b) {
		data = b;
	}
	
	@XmlElement
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
	@XmlElement
	public Integer getProjectid() {
		return Integer.parseInt(properties.get("projectid"));
	}
	
	@XmlElement
	public String getFile() {
		return properties.get("file");
	}
	
	@XmlElementWrapper(name = "fields")
	@XmlElement(name = "field")
	public List<Field> getFields() {
		return fields;
	}
	
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	@XmlElement
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public int save() {
		int n = super.save();
		try {
			PreparedStatement s = DB.connection.prepareStatement("UPDATE images SET data = ? WHERE rowid = ?");
			s.setBytes(1, data);
			s.setString(2, properties.get("rowid"));
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return n;
	}

}
