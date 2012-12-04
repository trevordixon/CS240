package client;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class HighlightButton extends JButton {
	private boolean visible = false;
	Color visibleColor = new Color(100, 100, 255, 80);
	Color invisibleColor = new Color(100, 100, 255, 0);
	
	@Override
	public void show() {
		visible = true;
		this.repaint();
	}

	@Override
	public void hide() {
		visible = false;
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(visible ? visibleColor : invisibleColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
}
