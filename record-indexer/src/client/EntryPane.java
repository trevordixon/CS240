package client;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class EntryPane extends JTabbedPane {
	public EntryPane() {
		super(JTabbedPane.TOP);

		TableEntryPane tableEntryPane = new TableEntryPane();
		addTab("Table Entry", null, tableEntryPane, null);
		
		JPanel formEntryPanel = new JPanel();
		addTab("Form Entry", null, formEntryPanel, null);

	}
}
