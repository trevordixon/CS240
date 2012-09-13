public enum letter {
	A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z;

	public static letter valueOf(char c) {
		return letter.valueOf(Character.toString(Character.toUpperCase(c)));
	}
}
