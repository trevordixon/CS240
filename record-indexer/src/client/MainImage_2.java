package client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainImage_2 extends JPanel {
	CurrentDataModel model;
	Image image;
	Image scaledImage;
	double scalar = 1;
	
	public MainImage_2() {
		super();
		setLayout(null);
		System.out.println("HERE");
	}
	
	private void refresh() {
		if (scalar == 1) scaledImage = image;
		else scaledImage = image.getScaledInstance((int) (image.getWidth(null) * scalar), (int) (image.getHeight(null) * scalar), Image.SCALE_FAST);
		
		//imageLabel.setIcon(new ImageIcon(scaledImage));

		autoSetSize();
	}
	
	public void setImage(String url) {
		try {
			image = ImageIO.read(new URL(url));
			//refresh();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
		System.out.println("Paint me");
	}

	
	public void setModel(CurrentDataModel model) {
		this.model = model;
		
		setImage(model.batch.getUrl());
		
		repaint();
		
		
//		Project project = model.batch.getProject();
//		List<Field> fields = model.batch.getFields();
//		
//		for (int i = 0; i < project.getRecordsperimage(); i++) {
//			for (Field field : fields) {
//				HighlightButton highlight = new HighlightButton();
//				add(highlight);
//				setComponentZOrder(highlight, 0);
//				highlight.setBounds(field.getXcoord(), project.getFirstycoord() + project.getRecordheight() * i, field.getWidth(), project.getRecordheight());
//				highlight.addActionListener(highlightClick);
//			}
//		}
//		
//		repaint();
	}
	
	public void zoomIn() {
		scalar *= 1.1;
		refresh();
	}
	
	public void zoomOut() {
		scalar *= 0.9;
		refresh();
	}
	
	public void setTransformOrigin(int x, int y) {
		
	}
	
	private void autoSetSize() {
		int width = scaledImage.getWidth(null);
		int height = scaledImage.getHeight(null);
		
		//imageLabel.setSize(width, height);
		setSize(width, height);
	}
	
	ActionListener highlightClick = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			HighlightButton highlight = (HighlightButton) e.getSource();
			highlight.show();
		}
	};
}
