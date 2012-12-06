package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImageNav extends JPanel {
	private CurrentDataModel model;

	public ImageNav() {
		super();
		setBackground(new Color(234, 234, 234));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (model == null) return;

		BufferedImage image = model.getImage();
		
		if (image == null) return;
		
		int height = getHeight(); 
		int width = (int) (((double) height/image.getHeight()) * image.getWidth());
		
		if (width > getWidth()) {
			width = getWidth();
			height = (int) (((double) width/image.getWidth()) * image.getHeight());
		}
		
		int x = (getWidth() - width) / 2;
		int y = (getHeight() - height) / 2;
		
		g.drawImage(image, x, y, width, height, null);
	}
	
	public void setModel(CurrentDataModel model) {
		this.model = model;
		repaint();
	}
}
