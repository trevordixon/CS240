package spelling;

public enum letter {
	A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,
	SPACE(" "),
	DASH("-");
	
	private final String val;
	
	public static letter valueOf(char c) {
		if (c == ' ') return letter.SPACE;
		if (c == '-') return letter.DASH;
		
		try {
			return letter.valueOf(Character.toString(Character.toUpperCase(c)));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	private letter() {
		val = this.name();
	}
	
	private letter(String val) {
		this.val = val;
	}
	
	@Override public String toString() {
		return val;
	}
}
