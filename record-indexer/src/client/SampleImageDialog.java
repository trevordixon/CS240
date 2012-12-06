package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import shared.Project;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class SampleImageDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Project p = new Project();
			p.setId(1);
			p.setTitle("1890 Census");
			SampleImageDialog dialog = new SampleImageDialog(p);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SampleImageDialog(Project project) {
		setResizable(false);
		setModal(true);
		setTitle("Sample image from " + project.getTitle());
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		setSize(640, 480);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				final JDialog dialog = this;
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dialog.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		String imageURL = Communicator.resource.path("batch/sample/" + project.getId()).get(String.class);
		
		try {
			Image image = ImageIO.read(new URL(imageURL)).getScaledInstance(640, 480, Image.SCALE_FAST);
			JLabel picLabel = new JLabel(new ImageIcon(image));
			getContentPane().add(picLabel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
