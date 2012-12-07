package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import client.listeners.ViewportPercentListener;

@SuppressWarnings("serial")
public class ImageNav extends JPanel {
	private CurrentDataModel model;
	double xStartPercent, yStartPercent, xEndPercent, yEndPercent;

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
		
		g.setColor(new Color(100, 100, 255, 80));
		g.fillRect(x + (int) (xStartPercent * width), y + (int) (yStartPercent * height), (int) ((1-xStartPercent-xEndPercent) * width), (int) ((1-yStartPercent-yEndPercent) * height));
	}
	
	public void setModel(CurrentDataModel model) {
		this.model = model;
		
		model.setViewportPercentListener(new ViewportPercentListener() {
			@Override
			public void change(double xStartPercent, double yStartPercent, double xEndPercent, double yEndPercent) {
				ImageNav.this.xStartPercent = xStartPercent;
				ImageNav.this.yStartPercent = yStartPercent;
				ImageNav.this.xEndPercent = xEndPercent;
				ImageNav.this.yEndPercent = yEndPercent;
				
				ImageNav.this.repaint();
			}
		});

		
		repaint();
	}
}
