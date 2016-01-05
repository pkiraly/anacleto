package com.anacleto.util;

public class WildCardMatch {

	/**
	 * Determines if a word matches a wildcard pattern. 
	 * A wrapper method for wildcardEquals
	 * @param searchText The text to compare
	 * @param pattern The pattern
	 * @return
	 */
	public synchronized static boolean match(String searchText, String pattern) {
		return wildcardEquals(pattern, 0, searchText, 0);		
	}

	/***************************************************************************
	 * String equality with support for wildcards
	 **************************************************************************/

	public static final char WILDCARD_STRING = '*';

	public static final char WILDCARD_CHAR = '?';

	/**
	 * Determines if a word matches a wildcard pattern. <small>Work released by
	 * Granta Design Ltd after originally being done on company time.</small>
	 */
	public static final boolean wildcardEquals(String pattern, int patternIdx,
			String string, int stringIdx) {
		for (int p = patternIdx;; ++p) {
			for (int s = stringIdx;; ++p, ++s) {
				// End of string yet?
				boolean sEnd = (s >= string.length());
				// End of pattern yet?
				boolean pEnd = (p >= pattern.length());

				// If we're looking at the end of the string...
				if (sEnd) {
					// Assume the only thing left on the pattern is/are
					// wildcards
					boolean justWildcardStringsLeft = true;

					// Current wildcard position
					int wildcardSearchPos = p;
					// While we haven't found the end of the pattern,
					// and haven't encountered any non-wildcard characters
					while (wildcardSearchPos < pattern.length()
							&& justWildcardStringsLeft) {
						// Check the character at the current position
						char wildchar = pattern.charAt(wildcardSearchPos);
						// If it's not a wildcard character, then there is more
						// pattern information after this/these wildcards.

						if  (wildchar != WILDCARD_STRING) {
							justWildcardStringsLeft = false;
						} else {
							// Look at the next character
							wildcardSearchPos++;
						}
					}

					// This was a prefix wildcard search, and we've matched, so
					// return true.
					if (justWildcardStringsLeft) {
						return true;
					}
				}

				// If we've gone past the end of the string, or the pattern,
				// return false.
				if (sEnd || pEnd) {
					break;
				}

				// Match a single character, so continue.
				if (pattern.charAt(p) == WILDCARD_CHAR) {
					continue;
				}

				//
				if (pattern.charAt(p) == WILDCARD_STRING) {
					// Look at the character beyond the '*'.
					++p;
					// Examine the string, starting at the last character.
					for (int i = string.length(); i >= s; --i) {
						if (wildcardEquals(pattern, p, string, i)) {
							return true;
						}
					}
					break;
				}
				if (pattern.charAt(p) != string.charAt(s)) {
					break;
				}
			}
			return false;
		}
	}
}
