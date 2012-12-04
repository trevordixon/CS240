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

	public void setModel(CurrentDataModel model) {
		this.model = model;
		
		Project project = model.batch.getProject();
		List<Field> fields = model.batch.getFields();
		
		String[] columnNames = new String[fields.size() + 1];
		columnNames[0] = "";
		
		String[][] data = new String[project.getRecordsperimage()][fields.size() + 1];
		
		int i = 1;
		for (Field field : fields) {
			columnNames[i++] = field.getTitle();
		}
		
		for (Integer row = 0; row < project.getRecordsperimage(); row++) {
			data[row][0] = new Integer	(row + 1).toString();
		}
		
		TableModel tableModel  = new DefaultTableModel(data, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return column != 0;
			}
		};

		table.getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);
		table.getSelectionModel().addListSelectionListener(selectionListener);
		
		table.setModel(tableModel);
		
		model.addListener(new DataListener() {
			@Override
			public void selectionChange(int row, int col) {
				System.out.println("Table detected selection change: " + row + "  " + col);
			}

			@Override
			public void dataChange(int row, int col, String data) {
				// TODO Auto-generated method stub
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
