package client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import shared.*;

@SuppressWarnings("serial")
public class CurrentDataModel implements Serializable {
	private Batch batch;
	private transient BufferedImage image;

	private transient Set<DataListener> listeners = new HashSet<DataListener>();
	private CurrentValue[][] data;
	
	private int selectedRow = 0;
	private int selectedCol = 0;
	
	Map<String, Object> properties = new HashMap<String, Object>();
	
	public Batch getBatch() {
		return batch;
	}
	
	private void loadImage() {
		String url = batch.getUrl();
		try {
			image = ImageIO.read(new URL(url));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadBatch(Batch batch) {
		this.batch = batch;
		
		Project project = batch.getProject();
		List<Field> fields = batch.getFields();

		loadImage();
		data = new CurrentValue[project.getRecordsperimage()][fields.size()];
		
		for (int row = 0; row < data.length; row++) {
			for (int col = 0; col < data[row].length; col++) {
				data[row][col] = new CurrentValue("");
			}
		}
		
		for (Field field : fields) {
			field.parseKnownData();
		}
	}
	
	public void updateData(int row, int col, String data) {
		if (this.data[row][col].equals(data)) return;
		
		CurrentValue cv = new CurrentValue();
		cv.value = data;
		
		Field field = batch.getFields().get(col);
		List<String> suggestions = new ArrayList<String>();
		String suggestion = field.getSuggestion(data);
		if (suggestion == null || !data.toLowerCase().equals(suggestion.toLowerCase())) {
			suggestions.add(suggestion);
		}
		cv.suggestions = suggestions;
		
		this.data[row][col] = cv;

		for (DataListener listener : listeners) {
			listener.dataChange(row, col, cv);
		}
	}
	
	public void changeSelection(int row, int col) {
		selectedRow = row;
		selectedCol = col;
		
		for (DataListener listener : listeners) {
			listener.selectionChange(row, col);
		}		
	}
	
	public void addListener(DataListener listener) {
		listeners.add(listener);
	}
	
	public CurrentValue getData(int row, int col) {
		return data[row][col];
	}
	
	public CurrentValue[][] getData() {
		return data;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public int getSelectedRow() {
		return selectedRow;
	}
	
	public int getSelectedCol() {
		return selectedCol;
	}
	
	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
	    in.defaultReadObject();
	    listeners = new HashSet<DataListener>();
	    if (batch != null) loadImage();
	}
	
	class CurrentValue implements java.io.Serializable {
		public String value;
		public List<String> suggestions = new ArrayList<String>();
		
		public CurrentValue() {
			value = "";
		}
		
		public CurrentValue(String value) {
			this.value = value;
		}
		
		public boolean equals(String s) {
			return value.equals(s);
		}
		
		public boolean equals(CurrentValue cv) {
			return value.equals(cv.value);
		}
	}
}
