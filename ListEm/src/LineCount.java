import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LineCount extends FileUtil {
	public LineCount(String[] args) {
		super(args);
	}

	int process(File f) {
		int lines = 0;
		
		Scanner s = null;
		try {
			s = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (s.hasNextLine()) {
			s.nextLine();
			++lines;
		}
		
		System.out.println(lines + " " + f.getAbsolutePath());
		
		return lines;
	}

	String summaryLabel() {
		return "TOTAL: ";
	}

	static String[] testArgs() {
		String[] args = {"/Users/tdixon/Sites/ht", ".*"};
		return args;
	}

	void printUsage() {
		System.out.println("USAGE: java LineCount [-r] directoryName fileSelectionPattern");
	}
	
	public static void main(String[] args) {
		new LineCount(
//			testArgs()
			args
		);
	}
}
