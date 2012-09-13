import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Spell {
	public static String[] deletions(String word) {
		String[] words = new String[word.length()];
		for (int i = 0; i < words.length; i++) {
			words[i] = word.substring(0, i) + word.substring(i+1);
		}
		return words;
	}
	
	public static String[] transpositions(String word) {
		String[] words = new String[word.length() - 1];
		for (int i = 0; i < words.length; i++) {
			words[i] = word.substring(0, i) + word.charAt(i+1) + word.charAt(i) + word.substring(i+2);
		}
		return words;
	}
	
	public static String[] alterations(String word) {
		String[] words = new String[word.length() * 25];
		for (int i = 0; i < word.length(); i++) {
			String currentLetter = Character.toString(word.charAt(i));
			int j = 0;
			for (letter l : letter.values()) {
				String c = l.toString().toLowerCase();
				if (currentLetter.equalsIgnoreCase(c)) continue;
				words[i * 25 + j++] = word.substring(0, i) + c + word.substring(i+1);
			}
		}
		return words;
	}
	
	public static String[] insertions(String word) {
		String[] words = new String[(word.length() + 1) * 26];
		for (int i = 0; i <= word.length(); i++) {
			int j = 0;
			for (letter l : letter.values()) {
				String c = l.toString().toLowerCase();
				words[i * 26 + j++] = word.substring(0, i) + c + word.substring(i);
			}
		}
		return words;
	}
	
	public static Set<String> oneEditDistance(String word) {
		Set<String> words = new HashSet<String>();
		
		for (String s : deletions(word))       words.add(s);
		for (String s : transpositions(word))  words.add(s);			
		for (String s : alterations(word))     words.add(s);
		for (String s : insertions(word))      words.add(s);			
		
		return words;
	}
	
	public static Set<String> oneEditDistance(Set<String> origWords) {
		Set<String> words = new HashSet<String>();
		
		for (String word : origWords) {
			if (word.equals("")) continue;
			for (String s : oneEditDistance(word)) {
				words.add(s);
			}
		}
		
		return words;
	}
	
	public static String getSuggestion(String word, Words words) {
		word = word.toLowerCase();
		if (word.equals("")) return null;
		if (words.find(word) != null) return word;
		
		String suggestion = null;
		int count = 0;
		
		WordNode n;
		Set<String> words1 = oneEditDistance(word);
		for (String s : words1) {
			n = words.find(s);
			if (n != null) {
				if (n.count > count) {
					suggestion = s;
					count = n.count;
				} else if (n.count == count && suggestion.compareTo(s) > 0) {
					suggestion = s;
					count = n.count;
				}
			}
		}
		
		if (suggestion != null) return suggestion;
		
		for (String s : oneEditDistance(words1)) {
			n = words.find(s);
			if (n != null) {
				if (n.count > count) {
					suggestion = s;
					count = n.count;
				} else if (n.count == count && suggestion.compareTo(s) > 0) {
					suggestion = s;
					count = n.count;
				}
			}
		}
		
		return suggestion;
	}
	
	public static void main(String[] args) {
		String filePath = args[0],
			word = (args.length > 1) ? args[1] : "";
		
		Scanner sc = null;
		
		try {
			sc = new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
			System.err.println("Dictionary file not found.");
			return;
		}
		
		try {
			Words dict = new Words();
			while (sc.hasNext()) {
				String s = sc.next();
				if (s.matches("[A-Za-z]*"))
					dict.add(s);
			}
			
			String suggestion = getSuggestion(word, dict);
			if (suggestion == null) {
				System.out.println("No Suggestions!");
			} else {
				System.out.println(suggestion);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("The word must be a sequence of 0 or more letters");
		}
	}
}
