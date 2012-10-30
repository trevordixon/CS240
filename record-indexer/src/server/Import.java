package server;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import shared.User;

public class Import {

	private static Document doc;
	
	private static void importUsers() {
		doc.getDocumentElement().normalize();
		
		NodeList users = doc.getElementsByTagName("user");
		
		for (int i = 0; i < users.getLength(); i++) {
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
		doc.getDocumentElement().normalize();
		
		NodeList users = doc.getElementsByTagName("project");
		
		for (int i = 0; i < users.getLength(); i++) {
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
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		File xmlFile = new File(args[0]);
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
		importUsers();
		importProjects();
	}

}
