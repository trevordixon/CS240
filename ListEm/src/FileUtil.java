import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
class NotADirectoryException extends Exception { }

@SuppressWarnings("serial")
class NotReadableException extends Exception { }

@SuppressWarnings("serial")
class UsageException extends Exception { }

public abstract class FileUtil {
	String path;
	File dir;
	Pattern filenamePattern;
	Pattern searchPattern;
	boolean recurse;

	boolean requireSearchPattern() {
		return false;
	}
	
	public FileUtil() { }
	
	public FileUtil(String[] args) {
		int i = 0;
		
		try {
			if (args[0].equals("-r")) {
				recurse = true;
				i = 1;
			}
		
			path = args[i];
			dir = new File(path);
			filenamePattern = Pattern.compile(args[i+1]);
			if (requireSearchPattern()) searchPattern = Pattern.compile(".*" + args[i+2] + ".*");
			else if (args.length > i+2) throw new UsageException();
		} catch (Exception e) {
			printUsage();
			return;
		}
		
		try {
			loopFiles();
		} catch (NotADirectoryException e) {
			System.out.println(dir.getPath() + " is not a directory");
		}
	}
	
	int loopFiles() throws NotADirectoryException {
		if (!dir.isDirectory()) throw new NotADirectoryException();
		return loopFiles(dir, true);
	}
	
	private int loopFiles(File dir, boolean printSummary) {
		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) return recurse;
				return filenamePattern.matcher(f.getName()).matches();
			}
		});
		
		int total = 0;
		for (File f : files) {
			if (f.isDirectory()) total += loopFiles(f, false);
			else if (!f.canRead()) {
				System.out.println("File " + f.getPath() + " is unreadable");
			}
			else total += process(f);
		}
		
		if (printSummary) System.out.println(summaryLabel() + total);
		
		return total;
	}
	
	abstract int process(File file);	
	abstract String summaryLabel();
	abstract void printUsage();
}
