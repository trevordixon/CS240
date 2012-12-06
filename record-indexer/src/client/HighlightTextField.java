package client;

import java.awt.Graphics;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class HighlightTextField extends JTextField {
	public void paintComponent(Graphics g) {
		//this.setBackground(highlightColor);
		super.paintComponent(g);
	}
}
