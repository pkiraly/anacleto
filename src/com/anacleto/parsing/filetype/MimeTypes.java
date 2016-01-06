package com.anacleto.parsing.filetype;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.collections.FastTreeMap;
import org.apache.log4j.Logger;

import com.anacleto.base.Logging;

public class MimeTypes {

	
	private static boolean initialized = false;

	//ExtensionMap: xls:application/excel
	private static FastTreeMap extensionMap;
	
	//aliasMap: application/x-excel application/excel
	private static FastTreeMap aliasMap;
	
	private static Logger adminLog = Logging.getAdminLogger();
	private static Logger indexingLog = Logging.getIndexingLogger();
	
	synchronized public static String getMimeForFileName(String fileName){
		if (!initialized){
			initialize();
			initialized = true;
		}
		
		String retName = null;
		int extIdx = fileName.lastIndexOf('.');
		String ext = "";
		if (extIdx >= 0 || extIdx < fileName.length()) {
			ext = fileName.substring(extIdx + 1).toLowerCase();
			retName =  (String)extensionMap.get(ext);
		}
		
		if (retName == null)
			indexingLog.warn("No mime found for file: " + fileName + " (" + ext + ")");
		
		return retName;
	}

	synchronized public static String getAliasMime(String mime){
		
		if (!initialized){
			initialize();
			initialized = true;
		}
		
		String retName =  (String)aliasMap.get(mime);
		
		if (retName == null)
			retName = mime;
		
		return retName;
	}
	
	synchronized public static String getBaseMime(String mime){
		
		int ext = mime.indexOf(';');
		if (ext >= 0)
			return mime.substring(0, ext);
		else
			return mime;
		
	}
	
	/**
	 * Get short mime type: example: text/html will become html
	 * @param mime
	 * @return
	 */
	public static String getShortType(String mime){
		
		String baseMime = MimeTypes.getBaseMime(mime);
		int ext = baseMime.indexOf('/');
		if (ext < 0){
			return baseMime;	
		}
		
		String shortType = baseMime.substring(ext + 1);
		
		if (shortType.equals("plain")){
			return "text";
		}
		
		if (shortType.equals("octet-stream")){
			//Octet-streams have different types:
			int extTpeS = mime.indexOf('(');
			int extTpeE = mime.indexOf(')');
			if (extTpeE > 0 && extTpeS > 0 && extTpeE > extTpeS){
				String shortOctetType = mime.substring(extTpeS + 1, extTpeE);
				if (shortOctetType.equals("index"))
					return "html";
				
				if (shortOctetType.equals("?"))
					return "octet-stream";
				
				if (shortOctetType.equals("license"))
					return "text";
				
				return shortOctetType;
			}
		}
		
		if (shortType.startsWith("x-")){
			return shortType.substring(2);
		}
		
		if (shortType.equals("vnd.palm")){
			return "palm";
		}
		
		if (shortType.equals("svg+xml")){
			return "xml";
		}
		
		if (shortType.equals("mpeg")){
			if (mime.startsWith("audio/"))
				return "audio_mpeg";
			
			if (mime.startsWith("video/"))
				return "video_mpeg";
		}
		return shortType;
		
	}
	
	public static String getEncoding(String mime){
		
		int ext = mime.indexOf("charset=");
		if (ext >= 0){
			int start = mime.indexOf('"', ext) + 1;
			int end   = mime.indexOf('"', start);
			if (end > start)
				return mime.substring(start, end);
		} 
		
		return null;
	}

	synchronized public static void initialize(){
		ClassLoader cloader = MimeTypes.class.getClassLoader();
		InputStream is = cloader.getResourceAsStream(
			"com/anacleto/parsing/filetype/mime.types");
		if (is == null){
			adminLog.warn("Error occured while loading mime types." +
					" Root cause: missing " +
					" com/anacleto/parsing/filetype/mime.types from " +
					"classpath");
			return;
		}
		
		loadMimes(is);			
	}

	
	private static void loadMimes(InputStream is){
		
		String fileContent = readFile(is);

		String[] lines = fileContent.split(
				System.getProperty("line.separator"));

		extensionMap = new FastTreeMap();
		aliasMap = new FastTreeMap();
		
		for (int i = 0; i < lines.length; i++) {
			if ( lines[i] == null 
					|| lines[i].trim().length() == 0 )
				continue;
			
			lines[i] = lines[i].trim();
			
			if (lines[i].charAt(0) == '#')
				continue;
			
			int space = 0; 
			for (int j = 0; j < lines[i].length(); j++){
				if (Character.isWhitespace(lines[i].charAt(j))){
					space = j;
					break;
				}
			}
			
			if (space == 0)
				continue;
			String ext = lines[i].substring(1, space);
			String mime = lines[i].substring(space).trim();
			
			String registeredMime = (String)extensionMap.get(ext);
			if (registeredMime == null){
				extensionMap.put(ext, mime);
				// adminLog.debug("Mime added: " + mime + " for extension: " + ext);
			} else {
				aliasMap.put(mime, registeredMime);
				/*
                adminLog.debug("Mime alias added: " + mime + " for mime: " + 
						registeredMime );
                */
			}
		}
		
		
	}
	
	private static String readFile(InputStream is) {
		
		StringBuffer buf = new StringBuffer();
		try {
			
			byte buffer[] = new byte[1024];
			int n;
			while (true) {
			    n = is.read(buffer);
			    if (n < 1)
			        break;
			    buf.append(new String(buffer, "UTF-8"));
			}
		} catch (FileNotFoundException e) {
			adminLog.error("Error occured while loading mime types." +
					" Root cause: missing " +
					" com/anacleto/parsing/filetype/mime.types from " +
					"classpath");
		} catch (IOException e) {
			adminLog.error("Error occured while loading mime types." +
					" Root cause: " + e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				;
			}
		}

		
		return buf.toString();
	}
}
