package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.ws.rs.core.MediaType;

import client.listeners.DownloadListener;

import com.sun.jersey.api.client.GenericType;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.Vector;

import shared.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class DownloadBatchDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	Vector<Project> projects = new Vector<Project>();
	JComboBox<Project> projectSelect;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DownloadBatchDialog dialog = new DownloadBatchDialog(new DownloadListener() {
				@Override
				public void callBack(Batch batch) {
					System.out.println(batch);
				}
			});
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DownloadBatchDialog(final DownloadListener listener) {
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setSize(389, 114);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{47, 186, 122, 0};
		gbl_contentPanel.rowHeights = new int[]{29, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblProject = new JLabel("Project:");
			GridBagConstraints gbc_lblProject = new GridBagConstraints();
			gbc_lblProject.anchor = GridBagConstraints.EAST;
			gbc_lblProject.insets = new Insets(0, 0, 0, 5);
			gbc_lblProject.gridx = 0;
			gbc_lblProject.gridy = 0;
			contentPanel.add(lblProject, gbc_lblProject);
		}
		{
			DefaultComboBoxModel<Project> projectsModel = new DefaultComboBoxModel<Project>(projects);
			projectSelect = new JComboBox<Project>(projectsModel);
			
			
			GridBagConstraints gbc_projectSelect = new GridBagConstraints();
			gbc_projectSelect.fill = GridBagConstraints.HORIZONTAL;
			gbc_projectSelect.insets = new Insets(0, 0, 0, 5);
			gbc_projectSelect.gridx = 1;
			gbc_projectSelect.gridy = 0;
			contentPanel.add(projectSelect, gbc_projectSelect);
		}
		{
			JButton btnViewSampleImage = new JButton("View Sample");
			btnViewSampleImage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Project selectedProject = (Project) projectSelect.getSelectedItem();
					SampleImageDialog dialog = new SampleImageDialog(selectedProject);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
			});
			GridBagConstraints gbc_btnViewSampleImage = new GridBagConstraints();
			gbc_btnViewSampleImage.anchor = GridBagConstraints.NORTHWEST;
			gbc_btnViewSampleImage.gridx = 2;
			gbc_btnViewSampleImage.gridy = 0;
			contentPanel.add(btnViewSampleImage, gbc_btnViewSampleImage);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				final JDialog dialog = this;
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dialog.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			{
				final JDialog dialog = this;
				JButton downloadButton = new JButton("Download");
				downloadButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Project selectedProject = (Project) projectSelect.getSelectedItem();
						Batch batch = Communicator.resource
							.path("batch/get/" + selectedProject.getId())
							.accept(MediaType.TEXT_XML_TYPE)
							.get(Batch.class);
						
						dialog.dispose();
						listener.callBack(batch);
					}
				});
				downloadButton.setActionCommand("Download");
				buttonPane.add(downloadButton);
				getRootPane().setDefaultButton(downloadButton);
			}
		}
		
		loadBatches();
	}

	private void loadBatches() {
		List<Project> projects = Communicator.resource
				.path("project")
				.accept(MediaType.TEXT_XML_TYPE)
				.get(new GenericType<List<Project>>(){});

		for (Project p : projects) {
			this.projects.addElement(p);
		}
		
		projectSelect.setSelectedIndex(0);
	}
	
}
