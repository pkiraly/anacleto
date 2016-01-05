package com.anacleto.util;

import java.text.Collator;
import java.util.*;

import org.apache.log4j.Logger;

import com.anacleto.base.Logging;

public class CharacterSorter {

	private static  Locale loc;
	private static  Logger log = Logging.getAdminLogger();
	
	private static SortedSet charSet;
	
	synchronized public static void initialize(Locale loc) {
		
		if (CharacterSorter.loc != null && CharacterSorter.loc == loc) 
			return;
		
		log.info("Initialize sorted character set from locale: " 
				+ loc.getDisplayName());
	
		CharacterSorter.loc = loc;
		
		charSet = new TreeSet(Collator.getInstance(loc));

		//TODO some unicode characters may remain outside
		for (int i = 0; i < 256*256; i++) {
			/*
			if (!Character.isDefined((char)i))
				continue;
			
			if (Character.isUpperCase((char)i))
				continue;
			
			if (Character.isWhitespace((char)i))
				continue;
			*/
			
			if (!Character.isLowerCase((char)i))
				continue;
			
			charSet.add(new Character((char)i).toString());
		}
	}

	synchronized public static Character getCharacterBefore(char c){
		
		SortedSet head = charSet.headSet(String.valueOf(c));
		if (head.size() == 0)
			return null;
		
		char retChar = ((String)head.last()).charAt(0);
		
		return (new Character(retChar));
	}
	
	synchronized public static Character getCharacterAfter(char c){
		SortedSet tail = charSet.tailSet(String.valueOf(c));
		if (tail.size() <= 1)
			return null;
		
		Iterator it = tail.iterator();
		it.next();
		char retChar = ((String)it.next()).charAt(0);

		return (new Character(retChar));
	}
}
