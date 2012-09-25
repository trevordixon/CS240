public class Partition {
	public Words words = new Words();
	public final int bitmask;
	
	public Partition(int bitmask) {
		this.bitmask = bitmask;
	}
	
	public int size() {
		return words.size();
	}
	
	public int letterCount() {
		return numberSetBits(bitmask);
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
	
}
