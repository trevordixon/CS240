package imageEditor;

public class ImageEditor {
	public static PixelMap inverse(PixelMap _pixelMap) {
		PixelMap pixelMap = _pixelMap.clone();
		
		pixelMap.forEach(new CallBack() {
			public void handle(PixelMap pixelMap, Pixel p, int r, int c) {
				pixelMap.set(r, c, p.invert());
			}
		});
		
		return pixelMap;
	}
	
	public static PixelMap grayscale(PixelMap _pixelMap) {
		PixelMap pixelMap = _pixelMap.clone();

		pixelMap.forEach(new CallBack() {
			public void handle(PixelMap pixelMap, Pixel p, int r, int c) {
				pixelMap.set(r, c, p.grayscale());
			}
		});
		
		return pixelMap;
	}
	
	public static PixelMap emboss(PixelMap _pixelMap) {
		PixelMap pixelMap = _pixelMap.clone();

		pixelMap.forEach(new CallBack() {
			public void handle(PixelMap pixelMap, Pixel p, int r, int c) {
				pixelMap.set(r, c, p.grayscale());
			}
		});
		
		return pixelMap;
	}
	
	public static void main(String[] args) throws Exception {
		PixelMap pixelMap = new PixelMap("src/imageEditor/pic.ppm");
		
		pixelMap.writeToFile("src/imageEditor/copy.ppm");
		inverse(pixelMap).writeToFile("src/imageEditor/inverse.ppm");
		grayscale(pixelMap).writeToFile("src/imageEditor/gray.ppm");
		emboss(pixelMap).writeToFile("src/imageEditor/emboss.ppm");
	}
}
