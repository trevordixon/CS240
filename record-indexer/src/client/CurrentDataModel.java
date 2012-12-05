package client;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import shared.Batch;

@SuppressWarnings("serial")
public class CurrentDataModel implements Serializable {
	private Batch batch;

	private transient Set<DataListener> listeners = new HashSet<DataListener>();
	private String[][] data;
	
	private int selectedRow = 0;
	private int selectedCol = 0;
	
	Map<String, Integer> properties = new HashMap<String, Integer>();
	
	public Batch getBatch() {
		return batch;
	}
	
	public void setBatch(Batch batch) {
		this.batch = batch;

		data = new String[batch.getProject().getRecordsperimage()][batch.getFields().size()];
		
		for (int row = 0; row < data.length; row++) {
			for (int col = 0; col < data[row].length; col++) {
				data[row][col] = "";
			}
		}
	}
	
	public void updateData(int row, int col, String data) {
		if (this.data[row][col].equals(data)) return;
		
		this.data[row][col] = data;

		for (DataListener listener : listeners) {
			listener.dataChange(row, col, data);
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
	
	public String getData(int row, int col) {
		return data[row][col];
	}
	
	public String[][] getData() {
		String [][] dataClone = new String[data.length][];
		
		for (int i = 0; i < data.length; i++)
			dataClone[i] = data[i].clone();

		return dataClone;
	}
	
	public int getSelectedRow() {
		return selectedRow;
	}
	
	public int getSelectedCol() {
		return selectedCol;
	}
	
	public void setProperty(String key, Integer value) {
		properties.put(key, value);
	}
	
	public Integer getProperty(String key) {
		return properties.get(key);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		    in.defaultReadObject();
		    listeners = new HashSet<DataListener>();
		}
}
