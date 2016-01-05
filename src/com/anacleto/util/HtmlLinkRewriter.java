package com.anacleto.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;

import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexManager;

public class HtmlLinkRewriter {
	
	private String downloadLinkPrefix;

	private String relativePrefix;
	private String relativePostfix;
	
	
	public HtmlLinkRewriter(String downloadLinkPrefix, 
							String relativePrefix, 
							String relativePostfix) {
		
		this.downloadLinkPrefix = downloadLinkPrefix;
		this.relativePrefix = relativePrefix;
		this.relativePostfix = relativePostfix;
	}
	
	/**
	 * Replace links in a html document.
	 * The rules are:
	 * 1. Search for all relative links within the document (src, href)
	 * 2. if the found link is within a <link> tag and the rel=stylesheet
	 * option is specified, or it is within a script, iframe, object, applet
	 *  
	 * @param content
	 * @return
	 */
	public String rewrite(BookPage page, String content){
		String retContent = content;
		
		retContent = rewriteLink(page, retContent, "href");
		retContent = rewriteLink(page, retContent, "src");
		
		return retContent;
	}
	
	
	private String rewriteLink(BookPage page, String content, String type){
		int lastPos = 0;
		while (true){
			
			int hrefPos = content.indexOf(type, lastPos);
			if (hrefPos >= 0){
				int start = content.indexOf('"', hrefPos) + 1;
				int end   = content.indexOf('"', start);
				if (start > 0 && end > start){
					//TODO: uri rewriting
					try {
						URI uri = new URI(content.substring(start, end));
						if (!uri.isAbsolute()){
							int currPos = hrefPos;
							int spacePos = hrefPos;
							while (currPos >= 0){
								if (content.charAt(currPos) == ' ')
									spacePos = currPos;
								
								if (content.charAt(currPos) == '<'){
									break;
								}
								currPos--;
							}
							String tag = content.substring(currPos+1, spacePos);
							
							if (tag.equals("link") || tag.equals("script") || 
									tag.equals("iframe") || tag.equals("object")
									|| tag.equals("applet") || tag.equals("img")){
								
								//dowload mime...
								File pageLoc = new File(page.getLocation());
								String newLink = pageLoc.getParent() + "/" 
										+ content.substring(start, end);
								
								content = content.substring(0, start) +
										downloadLinkPrefix + newLink +
										content.substring(end);
							} else if (tag.equals("a")){
								//showDocument.....
								
								String relPath = content.substring(start, end);
								
								//dowload mime...
								File pageLoc = new File(page.getLocation());
								
								pageLoc = pageLoc.getParentFile();
								while (true){
									if (relPath.startsWith("..")){
										pageLoc = pageLoc.getParentFile();
										relPath = relPath.substring(2);
										
									} else if (relPath.startsWith(".")){
										relPath = relPath.substring(1);
										
									} else if (relPath.startsWith("/")){
										relPath = relPath.substring(1);
									} else
										break;
										
								}
								
								String newLink = pageLoc + "/" + 
										content.substring(start, end);
								IndexManager im;
								try {
									im = new IndexManager();
									Document doc = im.executeTermQuery(
											new Term("location", newLink), 0);
								
									if (doc != null){
										BookPage refEl = new BookPage(doc);
										content = content.substring(0, start)
											+ relativePrefix + refEl.getName()
											+ relativePostfix
											+ content.substring(end);
									}
								} catch (IOException e) {
								} catch (ParseException e) {
								}
								
								
							}
							
						}
					} catch (URISyntaxException e) {
						
					}
					lastPos = end;
				} else
					break;
				
			} else{
				break;
			}
		}
		return content;
	}

}
