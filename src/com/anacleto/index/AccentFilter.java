/*
 * Created on May 18, 2005
 *
 */
package com.anacleto.index;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections.FastHashMap;
import org.apache.commons.collections.FastTreeMap;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * Replace accented chars from a token stream.
 */
public class AccentFilter extends TokenFilter {
	/*
	private static final Collection validChars = new HashSet();
	private static final String validCharsStr = "abcdefghijklmnopqrstuvwxyz\u00E6\u00F8\u00E5"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ\u00C6\u00D8\u00C5"
			+ "0123456789"
			+ "-";
	static {
		for (int i = 0; i < validCharsStr.length(); i++)
			validChars.add(new Character(validCharsStr.charAt(i)));
	}
	*/
	
	/**
	 * Default accent conversion map. 
	 */
	public static final Map getDefaultAccentMap(){
	
		Map defaultAccentMap = new FastTreeMap();
//		defaultAccentMap.put(new Character('\u00C0'), "A");
//		defaultAccentMap.put(new Character('\u00C1'), "A");
//		defaultAccentMap.put(new Character('\u00C2'), "A");
//		defaultAccentMap.put(new Character('\u00C3'), "A");
		defaultAccentMap.put(new Character('\u00E0'), "a");
		defaultAccentMap.put(new Character('\u00E1'), "a");
		defaultAccentMap.put(new Character('\u00E2'), "a");
		defaultAccentMap.put(new Character('\u00E3'), "a");
		defaultAccentMap.put(new Character('\u00E4'), "a");

		defaultAccentMap.put(new Character('\u00E7'), "c");

//		defaultAccentMap.put(new Character('\u00C8'), "E");
//		defaultAccentMap.put(new Character('\u00C9'), "E");
//		defaultAccentMap.put(new Character('\u00CA'), "E");
//		defaultAccentMap.put(new Character('\u00CB'), "E");
		defaultAccentMap.put(new Character('\u00E8'), "e");
		defaultAccentMap.put(new Character('\u00E9'), "e");
		defaultAccentMap.put(new Character('\u00EA'), "e");
		defaultAccentMap.put(new Character('\u00EB'), "e");
		
		
//		defaultAccentMap.put(new Character('\u00CC'), "I");
//		defaultAccentMap.put(new Character('\u00CD'), "I");
//		defaultAccentMap.put(new Character('\u00CE'), "I");
//		defaultAccentMap.put(new Character('\u00CF'), "I");
		defaultAccentMap.put(new Character('\u00EC'), "i");
		defaultAccentMap.put(new Character('\u00ED'), "i");
		defaultAccentMap.put(new Character('\u00EE'), "i");
		defaultAccentMap.put(new Character('\u00EF'), "i");

//		defaultAccentMap.put(new Character('\u00D1'), "N");
		defaultAccentMap.put(new Character('\u00F1'), "n");

//		defaultAccentMap.put(new Character('\u00D2'), "O");
//		defaultAccentMap.put(new Character('\u00D3'), "O");
//		defaultAccentMap.put(new Character('\u00D4'), "O");
//		defaultAccentMap.put(new Character('\u00D5'), "O");
//		defaultAccentMap.put(new Character('\u00D6'), "O");
		defaultAccentMap.put(new Character('\u00F2'), "o");
		defaultAccentMap.put(new Character('\u00F3'), "o");
		defaultAccentMap.put(new Character('\u00F4'), "o");
		defaultAccentMap.put(new Character('\u00F5'), "o");
		defaultAccentMap.put(new Character('\u00F6'), "o");

//		defaultAccentMap.put(new Character('\u0150'), "O");
		defaultAccentMap.put(new Character('\u0151'), "o");

		defaultAccentMap.put(new Character('\u015F'), "s");

//		defaultAccentMap.put(new Character('\u00D9'), "U");
//		defaultAccentMap.put(new Character('\u00DA'), "U");
//		defaultAccentMap.put(new Character('\u00DB'), "U");
//		defaultAccentMap.put(new Character('\u00DC'), "U");
		defaultAccentMap.put(new Character('\u00F9'), "u");
		defaultAccentMap.put(new Character('\u00FA'), "u");
		defaultAccentMap.put(new Character('\u00FB'), "u");
		defaultAccentMap.put(new Character('\u00FC'), "u");

//		defaultAccentMap.put(new Character('\u0170'), "U");
		defaultAccentMap.put(new Character('\u0171'), "u");
		
//		defaultAccentMap.put(new Character('\u00DD'), "Y");
		defaultAccentMap.put(new Character('\u00FD'), "y");
		defaultAccentMap.put(new Character('\u00FF'), "y");

//		defaultAccentMap.put(new Character('\u00C6'), "AE");
		defaultAccentMap.put(new Character('\u00E6'), "ae");
//		defaultAccentMap.put(new Character('\u00D8'), "OE");
		defaultAccentMap.put(new Character('\u00F8'), "oe");
//		defaultAccentMap.put(new Character('\u00C5'), "AA");
		defaultAccentMap.put(new Character('\u00E5'), "aa");
		
		return defaultAccentMap;
	}

	private Map accentMap = new  FastHashMap();
	
	private Token token = null;

	public AccentFilter(TokenStream in, Map accentMap) {
		super(in);
		this.accentMap = accentMap;
	}
	
	public Token next() throws IOException {
		if ((token = input.next()) == null)
			return null;
		String s = process(token.termText());
		if (!s.equals(token.termText())) {
			return new Token(s, token.startOffset(), token.endOffset(), 
					token.type());
		} else {
			return token;
		}
	}
	
	private String process(String str) {
		StringBuffer sb = new StringBuffer(str);
		
		// First check for accents
		for (int i = 0; i < sb.length(); i++) {
			Character c = new Character(sb.charAt(i));
			String rep = (String) accentMap.get(c);
			if (rep != null)
				sb.replace(i, i + 1, rep);
		}

		return sb.toString();
	}
}
