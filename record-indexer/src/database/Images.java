package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.sun.jersey.api.core.HttpContext;

import server.BadParameterException;
import shared.Field;
import shared.Batch;
import shared.Project;
import shared.Util;
import shared.Value;

@Path("/batch")
public class Images {
	
	@GET
	@Path("sample/{projectid}")
	@Produces({MediaType.TEXT_PLAIN, MediaType.TEXT_XML})
	public String sampleImage(@PathParam("projectid") Integer projectid, @Context HttpContext context) {
		if (projectid == null) throw new BadParameterException();
		try {
			String baseUri = context.getRequest().getBaseUri().toString();
			Map<String, String> image = DB.get("SELECT rowid FROM images WHERE projectid = ?", projectid).get(0);
			return baseUri + "batch/image/" + image.get("rowid") + "\n";
		} catch (Exception e) {
			throw new BadParameterException();
		}
	}
	
	@GET
	@Path("image/{imageid}")
	@Produces("image/png")
	public Response getImage(@PathParam("imageid") String imageid) {
		try {
			PreparedStatement s = DB.connection.prepareStatement("SELECT data FROM images WHERE rowid = ?");
			s.setString(1, imageid);
			ResultSet r = s.executeQuery();
			return Response.ok(r.getBytes("data")).build();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@GET
	@Path("get/{projectid}")
	@Produces(MediaType.TEXT_XML)
	public Response getStructured(@PathParam("projectid") String projectid, @Context HttpContext context, @Context SecurityContext sc) {
		String username = sc.getUserPrincipal().getName();
		String baseUri = context.getRequest().getBaseUri().toString();
		
		Map<String, String> result = DB.get("SELECT images.rowid AS imageid, * FROM projects, images WHERE projects.rowid = images.projectid AND projectid = ? AND username IS NULL", projectid).get(0);
		String[] values = {username, result.get("imageid")};
		DB.run("UPDATE images SET username = ? WHERE rowid = ?", values);

		Batch batch = new Batch(result);
		List<Field> fields = Fields.get(Integer.parseInt(projectid));
		batch.setFields(fields);
		batch.setUrl(baseUri + "batch/image/" + result.get("imageid"));
		batch.setProject(new Project(result));
		
		return Response.ok(batch).build();
	}
	
	@POST
	@Path("get")
	@Produces(MediaType.TEXT_PLAIN)
	public String getBatch(@FormParam("projectid") Integer projectid, @FormParam("username") String username, @Context HttpContext context) {
		List<String> response = new ArrayList<String>();
		
		if (projectid == null) {
			throw new BadParameterException();
		}
		
		try {
			String baseUri = context.getRequest().getBaseUri().toString();
			
			Map<String, String> image = DB.get("SELECT images.rowid AS imageid, * FROM projects, images WHERE projects.rowid = images.projectid AND projectid = ? AND username IS NULL", projectid).get(0);
			String[] values = {username, image.get("imageid")};
			DB.run("UPDATE images SET username = ? WHERE rowid = ?", values);
			
			response.add(image.get("imageid"));
			response.add(String.valueOf(projectid));
			response.add(baseUri + "batch/image/" + image.get("imageid"));
			response.add(image.get("firstycoord"));
			response.add(image.get("recordheight"));
			response.add(image.get("recordsperimage"));
			
			List<Map<String, String>> fields = DB.get("SELECT rowid, * FROM fields WHERE projectid = ? ORDER BY rowid ASC", projectid);
			for (Map<String, String> field : fields) {
				response.add(field.get("rowid"));
				response.add(String.valueOf(fields.indexOf(field) + 1));
				response.add(field.get("title"));
				response.add(baseUri + field.get("helphtml"));
				response.add(field.get("xcoord"));
				response.add(field.get("width"));
				
				if (field.get("knowndata") != null) {
					response.add(baseUri + field.get("knowndata"));
				}
			
			}
			
			return shared.Util.join(response, "\n") + "\n";
		} catch (Exception e) {
			e.printStackTrace();
			throw new BadParameterException();
		}
	}
	
	@POST
	@Path("submit")
	@Produces(MediaType.TEXT_PLAIN)
	public String submitBatch(@FormParam("batch") Integer imageid, @FormParam("record_values") String _values, @Context HttpContext context, @Context SecurityContext sc) {
		String username = sc.getUserPrincipal().getName();
		
		if (imageid == null || _values == null) {
			System.out.println("imageid: " + imageid);
			System.out.println("_values: " + _values);
			throw new BadParameterException();
		}
			
		try {
			Map<String, String> imageInfo = DB.get("SELECT * FROM images WHERE rowid = ?", imageid).get(0);
			Integer projectid = Integer.parseInt(imageInfo.get("projectid"));
			if (!imageInfo.get("username").equals(username)) {
				System.out.println(imageInfo.get("username") + "   " + username);
				throw new BadParameterException();
			}
		
			List<Map<String, String>> fields = DB.get("SELECT rowid,* FROM fields WHERE projectid = ? ORDER BY rowid ASC", projectid);
			Map<String, String> projectInfo = DB.get("SELECT * FROM projects WHERE rowid = ?", projectid).get(0);
			int recordsperimage = Integer.parseInt(projectInfo.get("recordsperimage"));
			int firstycoord = Integer.parseInt(projectInfo.get("firstycoord"));
			int recordheight = Integer.parseInt(projectInfo.get("recordheight"));
			int numberValues = fields.size() * recordsperimage;
			
			String[] values = _values.split(",", -1);
			if (values.length != numberValues) {
				System.out.println(values.length + "   " + numberValues);
				System.out.println(values);
				System.out.println(_values);
				
				throw new BadParameterException();
			}
			for (int i = 0, f = 0, r = 0; i < numberValues; i++) {
				Map<String, String> properties = new HashMap<String, String>();
				properties.put("fieldid", fields.get(f).get("rowid"));
				properties.put("imageid", imageid.toString());
				properties.put("value", values[i].trim());
				properties.put("ycoord", String.valueOf(firstycoord + (r * recordheight)));
				
				if ((++f + 1) > fields.size()) {
					f = 0;
					++r;
				}
				
				new Value(properties).save();
			}

			DB.run("UPDATE images SET username = '' WHERE rowid = ?", imageid);
			return "TRUE\n";
		} catch (BadParameterException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@POST
	@Path("search")
	@Produces(MediaType.TEXT_PLAIN)
	public String search(@FormParam("fields") String _fields, @FormParam("search_values") String _searchValues, @Context HttpContext context) {
		String baseUri = context.getRequest().getBaseUri().toString();
		
		_searchValues = _searchValues.toLowerCase();
		
		String[] fields = _fields.split(",");
		String[] searchValues = _searchValues.split(",");
		
		for (int i = 0; i < fields.length; i++) {
			fields[i] = fields[i].trim();
		}
		
		for (int i = 0; i < searchValues.length; i++) {
			searchValues[i] = searchValues[i].trim();
		}
		
		String[] p1 = new String[fields.length];
		String[] p2 = new String[searchValues.length];
		
		for (int i = 0; i < fields.length; i++) {
			p1[i] = "?";
		}
		
		for (int i = 0; i < searchValues.length; i++) {
			p2[i] = "?";
		}
		
		String sql = "SELECT `values`.*, `values`.rowid, LOWER(`values`.value) AS lcval, images.file FROM `values`, images WHERE images.rowid = `values`.imageid AND lcval IN (" + Util.join(p1, ",") + ") AND fieldid IN (" + Util.join(p2, ",") + ")";
		List<Map<String, String>> results = DB.get(sql, Util.arrayConcat(searchValues, fields));
		
		StringBuilder response = new StringBuilder();
		for (Map<String, String> result : results) {
			response
				.append(result.get("imageid"))
				.append("\n")
				.append(baseUri + result.get("file"))
				.append("\n")
				.append(result.get("rowid"))
				.append("\n")
				.append(result.get("fieldid"))
				.append("\n")
			;
		}
		
		return response.toString();
	}

}
