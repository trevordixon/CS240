package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import server.BadParameterException;
import server.Server;

@Path("/images")
public class Images {
	
	@POST
	@Path("sample")
	@Produces(MediaType.TEXT_PLAIN)
	public String sampleImage(@FormParam("projectid") Integer projectid) {
		if (projectid == null) throw new BadParameterException();
		try {
			Map<String, String> image = DB.get("SELECT file FROM images WHERE projectid = ?", projectid).get(0);
			return Server.URI + image.get("file") + "\n";
		} catch (Exception e) {
			throw new BadParameterException();
		}
	}

	@POST
	@Path("get_batch")
	@Produces(MediaType.TEXT_PLAIN)
	public String getBatch(@FormParam("projectid") Integer projectid, @FormParam("username") String username) {
		List<String> response = new ArrayList<String>();
		
		if (projectid == null) throw new BadParameterException();
		
		try {
			Map<String, String> image = DB.get("SELECT images.rowid AS imageid, * FROM projects, images WHERE projects.rowid = images.projectid AND projectid = ? AND username IS NULL", projectid).get(0);
			String[] values = {username, image.get("imageid")};
			DB.run("UPDATE images SET username = ? WHERE rowid = ?", values);
			
			response.add(image.get("imageid"));
			response.add(String.valueOf(projectid));
			response.add(Server.URI + image.get("file"));
			response.add(image.get("firstycoord"));
			response.add(image.get("recordheight"));
			response.add(image.get("recordsperimage"));
			
			List<Map<String, String>> fields = DB.get("SELECT rowid, * FROM fields WHERE projectid = ? ORDER BY rowid ASC", projectid);
			for (Map<String, String> field : fields) {
				response.add(field.get("rowid"));
				response.add(String.valueOf(fields.indexOf(field) + 1));
				response.add(field.get("title"));
				response.add(Server.URI + field.get("helphtml"));
				response.add(field.get("xcoord"));
				response.add(field.get("width"));
				
				if (field.get("knowndata") != null) {
					response.add(Server.URI + field.get("knowndata"));
				}
			
			}
			
			return shared.Util.join(response, "\n") + "\n";
		} catch (Exception e) {
			throw new BadParameterException();
		}
	}
	
}
