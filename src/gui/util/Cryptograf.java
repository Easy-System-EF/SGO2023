package gui.util;

public class Cryptograf {

	static final int key = 3;
	
	public static String senhaCryp = "";
	public static String senhaDesCryp = "";
	public static String senhaCrypII = "";
	public static String senhaDesCrypII = "";
	
	
	public static String criptografa(String str) {
		senhaCryp = "";
		char[] charsC = str.toCharArray();
		for (char c : charsC) {
			c += key;
			senhaCryp += c;
		}
		return senhaCryp;
	}
	
	public static String desCriptografa(String str) {
		senhaDesCryp = "";
		char[] charsD = str.toCharArray();
		for (char d : charsD) {
			d -= key;
			senhaDesCryp += d;
		}
		return senhaDesCryp;
	}

	public static String criptografaII(String str) {
		senhaCrypII = "";
		char[] charsC = str.toCharArray();
		for (char c : charsC) {
			c += key;
			senhaCrypII += c;
		}
		return senhaCrypII;
	}
	
	public static String desCriptografaII(String str) {
		senhaDesCrypII = "";
		char[] charsD = str.toCharArray();
		for (char d : charsD) {
			d -= key;
			senhaDesCrypII += d;
		}
		return senhaDesCrypII;
	}
}
