import java.io.FileReader;
import java.io.FileWriter;
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

	public Pixel(int v) {
		red = v;
		green = v;
		blue = v;
	}
	
	public Pixel invert() {
		return invert(255);
	}
	
	public Pixel invert(int max) {
		return new Pixel(max-red, max-green, max-blue);
	}
	
	public Pixel grayscale() {
		int v = (red + green + blue) / 3;
		return new Pixel(v);
	}
	
	public Pixel emboss(Pixel p) {
		int v = 128 + absMax(
			red - p.red,
			blue - p.blue,
			green - p.green
		);
		
		if (v > 255) v = 255;
		else if (v < 0) v = 0;
		
		return new Pixel(v);
	}
	
	public String toString() {
		return red + " " + green + " " + blue + " ";
	}
	
	public static int absMax(int...vals) {
		int max = 0;
		
		for (int v : vals) {
			if (Math.abs(v) > Math.abs(max)) {
				max = v;
			}
		}
		
		return max;
	}
	
	public static Pixel average(Pixel...pixels) {
		int r = 0,
			g = 0,
			b = 0;
		
		for (Pixel p : pixels) {
			r += p.red / pixels.length;
			g += p.green / pixels.length;
			b += p.blue / pixels.length;
		}
		
		return new Pixel(r, g, b);
	}
}

public class PixelMap {
	public int width;
	public int height;
	public int maxColorValue = 255;
	public Pixel[][] pixels;
	private PushbackReader stream;
	
	public PixelMap(int width, int height, Pixel[][] pixels) {
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
			pixels = new Pixel[height][width];
			readPixels();
		} catch (Exception e) {
			throw new Exception("File format not recognized.");
		}
	}
	
	public Pixel get(int r, int c) {
		return pixels[r][c];
	}
	
	public PixelMap set(int r, int c, Pixel p) {
		pixels[r][c] = p;
		return this;
	}
	
	public Pixel[] get(int r, int c1, int c2) {
		if (c2 >= pixels[r].length) c2 = pixels[r].length - 1;
		Pixel[] _pixels = new Pixel[c2 - c1 + 1];
		
		for (int i = 0; c1 <= c2; i++, c1++) {
			_pixels[i] = get(r, c1);
		}
		
		return _pixels;
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
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				int red = Integer.parseInt(readWord());
				skipSeparator();
	
				int green = Integer.parseInt(readWord());
				skipSeparator();
				
				int blue = Integer.parseInt(readWord());
				skipSeparator();
				
				pixels[r][c] = new Pixel(red, green, blue);
			}
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
	
	public PixelMap clone() {
		Pixel[][] _pixels = new Pixel[pixels.length][];
		for (int i = 0; i < pixels.length; i++)
			_pixels[i] = pixels[i].clone();

		return new PixelMap(width, height, _pixels);			
	}
	
	public String headerString() {
		return "P3\n" + width + " " + height + "\n" + maxColorValue + "\n";
		
	}
	
	public PixelMap writeToFile(String path) throws IOException {
		FileWriter w = new FileWriter(path);
		
		String header = "P3\n" + width + " " + height + "\n" + maxColorValue + "\n";
		w.write(header);
		
		for (Pixel[] row : pixels) {
			for (Pixel p : row) {
				w.write(p.toString());
			}
			
			w.write('\n');
		}
		
		w.close();
		return this;
	}
	
	public PixelMap forEach(CallBack cb) {
		for (int r = 0; r < pixels.length; r++) {
			for (int c = 0; c < pixels[r].length; c++) {
				cb.handle(this, pixels[r][c], r, c);
			}
		}
	
		return this;
	}
}
