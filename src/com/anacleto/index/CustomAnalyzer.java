/*
 * Created on May 18, 2005
 *
 */
package com.anacleto.index;

import java.io.*;
import java.util.*;

import org.apache.commons.collections.FastTreeMap;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import com.anacleto.base.Configuration;
import com.anacleto.base.Logging;
import com.anacleto.filters.SoftHypenFilter;

/**
 * Standard analyser + accentfilter
 * @author robi
 *
 */
public class CustomAnalyzer extends Analyzer {
	
	private static int 	BUFFER_SIZE = 256;
	
	private Map  accentMap = new FastTreeMap();
	private Set  stopSet   = new TreeSet();
	
	private File stopWordsFile;
	private File accentsFile;
	
	private Logger log = Logging.getAdminLogger();
	
	public CustomAnalyzer(File confDir){
		
		stopWordsFile = new File(confDir + "/stopwords");
		accentsFile   = new File(confDir + "/accents");
		
		if (confDir == null || !confDir.exists()){
			log.warn("Inexistent configuration directory: " + confDir);
		}else {
			if (!stopWordsFile.exists())
				try{
					stopWordsFile.createNewFile();
				} catch (IOException e) {
					log.warn("Error on creating stopwordsfile. Root cause: " + e);
				}
			if (!accentsFile.exists())
				try{
					accentsFile.createNewFile();
				} catch (IOException e) {
					log.warn("Error on creating accentsfile. Root cause: " + e);
				}
		}
		
		try {
			loadStopWords();
		} catch (IOException e) {
			log.warn("Error on loading stopwords. Root cause: " + e);
		}
		
		try {
			loadAccents();
		} catch (IOException e) {
			log.warn("Error on loading accents. Root cause: " + e);
		}
		deAccentStopWords();
	}
	
	public CustomAnalyzer(File confDir, String[] stopWords, Map accentMap){
		this.stopSet = StopFilter.makeStopSet(stopWords);
		this.accentMap = accentMap;
		deAccentStopWords();
	}
	
	public void deAccentStopWords(){
		
		HashSet newStopSet = new HashSet(stopSet.size());
		
		Iterator it = stopSet.iterator();
		while (it.hasNext()) {
			StringBuffer stopWord = new StringBuffer((String) it.next());
			for (int i = 0; i < stopWord.length(); i++) {
				Character c = new Character(stopWord.charAt(i));
				String rep = (String) accentMap.get(c);
				if (rep != null)
					stopWord.replace(i, i + 1, rep);
			}
			newStopSet.add(stopWord.toString());
		}
		stopSet = newStopSet;
		
	}
	
	/** 
	 * Constructs a {@link StandardTokenizer} filtered by a 
	 * {@link StandardFilter}, a {@link LowerCaseFilter} and a 
	 * {@link StopFilter}. 
	 */
	public TokenStream tokenStream(String fieldName, Reader reader) {
		TokenStream result = new StandardTokenizer(reader);
	    result = new SoftHypenFilter(result);
	    result = new StandardFilter(result);
	    result = new LowerCaseFilter(result);
	    result = new AccentFilter(result, accentMap);
	    result = new StopFilter(result, stopSet);
	    
	    return result;
	}
	
	public synchronized void loadStopWords() throws IOException{
		
		stopSet.clear();
		
		String fileContent = loadFile(stopWordsFile);
		String[] stopWordsStr = fileContent.split(
				System.getProperty("line.separator"));

		for (int i = 0; i < stopWordsStr.length; i++) {
			if ( stopWordsStr[i] != null 
					&& !stopWordsStr[i].trim().equals("") )
				stopSet.add(stopWordsStr[i]);
		}
	}

	public synchronized void saveStopWords() throws IOException{
		
		OutputStream out = new FileOutputStream(stopWordsFile, false);
		
		Iterator iter = stopSet.iterator();
		while (iter.hasNext()) {
			String currWord = iter.next() + System.getProperty("line.separator");
			out.write(currWord.getBytes("utf-8"));
		}
		out.close();
	}
	
	public void addStopWord(String word){
		if ( word != null && !word.trim().equals("") )
			stopSet.add(word);
	}
	
	public void deleteStopWord(String word){
		stopSet.remove(word);
	}
	
	public void deleteStopWord(String[] wordList){
		for (int i = 0; i < wordList.length; i++) {
			stopSet.remove(wordList[i]);
		}
	}
	
	/**
	 * @return Returns the stopSet.
	 */
	public Set getStopSet() {
		return stopSet;
	}
	
	public void loadDefaultAccents(){
		accentMap = AccentFilter.getDefaultAccentMap();
	}
	
	public synchronized void loadAccents() throws IOException{
		accentMap.clear();
		
		String fileContent = loadFile(accentsFile);
		String[] linesStr = fileContent.split(
				System.getProperty("line.separator"));
		
		for (int i = 0; i < linesStr.length; i++) {
			if ( linesStr[i] != null 
					&& !linesStr[i].trim().equals("") )
				addAccent(new Character(linesStr[i].charAt(0)), 
						linesStr[i].substring(2));
		}
	}

	public synchronized void saveAccents() throws IOException{
		
		OutputStream out = new FileOutputStream(accentsFile, false);
		
		Iterator iter = accentMap.keySet().iterator();
		while (iter.hasNext()) {
			Character from = (Character)iter.next();
			String    to   = (String)accentMap.get(from);
			String currWord = from + "=" + to 
				+ System.getProperty("line.separator");
			out.write(currWord.getBytes("utf-8"));
		}
		out.close();
	}
	
	public void addAccent(String from, String to){
		addAccent(from.charAt(0), to);
	}
	
	public void addAccent(char from, String to){
		addAccent(new Character(from), to);
	}
	
	public void addAccent(Character from, String to){
		if ( from != null  && !Character.isSpaceChar(from.charValue())
				&& to != null && !to.trim().equals("") )
			accentMap.put(from, to);
	}
	
	public void deleteAccent(Character from){
		accentMap.remove(from);
	}
	
	public void deleteAccents(String[] accentList){
		for (int i = 0; i < accentList.length; i++) {
			accentMap.remove(new Character(accentList[i].charAt(0)));
		}
	}
	
	/**
	 * Querys should be translated before parsing, to remove accented chars
	 * otherwise wildcardqueries will not work
	 * @param query
	 * @return
	 */
	public String translateQuery(String query){
		StringBuffer sb = new StringBuffer(query);
		
		// First check for accents
		for (int i = 0; i < sb.length(); i++) {
			Character c = new Character(sb.charAt(i));
			String rep = (String) accentMap.get(c);
			if (rep != null)
				sb.replace(i, i + 1, rep);
		}
		return sb.toString();
	}
	
	/**
	 * @return Returns the accentMap.
	 */
	public Map getAccentMap() {
		return accentMap;
	}
	private String loadFile(File file) throws IOException{
		
		InputStream is = new FileInputStream(file);

		StringBuffer buf = new StringBuffer();
		byte buffer[] = new byte[BUFFER_SIZE];
		int n;
		while (true) {
		    n = is.read(buffer);
		    if (n < 1)
		        break;
		    buf.append(new String(buffer, "UTF-8"));
		}
		is.close();
		
		return buf.toString();
	}
}
