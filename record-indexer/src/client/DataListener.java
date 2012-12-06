package client;

import client.CurrentDataModel.CurrentValue;

public interface DataListener {
	public void selectionChange(int row, int col);
	public void dataChange(int row, int col, CurrentValue data);
}
