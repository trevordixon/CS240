package client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainImagePanel extends JPanel {
	MainImage2 image = new MainImage2();
	CurrentDataModel model;
	
	public MainImagePanel() {
		super();
		
		this.setBackground(Color.GRAY);
		this.setLayout(null);
		this.add(image);
		
		final MainImagePanel panel = this;
		
		image.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { }

			@Override
			public void mousePressed(MouseEvent e) { }

			@Override
			public void mouseReleased(MouseEvent e) { }

			@Override
			public void mouseEntered(MouseEvent e) {
				panel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		image.addMouseMotionListener(new MouseMotionListener() {
			int oldX = -1;
			int oldY = -1;
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (oldX == -1) {
					oldX = e.getX();
					oldY = e.getY();
					return;
				}

				int imageX = image.getX() + (e.getX() - oldX);
				int imageY = image.getY() + (e.getY() - oldY);
				image.setLocation(imageX, imageY);
				
				model.setProperty("imageX", imageX);
				model.setProperty("imageY", imageY);
				
				MainImagePanel.this.calculateViewportPercent();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				oldX = -1;
				oldY = -1;
			}
			
		});
		
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					image.zoomIn();
					MainImagePanel.this.calculateViewportPercent();
				}
				else if (e.getWheelRotation() > 0) {
					image.zoomOut();
					MainImagePanel.this.calculateViewportPercent();
				}
			}
		});
		
	}
	
	public void calculateViewportPercent() {
		int imageX = image.getX();
		int imageY = image.getY();
		
		double panelWidth = MainImagePanel.this.getWidth();
		double panelHeight = MainImagePanel.this.getHeight();
		double imageWidth = image.getWidth();
		double imageHeight = image.getHeight();
		double imageEndX = imageX + imageWidth;
		double imageEndY = imageY + imageHeight;
		
		double xStartPercent = Math.max( (0 - imageX) / imageWidth, 0);
		double yStartPercent = Math.max( (0 - imageY) / imageHeight, 0);
		double xEndPercent = Math.max( (imageEndX - panelWidth) / imageWidth , 0);
		double yEndPercent = Math.max( (imageEndY - panelHeight) / imageHeight , 0);
		
		model.setViewportPercents(xStartPercent, yStartPercent, xEndPercent, yEndPercent);
	}
	
	public void setModel(CurrentDataModel model) {
		this.model = model;
		image.setModel(model);
		
		int panelWidth = this.getWidth();

		Integer imageX = (Integer) model.getProperty("imageX");
		Integer imageY = (Integer) model.getProperty("imageY");
		
		if (imageX == null) {
			imageX = (panelWidth - image.getWidth())/2;
			model.setProperty("imageX", imageX);
		}

		if (imageY == null) {
			imageY = 10;
			model.setProperty("imageY", imageY);
		}
		
		image.setLocation(imageX, imageY);
		image.repaint();
	}
}