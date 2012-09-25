import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Words implements Iterable<String> {
	Set<String> words = new TreeSet<String>();;
	
	public Words() { }
	
	public Words(String path) {
		Scanner wordList = null;
		try {
			wordList = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			System.err.println("Dictionary file not found.");
			return;
		}
		
		while(wordList.hasNext()) {
			this.add(wordList.next());
		}
	}
	
//	public Words(Set<String> words) {
//		this.words = words;
//	}
	
	public Words add(String word) {
		words.add(word.toLowerCase());
		return this;
	}
	
	public Words ofLength(int length) {
		Words w = new Words();
		
		for (String word : this) {
			if (word.length() == length)
				w.add(word);
		}
		
		return w;
	}
	
	static int numberSetBits(int v) {
		int c;
		for (c = 0; v > 0; v >>= 1) c += v & 1;
		return c;
	}
	
	static int wordToBitmask(String w, char c) {
		c = Character.toLowerCase(c);
		
		int bitmask = 0;
		
		char[] cs = w.toCharArray();
		for (int i = 0, j = 1; i < cs.length; i++, j *= 2) {
			if (cs[i] == c)
				bitmask += j;
		}
		return bitmask;
	}
	
	public Words bestPartition(char c) {
		c = Character.toLowerCase(c);
		
		int bestBitmask = -1;
		Words bestPartition = new Words();
		
		Map<Integer, Words> partitions = new HashMap<Integer, Words>();
		for (String w : this) {
			int bitmask = wordToBitmask(w, c);
			
			if (!partitions.containsKey(bitmask)) {
				partitions.put(bitmask, new Words());
			}
			
			Words partition = partitions.get(bitmask);
			partition.add(w);
			
			if (partition.size() > bestPartition.size()) {
				bestPartition = partition;
				bestBitmask = bitmask;
			} else if (partition.size() == bestPartition.size()) {
				if (numberSetBits(bitmask) < numberSetBits(bestBitmask)) {
					bestPartition = partition;
					bestBitmask = bitmask;
				} else if (numberSetBits(bitmask) == numberSetBits(bestBitmask)) {
					if (bitmask > bestBitmask) {
						bestPartition = partition;
						bestBitmask = bitmask;
					}
				}
			}
		}
		
		return bestPartition;
	}

	@Override
	public Iterator<String> iterator() {
		return words.iterator();
	}
	
	public int size() {
		return words.size();
	}
	
	public String toString() {
		String out = "";
		for (String w : this) {
			out += w + "\n";
		}
		return out;
	}
	
	public static void main(String[] args) {
		System.out.println();
	}

}
