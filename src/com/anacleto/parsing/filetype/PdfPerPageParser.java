package com.anacleto.parsing.filetype;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import org.pdfbox.exceptions.CryptographyException;
import org.pdfbox.exceptions.InvalidPasswordException;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDPage;
import org.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.pdfbox.util.PDFTextStripper;

import com.anacleto.base.Logging;

public class PdfPerPageParser {

	private InputStream input;
	private String prefix;
	private String year;
	private String namePrefix;

	private PDDocument pdfDocument;
	private Hashtable pageTable = new Hashtable();
	private List allPages; 
	private String[] pages;

	protected Logger log = Logging.getIndexingLogger();

	public PdfPerPageParser(InputStream is, String prefix, String year) {
		this.input = is;
		this.prefix = prefix;
		this.year = year;
		this.namePrefix = this.prefix + this.year;
		process();
	}

	private void process() {

		try {
			PDFParser parser = new PDFParser(input);
			parser.parse();
			pdfDocument = parser.getPDDocument();
			if (pdfDocument.isEncrypted()) {
				pdfDocument.decrypt("");
			}
			_getPdfTOC();
			// pages = _getPdfContent(true);
		} catch (IOException e) {
			log.error("Unable to load pdf document: \n" + e);
		} catch (CryptographyException e1) {
			log.error("Unable to decrypt pdf document.\n" + e1);
		} catch (InvalidPasswordException e2) {
			log.error("Missing password for pdf document.\n" + e2);
		} finally {
			try {
				pdfDocument.close();
			} catch (IOException e3) {
				log.error("Unable to close pdf document" + e3);
			}
		}
	}

	private void _getPdfTOC() {

		long start = new Date().getTime();
		try {

			PDDocumentOutline root = pdfDocument.getDocumentCatalog()
					.getDocumentOutline();
			PDOutlineItem item = root.getFirstChild();
			allPages = pdfDocument.getDocumentCatalog().getAllPages();
			int pageNr = -1;
			readMultilevelToc(item, "", pageNr, "");

			int len = getPageCount();
			for (int i = 1; i <= len; i++) {
				Integer IPageNr = new Integer(i);
				Hashtable childPage = null;
				if (!pageTable.containsKey(IPageNr)) {
					log.debug("WARNING: Missing page number at: " + i);
					childPage = new Hashtable();
					// childPage.put("origPageNr", "számozatlan");
					childPage.put("partTitle", "számozatlan oldalak");
					pageTable.put(IPageNr, childPage);
				}
				
				// betesszük a title elemet, hogy lehessen keresni
				childPage = (Hashtable)pageTable.get(IPageNr);
				
				if(childPage == null) {
					log.error("ERROR: page " + i + " doesn't exist");
				}
				/*
				String origPageNr = (String) childPage.get("origPageNr");
				if(origPageNr == null) {
					childPage.put("origPageNr", "számozatlan");
				}
				*/
				
				List titles = (List) childPage.get("title"); 
				if(titles == null) {
					if(i>1){
						// log.info("copy titles to " + childPage.get("origPageNr") + " (" + i + ")");
						Integer PrevPageNr = new Integer(i-1);
						Hashtable prevPage = (Hashtable)pageTable.get(PrevPageNr);
						List prevTitles = (List) prevPage.get("title");
						childPage.put("title", prevTitles);
						pageTable.put(IPageNr, childPage);
						prevTitles = null;
						PrevPageNr = null;
						prevPage = null;
					}
				}
			}

		} catch (Exception e) {
			log.error("Unable to load pdf document");
			e.printStackTrace();
		}
		long end = new Date().getTime();
		log.info("getPdfTOC took " + (end - start) + " ms");
	}

