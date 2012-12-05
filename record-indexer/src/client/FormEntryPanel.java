package client;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JList;

import shared.*;

import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class FormEntryPanel extends JPanel {
	CurrentDataModel model;
	JList<String> list = new JList<String>();
	JPanel formPanel;
	JTextField[] textFields;
	
	/**
	 * Create the panel.
	 */
	public FormEntryPanel() {
		setLayout(new BorderLayout(0, 0));
		
		add(list, BorderLayout.WEST);
		
		formPanel = new JPanel();
		add(formPanel, BorderLayout.CENTER);
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
	}

	public void setModel(final CurrentDataModel model) {
		this.model = model;
		
		Batch batch = model.batch;
		Project project = batch.getProject();
		List<Field> fields = batch.getFields();
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (int i = 0; i < project.getRecordsperimage(); i++) {
			listModel.addElement(Integer.toString(i+1) + "               ");
		}
		
		list.setModel(listModel);
		list.setSelectedIndex(model.getSelectedRow());
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				model.changeSelection(list.getSelectedIndex(), model.getSelectedCol());
			}
		});
		
		model.addListener(new DataListener() {
			@Override
			public void selectionChange(int row, int col) {
				list.setSelectedIndex(row);
				loadValues();
			}

			@Override
			public void dataChange(int row, int col, String data) {
				// TODO Auto-generated method stub
			}
		});
		
		textFields = new JTextField[fields.size()];
		int col = 0;
		for (Field field : model.batch.getFields()) {
			JPanel horizLayout = new JPanel();
			horizLayout.setLayout(new BoxLayout(horizLayout, BoxLayout.X_AXIS));
			horizLayout.setSize(horizLayout.getWidth(), 30);
			
			JLabel label = new JLabel(field.getTitle());
			label.setSize(120, 20);
			horizLayout.add(label);
			
			textFields[col] = new JTextField();
			label.setSize(label.getWidth(), 20);
			textFields[col].setColumns(10);
			textFields[col].addFocusListener(new FormTextFieldFocusListener(col, model));
			textFields[col].addKeyListener(new FormTextFieldChangeListener(col, model));
			if (model.getSelectedCol() == col) textFields[col].requestFocusInWindow();
			
			horizLayout.add(textFields[col]);
			
			formPanel.add(horizLayout);
			
			++col;
		}
	}
	
	private void loadValues() {
		for (int col = 0; col < textFields.length; col++) {
			textFields[col].setText(model.getData(model.getSelectedRow(), col));
		}
	}
	
	public void youreInFocus() {
		if (textFields == null) return;
		loadValues();
		textFields[model.getSelectedCol()].requestFocusInWindow();
	}
	
	class FormTextFieldChangeListener implements KeyListener {
		private final int col;
		private final CurrentDataModel model;
		
		public FormTextFieldChangeListener(int col, CurrentDataModel model) {
			this.col = col;
			this.model = model;
		}
		
		@Override
		public void keyTyped(KeyEvent e) { }

		@Override
		public void keyPressed(KeyEvent e) { }

		@Override
		public void keyReleased(KeyEvent e) {
			String data = ((JTextField) e.getSource()).getText();
			model.updateData(model.getSelectedRow(), col, data);
		}
	}
	
	class FormTextFieldFocusListener implements FocusListener {
		private final int col;
		private final CurrentDataModel model;
		
		public FormTextFieldFocusListener(int col, CurrentDataModel model) {
			this.col = col;
			this.model = model;
		}
		
		@Override
		public void focusGained(FocusEvent e) {
			model.changeSelection(model.getSelectedRow(), col);
		}

		@Override
		public void focusLost(FocusEvent e) { }
		
	}
}
