package shared;

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
}
