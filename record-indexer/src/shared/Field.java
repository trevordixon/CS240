package shared;

import java.util.Map;
import java.util.Scanner;

import javax.xml.bind.annotation.*;

import spelling.Spell;
import spelling.Words;

@SuppressWarnings("serial")
@XmlRootElement(name = "field")
public class Field extends Item {
	public Field() { }
	
	public Field(Map<String, String> properties) {
		super(properties);
	}

	public String getTable() { return "fields"; }
	
	@XmlElement
	public Integer getProjectid() {
		return Integer.parseInt(properties.get("projectid"));
	}
	
	@XmlElement
	public String getTitle() {
		return properties.get("title");
	}
	
	public void setTitle(String title) {
		properties.put("title", title);
	}
	
	@XmlElement
	public Integer getXcoord() {
		return Integer.parseInt(properties.get("xcoord"));
	}
	
	public void setXcoord(Integer xcoord) {
		properties.put("xcoord", xcoord.toString());
	}
	
	@XmlElement
	public Integer getWidth() {
		return Integer.parseInt(properties.get("width"));
	}
	
	public void setWidth(Integer width) {
		properties.put("width", width.toString());
	}
	
	@XmlElement
	public String getHelphtml() {
		return properties.get("helphtml");
	}
	
	public void setHelphtml(String helphtml) {
		properties.put("helphtml", helphtml);
	}
	
	@XmlElement
	public String getKnowndata() {
		return properties.get("knowndata");
	}
	
	public void setKnowndata(String knowndata) {
		properties.put("knowndata", knowndata);
	}
	
	private Words words;
	public void parseKnownData() {
		String data = getKnowndata();
		
		if (data == null) return;
		
		words = new Words();
		Scanner sc = new Scanner(data);
		sc.useDelimiter(",");
		while (sc.hasNext()) {
			String word = sc.next();
			words.add(word);
		}
		sc.close();
	}
	
	public String getSuggestion(String s) {
		if (words == null) return s;
		return Spell.getSuggestion(s, words);
	}
}
