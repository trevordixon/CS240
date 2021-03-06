package client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import client.listeners.ViewportPercentListener;

import shared.*;

@SuppressWarnings("serial")
public class CurrentDataModel implements Serializable {
	private Batch batch;
	private transient BufferedImage image;

	private transient Set<DataListener> listeners = new HashSet<DataListener>();
	private CurrentValue[][] data;
	
	private int selectedRow = 0;
	private int selectedCol = 0;
	
	double xStartPercent = 0;
	double yStartPercent = 0;
	double xEndPercent = 0;
	double yEndPercent = 0;
	
	transient ViewportPercentListener viewportPercentListener = new ViewportPercentListener() {
		@Override
		public void change(double xStartPercent, double yStartPercent,
				double xEndPercent, double yEndPercent) {
			// TODO Auto-generated method stub
		}  };
	
	public void setViewportPercents(double xStartPercent, double yStartPercent, double xEndPercent, double yEndPercent) {
		this.xStartPercent = xStartPercent;
		this.yStartPercent = yStartPercent;
		this.xEndPercent = xEndPercent;
		this.yEndPercent = yEndPercent;
		
		viewportPercentListener.change(xStartPercent, yStartPercent, xEndPercent, yEndPercent);
	}
	
	public void setViewportPercentListener(ViewportPercentListener l) {
		viewportPercentListener = l;
	}
	
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
				data[row][col] = new CurrentValue("", col);
			}
		}
		
		for (Field field : fields) {
			field.parseKnownData();
		}
	}
	
	public void unloadBatch() {
		this.batch = null;
		this.image = null;
		this.listeners = new HashSet<DataListener>();
		this.data = null;
		this.selectedRow = this.selectedCol = 0;
	}
	
	public void updateData(int row, int col, String data) {
		if (this.data[row][col].equals(data)) return;
		
		CurrentValue cv = new CurrentValue(data, col);
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
	
	public CurrentValue getSelectedData() {
		return getData(getSelectedRow(), getSelectedCol());
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
	
	public String getDataString() {
		String[] vals = new String[data.length * data[0].length];
		
		int i = 0;
		for (CurrentValue[] row : data) {
			for (CurrentValue cv : row) {
				vals[i++] = cv.value;
			}
		}
		
		return join(vals, ",");
	}
	
	private String join(String[] s, String glue) {
		int k = s.length;
		if (k == 0)
			return null;
		StringBuilder out = new StringBuilder();
		out.append(s[0]);
		
		for (int x = 1; x < k; ++x)
			out.append(glue).append(s[x]);
		
		return out.toString();
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
		public Set<String> suggestions = null;
		
		public CurrentValue() {
			value = "";
		}
		
		public CurrentValue(String value) {
			this.value = value;
		}
		
		public CurrentValue(String value, int col) {
			this.value = value;
			Field field = batch.getFields().get(col);
			suggestions = field.getSuggestions(value);

		}
		
		public boolean equals(String s) {
			return value.equals(s);
		}
		
		public boolean equals(CurrentValue cv) {
			return value.equals(cv.value);
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
}
