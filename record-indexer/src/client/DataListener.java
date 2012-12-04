package client;

public interface DataListener {
	public void selectionChange(int row, int col);
	public void dataChange(int row, int col, String data);
}
