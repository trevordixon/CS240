import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Grep extends FileUtil {
	boolean requireSearchPattern() {
		return true;
	}

	public Grep(String[] args) {
		super(args);
	}
	
	@Override
	int process(File f) {
		int matches = 0;
		
		Scanner sc = null;
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while (sc.hasNextLine()) {
			String s = sc.nextLine();
			if (searchPattern.matcher(s).matches()) {
				if (matches == 0) {
					System.out.println("FILE: " + f.getAbsolutePath());
				}
				
				System.out.println(s);
				++matches;
			}
		}
		
		if (matches > 0) System.out.println("MATCHES: " + matches);
		
		return matches;
	}
	
	String summaryLabel() {
		return "TOTAL MATCHES: ";
	}

	static String[] testArgs() {
		String[] args = {"-r", "/Users/tdixon/Sites/ht", ".*\\.js", "console"};
		return args;
	}

	void printUsage() {
		System.out.println("USAGE: java Grep [-r] directoryName fileSelectionPattern substringSelectionPattern");
	}
	
	public static void main(String[] args) {
		new Grep(
//			testArgs()
			args
		);
	}
}
