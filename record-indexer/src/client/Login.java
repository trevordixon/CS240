package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dialog.ModalityType;
import java.util.Scanner;

@SuppressWarnings("serial")
public class Login extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField usernameField;
	private JPasswordField passwordField;
	private boolean loggedin = false;
	
	/**
	 * Create the dialog.
	 */
	public Login() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setResizable(false);
		setSize(318, 134);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblUsername = new JLabel("Username:");
			GridBagConstraints gbc_lblUsername = new GridBagConstraints();
			gbc_lblUsername.anchor = GridBagConstraints.EAST;
			gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
			gbc_lblUsername.gridx = 0;
			gbc_lblUsername.gridy = 0;
			contentPanel.add(lblUsername, gbc_lblUsername);
		}
		{
			usernameField = new JTextField();
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 5, 0);
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 0;
			contentPanel.add(usernameField, gbc_textField);
			usernameField.setColumns(10);
		}
		{
			JLabel lblPassword = new JLabel("Password:");
			GridBagConstraints gbc_lblPassword = new GridBagConstraints();
			gbc_lblPassword.anchor = GridBagConstraints.EAST;
			gbc_lblPassword.insets = new Insets(0, 0, 0, 5);
			gbc_lblPassword.gridx = 0;
			gbc_lblPassword.gridy = 1;
			contentPanel.add(lblPassword, gbc_lblPassword);
		}
		{
			passwordField = new JPasswordField();
			GridBagConstraints gbc_passwordField = new GridBagConstraints();
			gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
			gbc_passwordField.gridx = 1;
			gbc_passwordField.gridy = 1;
			contentPanel.add(passwordField, gbc_passwordField);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton exitButton = new JButton("Exit");
				exitButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Login.this.setVisible(false);
						Login.this.dispose();
					}
				});
				buttonPane.add(exitButton);
			}
			{
				JButton loginButton = new JButton("Login");
				loginButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String username = usernameField.getText();
						String password = passwordField.getText();
						
						MultivaluedMap formData = new MultivaluedMapImpl();
						formData.add("username", username);
						formData.add("password", password);

						try {
							String response = Communicator.resource.path("user/validate").type(MediaType.APPLICATION_FORM_URLENCODED).post(String.class, formData);
							
							Communicator.setUsernameAndPassword(username, password);
							loggedin = true;
							
							Scanner sc = new Scanner(response);
							sc.next();
							String firstName = sc.next();
							String lastName = sc.next();
							String numIndexed = sc.next();
							
							JOptionPane.showMessageDialog(getContentPane(), "Welcome " + firstName + " " + lastName + ".\nYou have indexed " + numIndexed + " records.", "Welcome", JOptionPane.PLAIN_MESSAGE);
							
							Login.this.setVisible(false);
							Login.this.dispose();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(getContentPane(), "Wrong username and password.", "Failed", JOptionPane.WARNING_MESSAGE);
						}
						
					}
				});
				buttonPane.add(loginButton);
				getRootPane().setDefaultButton(loginButton);
			}
		}
	}
	
	boolean showDialog() {
		setVisible(true);
		return loggedin;
	}

}
