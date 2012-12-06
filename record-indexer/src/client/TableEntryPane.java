package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import shared.*;
import client.CurrentDataModel.CurrentValue;

@SuppressWarnings("serial")
public class TableEntryPane extends JScrollPane {
	private JTable table;
	private CurrentDataModel model;
	private SuggestionPopup popup = new SuggestionPopup();
	
	public TableEntryPane() {
		super();
		
		table = new JTable();

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setFillsViewportHeight(true);
		
		table.setDefaultEditor(CurrentValue.class, new DefaultCellEditor(new JTextField()){ });
		
		table.setDefaultRenderer(CurrentValue.class, new DefaultTableCellRenderer() {
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		    	Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		    	CurrentValue cv = (CurrentValue) value;
		    	
		    	if (cv == null) return c;
		    	
		    	if (cv.suggestions != null) c.setBackground(new Color(250, 170, 170));
		    	else c.setBackground(Color.WHITE);
		    	
		    	return c;
		    }
		});
		
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { }

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
			        int row = table.rowAtPoint(e.getPoint());
			        int col = table.columnAtPoint(e.getPoint());
			        model.changeSelection(row, col-1);
			        popup.refresh();
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) { }

			@Override
			public void mouseExited(MouseEvent e) { }
		});
		
		setViewportView(table);
	}

	public void setModel(final CurrentDataModel model) {
		this.model = model;
		
		popup.setModel(model);
		
		Project project = model.getBatch().getProject();
		List<Field> fields = model.getBatch().getFields();
		
		String[] columnNames = new String[fields.size() + 1];
		columnNames[0] = "";
		
		Object[][] data = new Object[project.getRecordsperimage()][fields.size() + 1];
		
		// Populate column headers
		int i = 1;
		for (Field field : fields) {
			columnNames[i++] = field.getTitle();
		}
		
		// Populate numbers in the first column
		for (Integer row = 0; row < project.getRecordsperimage(); row++) {
			data[row][0] = new Integer(row + 1);
		}
		
		CurrentValue[][] initData = model.getData();
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
				if (value == null) System.out.println("WHAT THE");
				
				String val = "";
				CurrentValue cv = null;
				
				if (value instanceof String) {
					val = (String) value;
					cv = model.new CurrentValue(val, col-1);
				}
				else if (value instanceof CurrentValue) {
					cv = ((CurrentValue) value);
					val = cv.value;
				}
				
				super.setValueAt(cv, row, col);
				
				model.updateData(row, col-1, val);
			}
			
			@Override
			public Class<?> getColumnClass(int i) {
				return (i == 0) ? String.class : CurrentValue.class;
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
			public void dataChange(int row, int col, CurrentValue data) {
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
