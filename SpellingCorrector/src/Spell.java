
public class Spell {
	public static String[] deletions(String word) {
		String[] words = new String[word.length()];
		for (int i = 0; i < words.length; i++) {
			words[i] = word.substring(0, i) + word.substring(i+1);
		}
		return words;
	}
	
	public static void main(String[] args) {
		Words w = new Words();
		w.add("kick");
		w.add("kicks");
		w.add("kicker");
		w.add("apple");
		System.out.println(w.getWordCount());
		System.out.println(w.getNodeCount());
		for (String s : deletions("bird")) {
			System.out.println(s);
		}
	}
}
