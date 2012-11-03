package shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

public class Util {
	public static String join(List<String> strings, String sep) {
		String result = "";
		for (String i : strings) {
			if (!result.equals("")) {
				result += sep;
			}
			
			result += i;
		}
		
		return result;
	}

	public static String join(String[] strings, String sep) {
		String result = "";
		for (String i : strings) {
			if (!result.equals("")) {
				result += sep;
			}
			
			result += i;
		}
		
		return result;
	}
	
	public static <T> T[] arrayConcat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}

}
