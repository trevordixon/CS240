package database;

import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import server.BadParamaterException;
import server.Server;

@Path("/images")
public class Images {
	
	@POST
	@Path("sample_image")
	@Produces(MediaType.TEXT_PLAIN)
	public String sampleImage(@FormParam("projectid") Integer projectid) {
		if (projectid == null) throw new BadParamaterException();
		try {
			Map<String, String> image = DB.get("SELECT file FROM images WHERE projectid = ?", projectid).get(0);
			return Server.URI + image.get("file") + "\n";
		} catch (Exception e) {
			throw new BadParamaterException();
		}
	}

}
