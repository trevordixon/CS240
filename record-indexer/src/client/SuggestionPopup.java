package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import client.CurrentDataModel.CurrentValue;

@SuppressWarnings("serial")
public class SuggestionPopup extends JPopupMenu {
	CurrentDataModel model;
	JMenuItem mntmSeeSuggestions = new JMenuItem("See suggestions");
	
	public SuggestionPopup() {
		super();
		
		add(mntmSeeSuggestions);
		mntmSeeSuggestions.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new SuggestionDialog(model);
			}
		});
	}
	
	public void setModel(CurrentDataModel model) {
		this.model = model;
	}
	
	public void refresh() {
		CurrentValue data = model.getSelectedData();
		boolean hasSuggestions = data.suggestions != null && (data.suggestions.size() > 0);
		mntmSeeSuggestions.setEnabled(hasSuggestions);
	}
}
