package util;

import java.util.Locale;
import java.util.Random;

public class KeyGenerator {
	
	private final Random random = new Random();
	
	private final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final String lower = upper.toLowerCase(Locale.ROOT);
	private final String digits = "0123456789";
	private final String letterSet = upper+lower+digits;
	
	public String generateKey() {
		char[] buf = new char[15];
		char[] symbols = letterSet.toCharArray();
		for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
	}
	
}
