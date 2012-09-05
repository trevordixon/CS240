public class Pixel {
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
			green - p.green,
			blue - p.blue
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
			r += p.red;
			g += p.green;
			b += p.blue;
		}
		
		r = r / pixels.length;
		g = g / pixels.length;
		b = b / pixels.length;
		
		return new Pixel(r, g, b);
	}
}
