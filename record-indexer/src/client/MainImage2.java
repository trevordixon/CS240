package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImageOp;
import java.util.List;

import javax.swing.JPanel;

import shared.*;
import client.CurrentDataModel.CurrentValue;

@SuppressWarnings("serial")
public class MainImage2 extends JPanel {
	private CurrentDataModel model;
	private BufferedImage image;
	private BufferedImage invertedImage;
	private Double scalar = new Double(1);
	private Boolean highlight = true;
	private Boolean invert = false;

	private Color visibleColor = new Color(100, 100, 255, 80);
	
	private static final short[] invertTable;
	private static BufferedImageOp invertOp;
	static {
		invertTable = new short[256];
		for (int i = 0; i < 256; i++) {
			invertTable[i] = (short) (255 - i);
		}
		invertOp = new LookupOp(new ShortLookupTable(0, invertTable), null);
	}
	
	class Cell {
		public int row = -1;
		public int col = -1;
		
		public Cell() { }
		
		public Cell(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}
	
	public MainImage2() {
		super();
		setLayout(null);
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Cell cell = getCellAt((int) (e.getX()/scalar), (int) (e.getY()/scalar));
				
				if (cell != null) model.changeSelection(cell.row, cell.col);
			}

			@Override
			public void mousePressed(MouseEvent e) { }

			@Override
			public void mouseReleased(MouseEvent e) { }

			@Override
			public void mouseEntered(MouseEvent e) { }

			@Override
			public void mouseExited(MouseEvent e) { }
		});
	}
	
	public Cell getCellAt(int x, int y) {
		Cell cell = new Cell();
		
		Project project = model.getBatch().getProject();
		List<Field> fields = model.getBatch().getFields();
		
		if (y < project.getFirstycoord()) return null;
		for (int row = 0; row < project.getRecordsperimage(); row++) {
			int lower = project.getFirstycoord() + (row * project.getRecordheight());
			int upper = lower + project.getRecordheight();
			
			if (y >= lower && y <= upper) {
				cell.row = row;
				break;
			}
		}
		if (cell.row == -1) return null;
		
		int col = 0;
		for (Field field : fields) {
			int lower = field.getXcoord();
			int upper = lower + field.getWidth();
			
			if (x >= lower && x <= upper) {
				cell.col = col;
				break;
			}
			
			col++;
		}
		if (cell.col == -1) return null;
		
		return cell;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int width = (int) (image.getWidth() * scalar);
		int height = (int) (image.getHeight() * scalar);
		
		g.drawImage(invert ? invertedImage : image, 0, 0, width, height, null);
		if (highlight) paintHighlight(g);
	}
	
	public void setInvert(Boolean invert) {
		this.invert = invert;
		model.setProperty("imageInvert", invert);
		repaint();
	}
	
	public void toggleInvert() {
		this.setInvert(!invert);
	}
	
	public void setHighlight(Boolean highlight) {
		this.highlight = highlight;
		model.setProperty("imageHighlight", highlight);
		repaint();
	}
	
	public void toggleHighlight() {
		setHighlight(!highlight);
	}
	
	public void paintHighlight(Graphics g) {
		g.setColor(visibleColor);
		
		int row = model.getSelectedRow();
		int col = model.getSelectedCol();
		
		Project project = model.getBatch().getProject();
		List<Field> fields = model.getBatch().getFields();
		
		int x = scale(fields.get(col).getXcoord());
		int y = scale(project.getFirstycoord() + (row * project.getRecordheight()));
		int width = scale(fields.get(col).getWidth());
		int height = scale(project.getRecordheight());
		
		g.fillRect(x, y, width, height);		
	}
	
	private int scale(int value) {
		return (int) (value * scalar);
	}
	
	private void refresh() {

		//if (scalar == 1) scaledImage = scaledImage;
		//else scaledImage = image.getScaledInstance((int) (image.getWidth(null) * scalar), (int) (image.getHeight(null) * scalar), Image.SCALE_FAST);
		
		autoSetSize();
	}
	
	public void setModel(CurrentDataModel model) {
		this.model = model;
		
		Double scalar = (Double) model.getProperty("imageScalar");
		Boolean highlight = (Boolean) model.getProperty("imageHighlight");
		Boolean invert = (Boolean) model.getProperty("imageInvert");
		
		if (scalar != null) this.scalar = scalar;
		if (highlight != null) this.highlight = highlight;
		if (invert != null) this.invert = invert;
		
		image = model.getImage();
		invertedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		invertOp.filter(image, invertedImage);
		refresh();
		
		final MainImage2 self = this;
		model.addListener(new DataListener() {
			@Override
			public void selectionChange(int row, int col) {
				self.repaint();
			}

			@Override
			public void dataChange(int row, int col, CurrentValue data) { }
		});
	}
	
	public void zoomIn() {
		scalar *= 1.1;
		model.setProperty("imageScalar", scalar);
		refresh();
	}
	
	public void zoomOut() {
		scalar *= 0.9;
		model.setProperty("imageScalar", scalar);
		refresh();
	}
	
	public void setTransformOrigin(int x, int y) {
		
	}
	
	private void autoSetSize() {
		int width = (int) (image.getWidth(null) * scalar);
		int height = (int) (image.getHeight(null) * scalar);
		
		setSize(width, height);
	}
}
