package shared;

import java.util.Map;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "project")
public class Project {
	private final int rowid;
	private final String title;
	private final int records_per_image;
	private final int first_y_coord;
	private final int record_height;
	
	public Project() {
		rowid = -1;
		title = "";
		records_per_image =
			first_y_coord =
			record_height = 0;
	}
	
	public Project(
		int rowid,
		String title,
		int records_per_image,
		int first_y_coord,
		int record_height
	) {
		this.rowid = rowid;
		this.title = title;
		this.records_per_image = records_per_image;
		this.first_y_coord = first_y_coord;
		this.record_height = record_height;
	}
	
	public Project(Map<String, String> properties) {
		this.rowid = Integer.parseInt(properties.get("rowid"));
		this.title = properties.get("title");
		this.records_per_image = Integer.parseInt(properties.get("records_per_image"));
		this.first_y_coord = Integer.parseInt(properties.get("first_y_coord"));
		this.record_height = Integer.parseInt(properties.get("record_height"));
	}
	
	@XmlAttribute
	public Integer getId() {
		return rowid;
	}
	
	@XmlElement
	public String getTitle() {
		return title;
	}
	
	@XmlElement
	public Integer getRecordsperimage() {
		return records_per_image;
	}
	
	@XmlElement
	public Integer getFirstycoord() {
		return first_y_coord;
	}
	
	@XmlElement
	public Integer getRecordheight() {
		return record_height;
	}
}
