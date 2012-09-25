import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Words implements Iterable<String> {
	public Set<String> words = new TreeSet<String>();
	
	public Words() { }
	
	public Words(String path) {
		Scanner wordList = null;
		try {
			wordList = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			// TODO: handle dictionary file not found
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
	
	public Partition bestPartition(char c) {
		c = Character.toLowerCase(c);
		
		Partition bestPartition = null;
		
		Map<Integer, Partition> partitions = new HashMap<Integer, Partition>();
		for (String w : this) {
			int bitmask = Partition.wordToBitmask(w, c);
			
			if (!partitions.containsKey(bitmask)) {
				partitions.put(bitmask, new Partition(bitmask));
			}
			
			Partition partition = partitions.get(bitmask);
			partition.words.add(w);
			
			if (bestPartition == null) {
				bestPartition = partition;
				continue;
			}
			
			if (partition.size() > bestPartition.size()) {
				bestPartition = partition;
			} else if (partition.size() == bestPartition.size()) {
				if (partition.letterCount() < bestPartition.letterCount()) {
					bestPartition = partition;
				} else if (partition.letterCount() == bestPartition.letterCount()) {
					if (partition.bitmask > bestPartition.bitmask) {
						bestPartition = partition;
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
