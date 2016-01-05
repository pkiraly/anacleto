package com.anacleto.parsing.filetype;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.anacleto.base.Logging;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexManager;

public class PdfPerPageIndexer {

	protected Logger log = Logging.getIndexingLogger();
	private String FS = System.getProperty("file.separator");

	public void processStream(InputStream is, HashMap baseFields,
			IndexManager indexMan) {

		try {
			String loc = (String) baseFields.get("Location");
			String path = (String) baseFields.get("path");
			String fileName = loc.substring(loc.lastIndexOf(FS)+1);
			String baseLocation = loc.substring(0, loc.length()-fileName.length()-1);
			baseLocation = baseLocation.substring(0, baseLocation.lastIndexOf(FS));
			String year = fileName.substring(0, fileName.lastIndexOf('.'));
			String prefix = (String)baseFields.get("parentName");
			String basePath = path.substring(0, path.lastIndexOf('/'));

			BookPage book = new BookPage();

			book.setName(prefix + year);
			book.setTitle(year);
			book.setPath((String)baseFields.get("path"));
			book.setParentName(prefix);
			book.setBookName((String)baseFields.get("BookName"));
			book.setLocation((String)baseFields.get("Location"));
			book.setFirstChildContent(true);
			book.setContentType((String)baseFields.get("ContentType"));
			book.addTextField("indexedAt", 
					String.valueOf(System.currentTimeMillis()));
			book.addTextField("logicalParent",
					prefix + ':'
					+ prefix);

			PdfPerPageParser pdfReader = new PdfPerPageParser(is, prefix, year);
			Hashtable pageTable = pdfReader.getPageTable();
			// String[] pages = pdfReader.getPages();
			// book.setChildCount(pages.length);
			int lenI = pdfReader.getPageCount();
			book.setChildCount(lenI);

			indexMan.addPageInBatch(book);

			long startI = new Date().getTime();
			// log.info("total number of pages: " + pages.length);
			log.info("total number of pages: " + lenI);
			// content = content.replace("##NEW_LINE##", "");
			for (int i = 0; i < lenI; i++) {
				BookPage page = new BookPage();
				page.setName(prefix + year + (i+1));
				page.setPath((String)baseFields.get("path") + "/" + page.getName());
				// log.debug("page.path: " + page.getPath());
				page.setParentName(prefix + year);
				page.setBookName((String)baseFields.get("BookName"));
				page.setContentType((String)baseFields.get("ContentType"));
				page.addTextField("indexedAt", String.valueOf(System.currentTimeMillis()));

				page.addTextField("year", year);
				page.setLocation(baseLocation+FS+"pages"+FS+year+FS+year+'-'+i+".pdf");
				page.addTextField(
					"content", pdfReader.getContentOfPdfFile(page.getLocation())
				);
				
				// addBaseFields(doc);
				// page.addTextField("content", pages[i]);
				Integer pageNr = new Integer(i+1);
				page.addTextField("pageNr", pageNr.toString());
				Hashtable pageData = (Hashtable) pageTable.get(pageNr);
				if(pageData == null) {
					log.debug("inexistent page number in TOC: " + pageNr);
					continue;
				}

				boolean hasTitle = false;
				
				for (Enumeration e = pageData.keys(); e.hasMoreElements();) {
					String key = (String) e.nextElement();
					if(key.equals("path") || 
					   key.equals("logicalParent") ||
					   key.equals("title"))
					{
						List l = (List)pageData.get(key);
						Iterator it = l.iterator();
						while(it.hasNext()) {
							if(key.equals("path")) {
								page.addKeyWord(key, 
									basePath 
									+ '/' + book.getName() + ':' + book.getTitle()
									+ (String)it.next());
							} else if(key.equals("logicalParent")) {
								page.addKeyWord(key, (String)it.next());
							} else {
								hasTitle = true;
								page.addTextField(key, (String)it.next());
							}
						}
						if(key.equals("logicalParent")) {
							StringBuffer text = new StringBuffer();
							it = l.iterator();
							while(it.hasNext()) {
								if(text.length() != 0){
									text.append(", ");
								}
								text.append((String)it.next());
							}
							page.addKeyWord("logicalParents", "{ " + text.toString() + " }");
						}
						
					} else if(key.equals("childCount")) { 
						String value = (String) pageData.get(key);
						page.addKeyWord(key, value);
					} else if(key.equals("isRoot")) { 
						// do nothing
					} else {
						String value = (String) pageData.get(key);
						page.addTextField(key, value);
					}
				}
				String title = (String) pageData.get("origPageNr");
				if(title == null){
					log.debug("null title (origPageNr) @" + pageNr);
				} else {
					page.setTitle(title);
					hasTitle = true;
				}
				if(hasTitle == false) {
					page.setTitle("[" + pageNr + "]");
				}


				indexMan.addPageInBatch(page);
			}
			long endI = new Date().getTime();
			log.info("PDF indexing took " + (endI - startI) + " ms");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
