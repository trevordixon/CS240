package client;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import javax.swing.JList;

@SuppressWarnings("serial")
public class FormEntryPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public FormEntryPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JList<String> list = new JList<String>();
		add(list, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);

	}

	public void setModel(CurrentDataModel model) {
		
	}
}
