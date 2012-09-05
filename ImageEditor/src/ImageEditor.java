import java.io.IOException;

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
	
	public static PixelMap emboss(final PixelMap _pixelMap) {
		PixelMap pixelMap = _pixelMap.clone();

		pixelMap.forEach(new CallBack() {
			public void handle(PixelMap pixelMap, Pixel p, int r, int c) {
				if (r == 0 || c == 0) {
					pixelMap.set(r, c, new Pixel(128));
					return;
				}
				
				Pixel embossed = p.emboss(_pixelMap.get(r-1, c-1));
				pixelMap.set(r, c, embossed);
			}
		});
		
		return pixelMap;
	}
	
	public static PixelMap blur(PixelMap _pixelMap, final int radius) {
		PixelMap pixelMap = _pixelMap.clone();

		pixelMap.forEach(new CallBack() {
			public void handle(PixelMap pixelMap, Pixel p, int r, int c) {
				pixelMap.set(r, c, Pixel.average(pixelMap.get(r, c, c + radius)));
			}
		});
		
		return pixelMap;
	}
	
	public enum Command {
		GRAYSCALE, INVERT, EMBOSS, MOTIONBLUR;
	}
	
	public static void main(String[] args) {
//		if (args.length == 0) {
//			String[] _args = {
//				"/Users/tdixon/Documents/CS240/ImageEditor/src/pic.ppm",
//				"/Users/tdixon/Documents/CS240/ImageEditor/src/blurry.ppm",
//				"motionblur",
//				"7"
//			};
//			
//			args = _args;
//		}
		
		String in, out;
		Command command;
		int	blurRadius = 0;
		
		try {
			in = args[0];
			out = args[1];
			command = Command.valueOf(args[2].toUpperCase());
			
			if (command.equals(Command.MOTIONBLUR)) {
				blurRadius = Integer.parseInt(args[3]);
				if (blurRadius <= 0)
					printError("motionblur-length must be greater than 0.");
			}
		} catch (Exception e) {
			printUsage();
			return;
		}
		
		PixelMap pixelMap;
		try {
			pixelMap = new PixelMap(in);
		} catch (Exception e) {
			printError(e.getMessage());
			return;
		}
		
		try {
			switch (command) {
				case GRAYSCALE:
					pixelMap = grayscale(pixelMap);
					break;
				case INVERT:
					pixelMap = inverse(pixelMap);
					break;
				case EMBOSS:
					pixelMap = emboss(pixelMap);
					break;
				case MOTIONBLUR:
					pixelMap = blur(pixelMap, blurRadius);
					break;
				default:
					printUsage();
					return;
			}
		} catch (Exception e) {
			printError("Unexpected error: " + e.getMessage());
		}
		
		try {
			pixelMap.writeToFile(out);
		} catch (IOException e) {
			printError("Error writing to file '" + out + "'");
		}
	}
	
	public static void printUsage() {
		System.out.println("USAGE: java ImageEditor  in-file out-file (grayscale|invert|emboss|motionblur motionblur-length)");
	}
	
	public static void printError(String err) {
		System.out.println(err);
	}
}
