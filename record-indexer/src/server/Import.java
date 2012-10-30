package server;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import database.DB;

import shared.*;

public class Import {

	private static Document doc;
	
	private static void importUsers() {
		NodeList users = doc.getElementsByTagName("user");
		
		for (int i = 0; i < users.getLength(); i++) {
			if (users.item(i).getNodeName().equals("#text")) continue;

			NodeList properties = users.item(i).getChildNodes();
			
			Map<String, String> values = new HashMap<String, String>();
			for (int j = 0; j < properties.getLength(); j++) {
				Node property = properties.item(j);
				
				if (property.getNodeName().equals("#text")) {
					continue;
				}
				
				values.put(property.getNodeName(), property.getTextContent());
			}
			
			User user = new User(values);
			user.save();
		}
	}
	
	private static void importProjects() {
		NodeList projects = doc.getElementsByTagName("project");
		
		for (int i = 0; i < projects.getLength(); i++) {
			if (projects.item(i).getNodeName().equals("#text")) continue;

			NodeList properties = projects.item(i).getChildNodes();
			
			Map<String, String> values = new HashMap<String, String>();
			Project project = new Project(values);
			
			for (int j = 0; j < properties.getLength(); j++) {
				Node property = properties.item(j);
				if (property.getNodeName().equals("#text")) continue;
				
				if (property.getNodeName().equals("fields")) {
					project.save();
					importFields(property, project.getId());
					continue;
				}
				
				if (property.getNodeName().equals("images")) {
					project.save();
					importImages(property, project.getId());
					continue;
				}
				
				values.put(property.getNodeName(), property.getTextContent());
			}
			
			project.save();
		}
	}
	
	private static void importFields(Node _fields, int projectid) {
		NodeList fields = _fields.getChildNodes();
		
		for (int i = 0; i < fields.getLength(); i++) {
			if (fields.item(i).getNodeName().equals("#text")) continue;
			
			NodeList properties = fields.item(i).getChildNodes();

			Map<String, String> values = new HashMap<String, String>();
			values.put("projectid", String.valueOf(projectid));
			Field field = new Field(values);
			
			for (int j = 0; j < properties.getLength(); j++) {
				Node property = properties.item(j);
				if (property.getNodeName().equals("#text")) continue;
				
				values.put(property.getNodeName(), property.getTextContent());
			}
			
			field.save();
		}
	}
	
	private static void importImages(Node _images, int projectid) {
		NodeList images = _images.getChildNodes();

		for (int i = 0; i < images.getLength(); i++) {
			if (images.item(i).getNodeName().equals("#text")) continue;
			
			NodeList properties = images.item(i).getChildNodes();

			Map<String, String> values = new HashMap<String, String>();
			values.put("projectid", String.valueOf(projectid));
			
			Image image = new Image(values);
			
			for (int j = 0; j < properties.getLength(); j++) {
				Node property = properties.item(j);
				
				if (property.getNodeName().equals("#text")) continue;
				if (property.getNodeName().equals("records")) {
					image.save();
					importRecords(property, image.getId());
					continue;
				}
				
				values.put(property.getNodeName(), property.getTextContent());
			}
			
			image.save();
		}
	}
	
	private static void importRecords(Node _records, int imageid) {
		NodeList records = _records.getChildNodes();
		List<Map<String, String>> fields = DB.get("SELECT rowid AS fieldid, * FROM fields WHERE projectid = (SELECT projectid FROM images WHERE rowid = ?) ORDER BY rowid ASC", imageid);
		Map<String, String> project = DB.get("SELECT * FROM projects WHERE rowid = (SELECT projectid FROM images WHERE rowid = ?)", imageid).get(0);
		int recordHeight = Integer.parseInt(project.get("recordheight"));
		int ycoord = Integer.parseInt(project.get("firstycoord"));
		
		for (int i = 0; i < records.getLength(); i++) {
			if (records.item(i).getNodeName().equals("#text")) continue;
			
			Node record = records.item(i);
			NodeList values = record.getChildNodes().item(1).getChildNodes();
			
			for (int j = 0, k = 0; j < values.getLength(); j++) {
				Node valueNode = values.item(j);
				if (valueNode.getNodeName().equals("#text")) continue;

				Map<String, String> properties = new HashMap<String, String>();
				Value value = new Value(properties);

				properties.put("imageid", String.valueOf(imageid));
				properties.put("value", valueNode.getTextContent());
				properties.put("fieldid", fields.get(k++).get("fieldid"));
				properties.put("ycoord", String.valueOf(ycoord));
				
				value.save();
			}

			ycoord += recordHeight;
			
		}
	}
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		File xmlFile = new File(args[0]);
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
		doc.getDocumentElement().normalize();
		importUsers();
		importProjects();
	}

}
