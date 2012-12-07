package client;

import javax.swing.JEditorPane;

import client.CurrentDataModel.CurrentValue;

@SuppressWarnings("serial")
public class HelpPane extends JEditorPane {
	CurrentDataModel model;
	
	public HelpPane() {
		setContentType("text/html");
		this.setEditable(false);
	}
	
	public void setModel(final CurrentDataModel model) {
		this.model = model;
		
		HelpPane.this.setText(model.getBatch().getFields().get(model.getSelectedCol()).getHelphtml());

		model.addListener(new DataListener() {
			@Override
			public void selectionChange(int row, int col) {
				HelpPane.this.setText(model.getBatch().getFields().get(col).getHelphtml());
			}

			@Override
			public void dataChange(int row, int col, CurrentValue data) { }
		});
	}
}
