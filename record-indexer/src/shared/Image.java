package shared;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import javax.xml.bind.annotation.*;

import database.DB;

@XmlRootElement(name = "image")
public class Image extends Item {
	byte[] data;
	
	public Image() { }
	
	public Image(Map<String, String> properties) {
		super(properties);
	}

	public String getTable() { return "images"; }
	
	public void setData(byte[] b) {
		data = b;
	}
	
	@XmlElement
	public Integer getProjectid() {
		return Integer.parseInt(properties.get("projectid"));
	}
	
	@XmlElement
	public String getFile() {
		return properties.get("file");
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