	private void readMultilevelToc(PDOutlineItem item, String parentName, 
			int parentPageNr, String path) {

		try {
			PDPage page = null;

			int siblingCounter = 0;
			while (item != null) {
				siblingCounter++;
				page = item.findDestinationPage(pdfDocument);
				int pageNr = -1;
				if (page != null) {
					pageNr = allPages.indexOf(page) + 1;
				}

				if(pageNr == -1) {
					if(parentName.equals("")) {
						pageNr = 1;
					}
					else {
						pageNr = parentPageNr;
					}
				}

				String title = clean(item.getTitle());
				if(pageNr == -1 && parentName.trim().length() > 0){
					log.error("PDF hiba: 0-ás oldalszám: '" + title + "', '" + parentName + "', '" + path + "'");
				}
				
				String fieldName = "";
				if(prefix.equals("parlamenti_naplo")) {
					if(year.indexOf("i") > -1) {
						fieldName = "iromanyszam";
					} else {
						if(path.indexOf("Oldalszámok") > -1) {
							fieldName = "origPageNr";
						} else if(path.indexOf("Ülésnapok") > -1) {
							fieldName = "ulesnap";
						} else {
							log.info("unmatched path: " + path + '/' + title);
							fieldName = "title";
						}
					}
				}
				else if(prefix.equals("szazadok")) {
					if(path.indexOf("ÉVES TARTALOMJEGYZÉK") > -1 ||
					   path.indexOf("ÉVES TARTALOMJEGYZÉÍK") > -1)
					{
						fieldName = "title";
					} else {
						Pattern pattern = Pattern.compile(":[0-9]+\\. szám");
						Matcher matcher = pattern.matcher(path);
						if(matcher.find()) {
							fieldName = "origPageNr";
						} else {
							log.info("unmatched path: " + path + '/' + title);
							fieldName = "title";
						}
					}
				}
				
				Integer IPageNr = new Integer(pageNr);
				Hashtable childPage = null;
				if (pageTable.containsKey(IPageNr)) {
					childPage = (Hashtable) pageTable.get(IPageNr);
					pageTable.remove(IPageNr);
				} else {
					childPage = new Hashtable();
				}

				List l = null;
				if(fieldName.equals("title")) {
					l = (List)childPage.get("title");
					if(l == null) {
						l = new ArrayList();
					}
					l.add(title);
					childPage.put("title", l);
				} else {
					childPage.put(fieldName, title);
				}

				childPage.put("siblingCounter", "" + siblingCounter);
				
				l = null;
				if(fieldName.equals("toc")){
					l = (List)childPage.get("title");
					if(l == null) {
						l = new ArrayList();
					}
					l.add(title);
					
					// századok sajátosság!!!!
					if(prefix.equals("szazadok")) {
						String[] pathComponents = path.split("/");
						if(pathComponents.length >= 4) {
							for(int i=3; i<pathComponents.length; i++) {
								String last = pathComponents[i];
								String pTitle = last.substring(last.indexOf(":")+1);
								l.add(pTitle);
							}
						}
					}

					childPage.put("title", l);
				}

				String path2 = path + "/" + siblingCounter + ':' + namePrefix + pageNr + ':' + title;
				l = (List)childPage.get("path");
				if(l == null) {
					l = new ArrayList();
				}
				l.add(path2);
				childPage.put("path", l);

				String parent = (path.lastIndexOf('/') > -1)
							? path.substring(path.lastIndexOf('/')+1)
							: "";
				boolean isRoot = false;
				if(parent.equals("")) {
					parent = namePrefix + ":" + year;
					isRoot = true;
				}
				l = (List)childPage.get("logicalParent");
				if(l == null) {
					l = new ArrayList();
				}
				l.add(parent);
				childPage.put("logicalParent", l);

				PDOutlineItem child = item.getFirstChild();
				int childCounter = 0;
				while(child != null){
					child = child.getNextSibling();
					childCounter++;
				}
				// item.
				childPage.put("childCount", "" + childCounter);
				childPage.put("isRoot", new Boolean(isRoot));

				pageTable.put(IPageNr, childPage);

				child = item.getFirstChild();
				if(child != null){
					readMultilevelToc(child, title, pageNr,
							path + '/' + namePrefix + pageNr 
							+ ':' + title);
				}

				item = item.getNextSibling();
			}
		} catch (IOException e) {
			log.error("Unable to load pdf document" + e);
		}
	}

	public String getContentOfPdfFile(String fileName) {

		String[] pages = {""};
		// log.info("indexing " + fileName);
		try {
			pdfDocument = PDDocument.load(fileName);
			if (pdfDocument.isEncrypted()) {
				pdfDocument.decrypt("");
			}
			pages = _getPdfContent(false);
		} catch (IOException e) {
			log.error("Unable to load pdf document: \n" + e);
		} catch (CryptographyException e1) {
			log.error("Unable to decrypt pdf document.\n" + e1);
		} catch (InvalidPasswordException e2) {
			log.error("Missing password for pdf document.\n" + e2);
		} finally {
			try {
				pdfDocument.close();
			} catch (IOException e3) {
				log.error("Unable to close pdf document" + e3);
			}
		}
		return pages[0];
	}

	private String[] _getPdfContent(boolean doSplit) {

		long start = new Date().getTime();
		String[] pages = null;
		try {
			StringWriter writer = new StringWriter();
			PDFTextStripper stripper = new PDFTextStripper();
			// stripper.setLineSeparator("##NEW_LINE##");
			if(doSplit == true) {
				stripper.setPageSeparator("##NEW_PAGE##");
			}
			stripper.writeText(pdfDocument, writer);

			if(doSplit == true) {
				pages = writer.getBuffer().toString().split("##NEW_PAGE##");
			} else {
				String[] page = { writer.getBuffer().toString() };
				pages = page;
			}
			writer = null;
		} catch (IOException e) {
			log.error(e);
		}
		long end = new Date().getTime();
		// log.info("getPdfContent took " + (end - start) + " ms");
		return pages;
	}

	public String[] getPages() {
		return pages;
	}

	public Hashtable getPageTable() {
		return pageTable;
	}

	private String clean(String text) {
		text = text.replaceAll("\\n", "");
		text = text.replaceAll("\\r", "");
		text = text.replaceAll("\\s+$", "");
		text = text.replaceAll("\u0000", "");
		return text;
	}
	
	public int getPageCount() {
		return allPages.size();
	}
}
