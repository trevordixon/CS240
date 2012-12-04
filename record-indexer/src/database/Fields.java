package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import shared.Field;

@Path("/fields")
public class Fields {

	public static List<Field> get(int projectid) {
		List<Field> fields = new ArrayList<Field>();
		
		List<Map<String, String>> results = DB.get("SELECT rowid, * FROM fields WHERE projectid = ? ORDER BY rowid ASC", projectid);
		for (Map<String, String> row : results) {
			fields.add(new Field(row));
		}

		return fields;
	}
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String toString(@FormParam("projectid") Integer projectid) {
		List<Map<String, String>> fields;
		
		String sql = "SELECT rowid, * FROM fields";
		try {
			if (projectid == null) {
				fields = DB.get(sql);
			} else {
				fields = DB.get(sql + " WHERE projectid = ?", projectid);
			}
			
			StringBuilder response = new StringBuilder();
			for (Map<String, String> field : fields) {
				response
					.append(field.get("projectid"))
					.append("\n")
					.append(field.get("rowid"))
					.append("\n")
					.append(field.get("title"))
					.append("\n")
				;
			}
			
			return response.toString();
		} catch (Exception e) {
			throw new server.BadParameterException();
		}
		
	}

}
