package database;

import shared.Project;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

@Path("/project")
public class Projects {
	
	public static List<Project> get() {
		List<Map<String, String>> results = DB.get("SELECT rowid, * FROM projects");
		
		List<Project> list = new ArrayList<Project>();
		for (Map<String, String> row : results) {
			list.add(new Project(row));
		}
		
		return list;
	}

	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public static Response getStructured(@Context SecurityContext sc) {		
		System.out.println(sc.getUserPrincipal().getName());

		List<Project> projects = Projects.get();
		GenericEntity<List<Project>> entity = new GenericEntity<List<Project>>(projects) {};
		return Response.ok(entity).build();
	}
	

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public static Response getPlainText() {
		List<Project> projects = Projects.get();
		
		StringBuilder response = new StringBuilder();
		for (Project p : projects) {
			response
				.append(p.getId())
				.append("\n")
				.append(p.getTitle())
				.append("\n")
			;
		}
		
		return Response.ok(response.toString()).build();
	}
	
}
