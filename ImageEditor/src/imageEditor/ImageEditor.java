package imageEditor;

public class ImageEditor {
	public static PixelMap invert(PixelMap pixelMap) {
		for (int i = 0; i < pixelMap.pixels.length; i++) {
			pixelMap.pixels[i] = pixelMap.pixels[i].inverse();
		}
		
		return pixelMap;
	}
	
	public static void main(String[] args) throws Exception {
		PixelMap pixelMap = new PixelMap("src/imageEditor/pic.ppm");
		pixelMap = invert(pixelMap);
		pixelMap.toString();
	}
}
