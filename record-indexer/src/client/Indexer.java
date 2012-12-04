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

import shared.Batch;

import client.listeners.DownloadListener;

public class Indexer {

	private JFrame frame;
	private CurrentDataModel model;
	
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
		
		final MainImagePanel mainImagePanel = new MainImagePanel();
		mainSplitter.setLeftComponent(mainImagePanel);
		
		Box actions = Box.createHorizontalBox();
		GridBagConstraints gbc_actions = new GridBagConstraints();
		gbc_actions.insets = new Insets(3, 0, 2, 0);
		gbc_actions.anchor = GridBagConstraints.WEST;
		gbc_actions.gridx = 0;
		gbc_actions.gridy = 0;
		frame.getContentPane().add(actions, gbc_actions);
		
		JButton btnZoomIn = new JButton("Zoom In");
		btnZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainImagePanel.image.zoomIn();
			}
		});
		actions.add(btnZoomIn);
		
		JButton btnZoomOut = new JButton("Zoom Out");
		btnZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainImagePanel.image.zoomOut();
			}
		});
		actions.add(btnZoomOut);
		
		JButton btnInvertImage = new JButton("Invert Image");
		actions.add(btnInvertImage);
		
		JButton btnToggleHighlights = new JButton("Toggle Highlights");
		actions.add(btnToggleHighlights);
		
		JButton btnSave = new JButton("Save");
		actions.add(btnSave);
		
		JButton btnSubmit = new JButton("Submit");
		actions.add(btnSubmit);
		
		JTabbedPane imageNav = new JTabbedPane(JTabbedPane.TOP);
		bottomSplitter.setRightComponent(imageNav);
		
		JPanel fieldHelpPanel = new JPanel();
		imageNav.addTab("Field Help", null, fieldHelpPanel, null);
		
		JPanel imageNavPanel = new JPanel();
		imageNav.addTab("Image Navigation", null, imageNavPanel, null);
		
		JTabbedPane entry = new JTabbedPane(JTabbedPane.TOP);
		bottomSplitter.setLeftComponent(entry);
		
		final TableEntryPane tableEntryPane = new TableEntryPane();
		entry.addTab("Table Entry", null, tableEntryPane, null);
		
		final FormEntryPanel formEntryPanel = new FormEntryPanel();
		entry.addTab("Form Entry", null, formEntryPanel, null);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		// Download Batch menu item
		final JMenuItem mntmDownloadBatch = new JMenuItem("Download Batch");
		mnFile.add(mntmDownloadBatch);
		mntmDownloadBatch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					DownloadBatchDialog dialog = new DownloadBatchDialog(new DownloadListener(){
						// CallBack when an image is downloaded
						@Override
						public void callBack(Batch batch) {
							model = new CurrentDataModel(batch);
							
							mainImagePanel.setModel(model);
							tableEntryPane.setModel(model);
							formEntryPanel.setModel(model);

							mntmDownloadBatch.setEnabled(false);
						}
					});
					
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
