package client;

import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JSplitPane;
import java.awt.GridBagConstraints;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import shared.Batch;

import client.listeners.DownloadListener;

public class Indexer {

	private JFrame frame;
	private CurrentDataModel model;
	private JSplitPane mainSplitter;
	private JSplitPane bottomSplitter;
	private MainImagePanel mainImagePanel;
	private TableEntryPane tableEntryPane;
	private FormEntryPanel formEntryPanel;
	private ImageNav imageNavPanel;
	
	private JMenuItem mntmDownloadBatch;
	
	private JButton btnZoomIn;
	private JButton btnZoomOut;
	private JButton btnToggleHighlights;
	private JButton btnInvertImage;
	private JButton btnSave;
	private JButton btnSubmit;

	
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
		try {
			FileInputStream fileIn = new FileInputStream(Communicator.getUsername() + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
           	model = (CurrentDataModel) in.readObject();
           	in.close();
           	fileIn.close();
		} catch (IOException i) {
			// ignore
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
		
		boolean saved = model != null;
		if (!saved) model = new CurrentDataModel();
		
		frame = new JFrame();

		if (saved) {
			Rectangle bounds = new Rectangle(
				(Integer) model.getProperty("frameX"),
				(Integer) model.getProperty("frameY"),
				(Integer) model.getProperty("frameWidth"),
				(Integer) model.getProperty("frameHeight")
			);
			 
			frame.setBounds(bounds);

		}
		else {
			frame.setSize(800, 600);
			frame.setLocationRelativeTo(null);
		}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		mainSplitter = new JSplitPane();
		mainSplitter.setContinuousLayout(true);
		mainSplitter.setResizeWeight(1.0);
		mainSplitter.setOrientation(JSplitPane.VERTICAL_SPLIT);
		if (saved) mainSplitter.setDividerLocation((Integer) model.getProperty("mainDivider"));
		GridBagConstraints gbc_mainSplitter = new GridBagConstraints();
		gbc_mainSplitter.fill = GridBagConstraints.BOTH;
		gbc_mainSplitter.gridx = 0;
		gbc_mainSplitter.gridy = 1;
		frame.getContentPane().add(mainSplitter, gbc_mainSplitter);
		
		bottomSplitter = new JSplitPane();
		bottomSplitter.setContinuousLayout(true);
		bottomSplitter.setResizeWeight(0.3);
		if (saved) bottomSplitter.setDividerLocation((Integer) model.getProperty("bottomDivider"));
		mainSplitter.setRightComponent(bottomSplitter);
		
		mainImagePanel = new MainImagePanel();
		mainSplitter.setLeftComponent(mainImagePanel);
		
		Box actions = Box.createHorizontalBox();
		GridBagConstraints gbc_actions = new GridBagConstraints();
		gbc_actions.insets = new Insets(3, 0, 2, 0);
		gbc_actions.anchor = GridBagConstraints.WEST;
		gbc_actions.gridx = 0;
		gbc_actions.gridy = 0;
		frame.getContentPane().add(actions, gbc_actions);
		
		btnZoomIn = new JButton("Zoom In");
		btnZoomIn.setEnabled(false);
		btnZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainImagePanel.image.zoomIn();
			}
		});
		actions.add(btnZoomIn);
		
		btnZoomOut = new JButton("Zoom Out");
		btnZoomOut.setEnabled(false);
		btnZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainImagePanel.image.zoomOut();
			}
		});
		actions.add(btnZoomOut);
		
		btnInvertImage = new JButton("Invert Image");
		btnInvertImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainImagePanel.image.toggleInvert();
			}
		});
		btnInvertImage.setEnabled(false);
		actions.add(btnInvertImage);
		
		btnToggleHighlights = new JButton("Toggle Highlights");
		btnToggleHighlights.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainImagePanel.image.toggleHighlight();
			}
		});
		btnToggleHighlights.setEnabled(false);
		actions.add(btnToggleHighlights);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveState();
			}
		});
		btnSave.setEnabled(false);
		actions.add(btnSave);
		
		btnSubmit = new JButton("Submit");
		btnSubmit.setEnabled(false);
		actions.add(btnSubmit);
		
		JTabbedPane imageNav = new JTabbedPane(JTabbedPane.TOP);
		bottomSplitter.setRightComponent(imageNav);
		
		JPanel fieldHelpPanel = new JPanel();
		imageNav.addTab("Field Help", null, fieldHelpPanel, null);
		
		imageNavPanel = new ImageNav();
		imageNav.addTab("Image Navigation", null, imageNavPanel, null);
		
		imageNav.setSelectedIndex(1);
		
		final JTabbedPane entry = new JTabbedPane(JTabbedPane.TOP);
		bottomSplitter.setLeftComponent(entry);
		
		tableEntryPane = new TableEntryPane();
		entry.addTab("Table Entry", null, tableEntryPane, null);
		
		formEntryPanel = new FormEntryPanel();
		entry.addTab("Form Entry", null, formEntryPanel, null);
		
		entry.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if (entry.getSelectedIndex() == 1)
					formEntryPanel.youreInFocus();
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		// Download Batch menu item
		mntmDownloadBatch = new JMenuItem("Download Batch");
		mnFile.add(mntmDownloadBatch);
		mntmDownloadBatch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					DownloadBatchDialog dialog = new DownloadBatchDialog(new DownloadListener(){
						// CallBack when an image is downloaded
						@Override
						public void callBack(Batch batch) {
							model.loadBatch(batch);
							loadBatch();
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
		
		if (model.getBatch() != null) loadBatch();
		
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) { }
			@Override
			public void windowClosing(WindowEvent e) {
				saveState();
			}
			@Override
			public void windowClosed(WindowEvent e) { }
			@Override
			public void windowIconified(WindowEvent e) { }
			@Override
			public void windowDeiconified(WindowEvent e) { }
			@Override
			public void windowActivated(WindowEvent e) { }
			@Override
			public void windowDeactivated(WindowEvent e) { }
		});
		
		// Detect application quitting on OS X
//		try {
//			Class.forName(com.apple.eawt.Application.class.getName());
//
//			com.apple.eawt.Application app = com.apple.eawt.Application.getApplication();
//			app.setQuitHandler(new com.apple.eawt.QuitHandler() {
//				@Override
//				public void handleQuitRequestWith(com.apple.eawt.AppEvent.QuitEvent qe, com.apple.eawt.QuitResponse qr) {
//					saveState();
//					qr.performQuit();
//				}
//			});
//		} catch (ClassNotFoundException e1) {
//			// Not on OS X, ignore
//		}
	}
	
	public void loadBatch() {
		mainImagePanel.setModel(model);
		tableEntryPane.setModel(model);
		formEntryPanel.setModel(model);
		imageNavPanel.setModel(model);
		
		mntmDownloadBatch.setEnabled(false);
		
		btnZoomIn.setEnabled(true);
		btnZoomOut.setEnabled(true);
		btnToggleHighlights.setEnabled(true);
		btnInvertImage.setEnabled(true);
		btnSave.setEnabled(true);
		btnSubmit.setEnabled(true);
	}
	
	public void saveState() {
		Rectangle bounds = frame.getBounds();
		model.setProperty("frameX", bounds.x);
		model.setProperty("frameY", bounds.y);
		model.setProperty("frameWidth", bounds.width);
		model.setProperty("frameHeight", bounds.height);
		
		model.setProperty("mainDivider", mainSplitter.getDividerLocation());
		model.setProperty("bottomDivider", bottomSplitter.getDividerLocation());
		
		try {
			FileOutputStream fileOut = new FileOutputStream(Communicator.getUsername() + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(model);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
}
