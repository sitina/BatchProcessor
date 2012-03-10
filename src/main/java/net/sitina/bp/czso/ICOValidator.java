package net.sitina.bp.czso;


/**
 * 
 * @author jirka
 * Takze, jak se ověřuje IČ? Například 69663963

první az sedmou číslici vynásobíme čísly 8, 7, 6, 5, 4, 3, 2 a součiny sečteme:
soucet = 6*8 + 9*7 + 6*6 + 6*5 + 3*4 + 9*3 + 6*2 = 228

spočítáme zbytek po dělení jedenácti: zbytek = soucet % 11
pro poslední osmou číslici c musí platit:
je-li zbytek 0 nebo 10, pak c = 1
je-li zbytek 1, pak c = 0
v ostatních případech je c = 11 - zbytek
 *
 *
 */
public final class ICOValidator {

	private ICOValidator() {
	}
	
	public static boolean validateICO(String icoString) {

		if (icoString.length() > 8) {
			return false;
		} 
		
		icoString = appendZeroes(icoString, 8);
		
		int[] ico = getICOAsArray(icoString);
		
	    // kontrolní součet
	    int a = 0;
	    for (int i = 0; i < 7; i++) {
	        a += ico[i] * (8 - i);
	    }

	    a = a % 11;

	    int c = 0;
	    if (a == 0) c = 1;	    
	    else if (a == 10) c = 1;
	    else if (a == 1) c = 0;
	    else c = 11 - a;

	    return (int) ico[7] == c;
	}
	
	private static int[] getICOAsArray(String ico) {
		int[] result = new int[8];
		
		for (int i = 0; i < 8; i++) {
			result[i] = Integer.valueOf(ico.substring(i, i + 1));
		}
		
		return result;
	}
	
	private static String appendZeroes(String originalString, int desiredLength) {
		while (originalString.length() < desiredLength) {
			originalString = "0" + originalString;
		}
		
		return originalString;
	}
	
}