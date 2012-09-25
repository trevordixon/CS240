import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

class UsedLetters {
	Set<Character> used = new TreeSet<Character>();
	
	public void add(char c) {
		used.add(c);
	}
	
	public boolean contains(char c) {
		return used.contains(c);
	}
	
	public String toString() {
		String out = "";
		
		Iterator<Character> it = used.iterator();
		boolean first = true;
		while (it.hasNext()) {
			if (!first) out += " ";
			out += it.next();
			first = false;
		}
		
		return out;
	}
}

public class EvilHangman {
	static String partialWord(Words words, UsedLetters u) {
		String out = "";
		
		char[] w = ((String) words.words.toArray()[0]).toCharArray();
		for (char c : w) {
			if (u.contains(c))
				out += c;
			else
				out += "-";
		}
		
		return out;
	}
	
	public static void main(String[] args) {
		int remainingGuesses = -1;
		int wordLength = -1;
		
		try {
			wordLength = Integer.parseInt(args[0]);
			remainingGuesses = Integer.parseInt(args[1]);
			
			if (wordLength < 2 || remainingGuesses < 1 || args.length != 2) {
				throw new IllegalArgumentException();
			}
		} catch (Exception e) {
			System.out.println("USAGE: java EvilHangman wordLength guesses\n\twordLength is the length of the word you want (integer >= 2)\n\tguesses is the number of guesses you want (integer >= 1)");
			return;
		}
		
		int correctlyGuessedLetters = 0;

		Words words = new Words("dictionary.txt").ofLength(wordLength);
		
		if (words.size() == 0) {
			System.out.println("No words available of that length");
			return;
		}
		
		UsedLetters usedLetters = new UsedLetters();
		
		Scanner in = new Scanner(System.in);
		while (remainingGuesses > 0) {			
			System.out.println("You have " + remainingGuesses + " guess" + (remainingGuesses == 1 ? "" : "es") + " left");
			System.out.println("Used letters: " + usedLetters);
			System.out.println("Word: " + partialWord(words, usedLetters));
			System.out.print("Enter guess: ");
			
			String guess = in.next().toLowerCase();
			char c;

			// make sure their input is okay
			if (guess.length() > 1) {
				System.out.println("Invalid input\n");
				continue;
			} else {
				c = guess.charAt(0);
				
				if (!Character.isLowerCase(c)) {
					System.out.println("Invalid input\n");
					continue;
				}
			}
			
			if (usedLetters.contains(c)) {
				System.out.println("You already used that letter\n");
				continue;
			}
			
			// guess is valid
			usedLetters.add(c);
			
			Partition p = words.bestPartition(c);
			words = p.words;
			
			int lc = p.letterCount();
			if (lc > 0) {
				correctlyGuessedLetters += lc;
				
				if (correctlyGuessedLetters == wordLength) {
					System.out.println("You won!");
					System.out.println("The word was: " + words.words.toArray()[0]);
					return;
				} else {
					System.out.println("Yes, there " + (lc == 1 ? "is" : "are") + " " + lc + " " + c + (lc == 1 ? "" : "'s"));
				}
			} else {
				--remainingGuesses;
				
				if (remainingGuesses == 0) {
					System.out.println("You lost!");
					System.out.println("The word was: " + words.words.toArray()[0]);
					return;
				}
				
				System.out.println("No, there are no " + c + "'s");
			}
			
			System.out.println();
		}
	}
}
