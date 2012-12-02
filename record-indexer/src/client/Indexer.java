package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JSplitPane;
import java.awt.GridBagConstraints;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Box;

public class Indexer {

	private JFrame frame;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Indexer window = new Indexer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Indexer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(300, 300, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		Box actions = Box.createHorizontalBox();
		GridBagConstraints gbc_actions = new GridBagConstraints();
		gbc_actions.insets = new Insets(3, 0, 2, 0);
		gbc_actions.anchor = GridBagConstraints.WEST;
		gbc_actions.gridx = 0;
		gbc_actions.gridy = 0;
		frame.getContentPane().add(actions, gbc_actions);
		
		JButton btnZoomIn = new JButton("Zoom In");
		actions.add(btnZoomIn);
		
		JButton btnZoomOut = new JButton("Zoom Out");
		actions.add(btnZoomOut);
		
		JButton btnInvertImage = new JButton("Invert Image");
		actions.add(btnInvertImage);
		
		JButton btnToggleHighlights = new JButton("Toggle Highlights");
		actions.add(btnToggleHighlights);
		
		JButton btnSave = new JButton("Save");
		actions.add(btnSave);
		
		JButton btnSubmit = new JButton("Submit");
		actions.add(btnSubmit);
		
		JSplitPane mainSplitter = new JSplitPane();
		mainSplitter.setResizeWeight(0.5);
		mainSplitter.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GridBagConstraints gbc_mainSplitter = new GridBagConstraints();
		gbc_mainSplitter.fill = GridBagConstraints.BOTH;
		gbc_mainSplitter.gridx = 0;
		gbc_mainSplitter.gridy = 1;
		frame.getContentPane().add(mainSplitter, gbc_mainSplitter);
		
		JSplitPane bottomSplitter = new JSplitPane();
		bottomSplitter.setResizeWeight(0.3);
		mainSplitter.setRightComponent(bottomSplitter);
		
		JTabbedPane imageNav = new JTabbedPane(JTabbedPane.TOP);
		bottomSplitter.setRightComponent(imageNav);
		
		JPanel panel = new JPanel();
		imageNav.addTab("Field Help", null, panel, null);
		
		JPanel panel_1 = new JPanel();
		imageNav.addTab("Image Navigation", null, panel_1, null);
		
		JTabbedPane entry = new JTabbedPane(JTabbedPane.TOP);
		bottomSplitter.setLeftComponent(entry);
		
		JPanel panel_2 = new JPanel();
		entry.addTab("Table Entry", null, panel_2, null);
		
		JPanel panel_3 = new JPanel();
		entry.addTab("Form Entry", null, panel_3, null);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		final JMenuItem mntmDownloadBatch = new JMenuItem("Download Batch");
		mnFile.add(mntmDownloadBatch);
		mntmDownloadBatch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmDownloadBatch.setEnabled(false);
					
				try {
					DownloadBatchDialog dialog = new DownloadBatchDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mnFile.add(mntmLogout);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mnFile.add(mntmQuit);
	}

}
