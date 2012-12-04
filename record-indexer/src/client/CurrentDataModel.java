package client;

import java.util.HashSet;
import java.util.Set;

import shared.Batch;

public class CurrentDataModel {
	private Set<DataListener> listeners = new HashSet<DataListener>();
	private String[][] data;
	public final Batch batch;
	private int selectedRow = -1;
	private int selectedCol = -1;
	
	public CurrentDataModel(Batch batch) {
		this.batch = batch;
		data = new String[batch.getProject().getRecordsperimage()][batch.getFields().size()];
	}
	
	public void updateData(int row, int col, String data) {
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
	
	public String[][] getData() {
		String [][] dataClone = new String[data.length][];
		
		for (int i = 0; i < data.length; i++)
			dataClone[i] = data[i].clone();

		return dataClone;
	}
}
