package imageEditor;

import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

class Pixel {
	public int red;
	public int green;
	public int blue;
	
	public Pixel(int r, int g, int b) {
		red = r;
		green = g;
		blue = b;
	}
	
	public Pixel inverse() {
		return inverse(255);
	}
	
	public Pixel inverse(int max) {
		return new Pixel(max-red, max-green, max-blue);
	}
	
	public String toString() {
		return red + " " + green + " " + blue + " ";
	}
}

public class PixelMap {
	public int width;
	public int height;
	public int maxColorValue = 255;
	public Pixel[] pixels;
	private PushbackReader stream;
	
	public PixelMap(int width, int height, Pixel[] pixels) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}
	
	public PixelMap(String path) throws Exception {
		try {
			stream = new PushbackReader(new FileReader(path));
		} catch (Exception e) {
			throw new Error("File not found.");
		}
		
		try {
			readHeader();
			pixels = new Pixel[width*height];
			readPixels();
		} catch (Exception e) {
			throw new Exception("File format not recognized.");
		}
	}
	
	private PixelMap readHeader() throws Exception {
		return
			readMagicNumber()
			.readDimensions()
			.readMaxColorValue();
	}
	
	private PixelMap readMagicNumber() throws Exception {
		if (!readWord().equals("P3")) {
			throw new Exception("File format not recognized.");
		}
		skipSeparator();
		return this;
	}
	
	private PixelMap readDimensions() throws NumberFormatException, IOException {
		width = Integer.parseInt(readWord());
		skipSeparator();
		height = Integer.parseInt(readWord());
		skipSeparator();
		return this;
	}
	
	private PixelMap readMaxColorValue() throws NumberFormatException, IOException {
		maxColorValue = Integer.parseInt(readWord());
		skipSeparator();
		return this;
	}
	
	private PixelMap readPixels() throws NumberFormatException, IOException {
		for (int p = 0; p < width * height; p++) {
			int r = Integer.parseInt(readWord());
			skipSeparator();

			int g = Integer.parseInt(readWord());
			skipSeparator();
			
			int b = Integer.parseInt(readWord());
			skipSeparator();
			
			pixels[p] = new Pixel(r, g, b);
		}
		return this;
	}
	
	private String readWord() throws IOException {
		StringBuffer word = new StringBuffer();
		
		int c;
		while (isChar(c = stream.read())) {
			word.append((char) c);
		}
		stream.unread(c);
		return word.toString();
	}
	
	private PixelMap skipSeparator() throws IOException {
		int c;
		while (!isChar(c = stream.read()));
		stream.unread(c);
		return this;
	}
	
	private boolean isChar(char c) {
		return (c != '#' && c != '\n' && c != '\t' && c != ' ');
	}
	
	private boolean isChar(int c) {
		return isChar((char) c);
	}
	
	public PixelMap clone(PixelMap _pixelMap) {
		return new PixelMap(_pixelMap.width, _pixelMap.height, _pixelMap.pixels);			
	}
	
	public String headerString() {
		return "P3\n" + width + " " + height + "\n" + maxColorValue + "\n";
		
//		for (Pixel p : pixels) {
//			//out += p.toString();
//			System.out.println(p);
//		}
	}
}
