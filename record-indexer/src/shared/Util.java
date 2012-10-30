package shared;

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
}
