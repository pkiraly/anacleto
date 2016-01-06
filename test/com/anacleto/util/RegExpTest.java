package com.anacleto.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class RegExpTest extends TestCase {

	private static String LINK = "href=(?:[\"\'])(.*?)(?:[\"\'])";
	
	public void testReg() throws Exception {
		String test= "egy link: href=\"valami\" es meg egy: href=\'mas\' vagy nem?";

		Pattern pattern = Pattern.compile(LINK);
		Matcher matcher = pattern.matcher(test);
		
		int start = 0;
		StringBuffer sb = new StringBuffer();
		while (matcher.find()){
			sb.append(test.substring(start,matcher.start(1)-1));
			sb.append('"' + modify(matcher.group(1)) + '"');
			start = matcher.end(1)+1;
		}
		sb.append(test.substring(start));
		System.out.println("result (rege): " + sb.toString());

	}

	public void testReg2() throws Exception {
		String LINK = "(href=[\"\'])(.*?)([\"\'])";
		String test= "egy link: href=\"valami\" es meg egy: href=\'mas\' vagy nem?";

		Pattern pattern = Pattern.compile(LINK);
		Matcher matcher = pattern.matcher(test);
		
		StringBuffer sb = new StringBuffer();
		while (matcher.find()){
			matcher.appendReplacement(sb, modify(matcher));
		}
		matcher.appendTail(sb);
		System.out.println("result (reg2): " + sb.toString());

	}

	private String modify (Matcher m) {
		return m.group(1) + modify(m.group(2)) + m.group(3);
	}

	private String modify (String text) {
		System.out.println("modify: " + text);
		return text.toUpperCase();
	}
	
	public void testName() throws Exception {
		String test= "egy link: href=\"valami\" es meg egy: href=\'mas\' vagy nem?";
		System.out.println("result (name): " + test.replaceAll(LINK, "href=\"" + modify("$1") + "\""));
	}

}
