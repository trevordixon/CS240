import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

class WordNode implements Trie.Node {
	protected int count = 0;
	protected Map<letter, WordNode> children = new EnumMap<letter, WordNode>(letter.class);
	private letter l;
	private int wordCount = -1;
	private int nodeCount = -1;
	
	public WordNode() {}
	
	public WordNode(letter l) {
		this.l = l;
	}
	
	@Override
	public int getValue() {
		return count;
	}
	
	public void add(String word) {
		wordCount = nodeCount = -1;
		letter l = letter.valueOf(word.charAt(0));
		
		if (word.length() == 1) {
			this.add(l);
			return;
		}
		
		WordNode n = children.get(l);
		if (n == null) {
			n = new WordNode(l);
			children.put(l, n);
		}
		
		n.add(word.substring(1));
	}
	
	private void add(letter l) {
		wordCount = nodeCount = -1;
		WordNode n = children.get(l);
		
		if (n == null) {
			n = new WordNode(l);
			children.put(l, n);
		}
		
		++n.count;
	}
	
	public WordNode find(String word) {
		if (word.equals("")) return null;
		
		letter l = letter.valueOf(word.charAt(0));
		
		if (word.length() == 1) {
			WordNode n = this.children.get(l);
			if (n == null || n.count == 0) return null;
			return n;
		}
		
		WordNode n = this.children.get(l);
		if (n == null) return null;
		return n.find(word.substring(1));
	}
	
	public int getWordCount() {
		if (wordCount > -1) return wordCount;
		
		wordCount = 0;
		
		for (Entry<letter, WordNode> entry : children.entrySet()) {
			WordNode n = entry.getValue();
			if (n.count > 0) ++wordCount;
			wordCount += n.getWordCount();
		}
		
		return wordCount;
	}
	
	public int getNodeCount() {
		if (nodeCount > -1) return nodeCount;
		
		nodeCount = children.size();
		
		for (Entry<letter, WordNode> entry : children.entrySet()) {
			WordNode n = entry.getValue();
			nodeCount += n.getNodeCount();
		}
		
		return nodeCount;
	}
	
	public String toString() {
		return "Node: letter=" + l + ", count=" + count;
	}
	
	public boolean equals(WordNode n) {
		return (
				count == n.count &&
				l.equals(n.l)
			);
	}

	public Iterator<WordNode> iterator() {
		return new Iterator<WordNode>() {
			private Deque<Iterator<WordNode>> iterators = new ArrayDeque<Iterator<WordNode>>();
			
			{ iterators.addFirst(WordNode.this.children.values().iterator()); }
			
			@Override
			public boolean hasNext() {
				return iterators.size() > 0 && iterators.peekFirst().hasNext();
			}

			@Override
			public WordNode next() {
				if (!hasNext()) throw new NoSuchElementException();
				Iterator<WordNode> i = iterators.peekFirst();
				WordNode n = i.next();
				
				if (!i.hasNext())
					iterators.removeFirst();
			
				if (n.children.size() > 0)
					iterators.addFirst(n.children.values().iterator());
				
				return n;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}			
		};
	}
}

public class Words extends WordNode implements Trie, Iterable<WordNode> {
	public int getNodeCount() {
		return super.getNodeCount() + 1;
	}
	
	public int hashCode() {
		return getWordCount() + getNodeCount();
	}
	
	@Override
	public boolean equals(Object o) {
		return equals((Words) o);
	}
	
	public boolean equals(Words w) {
		if (!(
			hashCode() == w.hashCode() &&
			getWordCount() == w.getWordCount() &&
			getNodeCount() == w.getNodeCount()
		)) return false;
		
		Iterator<WordNode> i1 = iterator();
		Iterator<WordNode> i2 = w.iterator();
		
		while(i1.hasNext() && i2.hasNext()) {
			if (!i1.next().equals(i2.next())) return false;
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		Words a = new Words();
		Words b = new Words();
		
		System.out.println(a.equals(b));
	}
}
