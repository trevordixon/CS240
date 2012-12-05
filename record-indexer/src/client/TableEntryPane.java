package client;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import shared.*;

@SuppressWarnings("serial")
public class TableEntryPane extends JScrollPane {
	private JTable table;
	private CurrentDataModel model;
	
	public TableEntryPane() {
		super();
		
		table = new JTable();

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setFillsViewportHeight(true);
		
		setViewportView(table);
	}

	public void setModel(final CurrentDataModel model) {
		this.model = model;
		
		Project project = model.getBatch().getProject();
		List<Field> fields = model.getBatch().getFields();
		
		String[] columnNames = new String[fields.size() + 1];
		columnNames[0] = "";
		
		String[][] data = new String[project.getRecordsperimage()][fields.size() + 1];
		
		// Populate column headers
		int i = 1;
		for (Field field : fields) {
			columnNames[i++] = field.getTitle();
		}
		
		// Populate numbers in the first column
		for (Integer row = 0; row < project.getRecordsperimage(); row++) {
			data[row][0] = new Integer(row + 1).toString();
		}
		
		String[][] initData = model.getData();
		// Populate data
		for (int row = 0; row < initData.length; row++) {
			for (int col = 0; col < initData[row].length; col++) {
				data[row][col+1] = initData[row][col];
			}
		}
		
		TableModel tableModel  = new DefaultTableModel(data, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return column != 0;
			}
			
			public void setValueAt(Object value, int row, int col) {
				super.setValueAt(value, row, col);
				model.updateData(row, col-1, (String) value);
			}
		};

		table.getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);
		table.getSelectionModel().addListSelectionListener(selectionListener);
		
		table.setModel(tableModel);
		
		setSelectedCell(model.getSelectedRow(), model.getSelectedCol() + 1);
		table.requestFocusInWindow();
		
		model.addListener(new DataListener() {
			@Override
			public void selectionChange(int row, int col) {
				table.setRowSelectionInterval(row, row);
				table.setColumnSelectionInterval(col+1, col+1);
			}

			@Override
			public void dataChange(int row, int col, String data) {
				table.setValueAt(data, row, col+1);
			}
		});
	}
	
	public void setSelectedCell(int row, int col) {
		table.getSelectionModel().setSelectionInterval(row, row);
		table.getColumnModel().getSelectionModel().setSelectionInterval(col, col);
	}
	
	ListSelectionListener selectionListener = new ListSelectionListener() {
		int selectedRow = -1;
		int selectedCol = -1;
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) return;
			
			int row = table.getSelectedRow();
			int col = table.getSelectedColumn();
			
			if (row == selectedRow && col == selectedCol) return;
			selectedRow = row;
			selectedCol = col;
			
			if (col > 0) model.changeSelection(row, col - 1);
		}
	};
	
}
