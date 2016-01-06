package com.anacleto.struts.action;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Searcher;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
import com.anacleto.base.Logging;
import com.anacleto.content.PDFUtils;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.index.IndexManager;
import com.anacleto.parsing.source.ContentReadException;
import com.anacleto.query.HtmlHighlighter;
import com.anacleto.struts.form.ShowDocumentForm;
import com.anacleto.util.MilliSecFormatter;
import com.lowagie.text.DocumentException;

/**
 * Action to show a document(html page) MyEclipse Struts Creation date:
 * 01-22-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/showDocument" name="showDocumentForm"
 *                input="/form/showDocument.jsp" scope="request" validate="true"
 */
public class ShowDocumentAction extends Action {

	protected static final int BUFFER_SIZE = 1024;
	private String FS = System.getProperty("file.separator");
	private Logger logger = Logging.getUserEventsLogger();
	
	// --------------------------------------------------------- Instance
	// Variables

	// --------------------------------------------------------- Methods

	/**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ConfigurationException
	 * @throws UnsupportedEncodingException
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		ShowDocumentForm sDForm = (ShowDocumentForm) form;

		try {
			IndexManager im = new IndexManager();

			HierarchicalElement currEl;
			String name = sDForm.getName();
			logger.info("name: " + name);
			if(sDForm.getName() == null || sDForm.getName().trim().equals("")) {
				Searcher searcher = im.getSearcher();
				String firstName = searcher.doc(1).get("name");
				currEl = Configuration.getElement(firstName);
			} else {
				currEl = Configuration.getElement(sDForm.getName());
			}
			logger.info("currEl: " + currEl.getLocation());
			
			// Previous page
			if (sDForm.getPrevPage().pressed()) {
				currEl = currEl.getPrevElement();

				// Next page
			} else if (sDForm.getNextPage().pressed())
				currEl = currEl.getNextElement();
			

			logger.info("ContentType: " + currEl.getContentType());
			logger.info(".pdf: " + currEl.getLocation().toLowerCase().endsWith(".pdf"));
			// TODO: this is not a correct solution, this bug came from
			// the indexing structure: there are a "parlamenti_naplo_fn"
			// book and page as well
			while(currEl.getLocation().toLowerCase().endsWith("years")) {
				currEl = currEl.getNextElement();
			}
			logger.info("currEl: " + currEl.getLocation());

			// Process Query related page commands:
			if (sDForm.getQuery() != null
					&& !sDForm.getQuery().trim().equals("")) {
				
				Hits hits = im.executeQuery(sDForm.getQuery());

				if (sDForm.getHitNo() < 1) {
					sDForm.setTotalResult(hits.length());

					int docNumber = im.getPageNumber(currEl.getName());
					if (docNumber > 0) {
						for (int i = 0; i < hits.length(); i++) {
							if (hits.id(i) == docNumber) {
								sDForm.setHitNo(i);
								break;
							}
							/*
							 * doc = new BookPage(hits.doc(i)); if(
							 * doc.getName().equals(currEl.getName()) ) {
							 * sDForm.setHitNo(i); break; }
							 */
						}
					}
				}

				if (sDForm.getPrevRes().pressed()) {
					// Previous result
					if (sDForm.getHitNo() > 0)
						sDForm.setHitNo(sDForm.getHitNo() - 1);

					currEl = new BookPage(hits.doc(sDForm.getHitNo()));

				} else if (sDForm.getNextRes().pressed()) {
					// Next result
					if (sDForm.getHitNo() < hits.length() - 1)
						sDForm.setHitNo(sDForm.getHitNo() + 1);
					currEl = new BookPage(hits.doc(sDForm.getHitNo()));
				}

				sDForm.setAtLast(false);
				if (hits.length() == sDForm.getHitNo() + 1)
					sDForm.setAtLast(true);
			} // Process Query END

			while (currEl.isFirstChildContent()) {
				Collection coll = currEl.getChildElements();
				Iterator it = coll.iterator();
				if (it.hasNext()) {
					currEl = (HierarchicalElement) it.next();
				} else {
					logger.error("Element: " + currEl.getName()
							+ " is firstChildContent but has no childs");
					break;
				}
			} // first child content END

			sDForm.setName(currEl.getName());

			if (currEl instanceof BookPage) {
				BookPage el = (BookPage) currEl;
				sDForm.setBook(el.getBookName());
				sDForm.setBookContentStyleSheet(
					el.getBook().getContentStyleSheet());

			} else if (currEl instanceof Book) {
				Book el = (Book) currEl;
				sDForm.setBook(el.getBookName());
				sDForm.setBookContentStyleSheet(el.getContentStyleSheet());
			}

			logger.info("Location: " + currEl.getLocation());
			String content = currEl.getContentToShow();
			if (content == null) {
				/*
				Enumeration names = request.getHeaderNames();
				StringBuffer namesBuffer = new StringBuffer();
				while (names.hasMoreElements()) {
					namesBuffer.append(" " + names.nextElement());
				}
				*/

				BookPage el = (BookPage) currEl;
				Book book = el.getBook();
				String pdfFileName;
				int pageNr;
				
				boolean fromPage = false;
				String pageLocation = getPageFileName(el, book);
				if (pageLocation != null) {
					logger.info("serving from page");
					pdfFileName = pageLocation;
					pageNr = 1;
					fromPage = true;
				} else {
					String relLocation = getRelFileName(el, book);
					pageNr = Integer.parseInt(el.getField("pageNr").stringValue());
					if (relLocation != null) {
						logger.info("serving from relative location");
						pdfFileName = relLocation;
					} else {
						logger.info("serving from absolute location");
						pdfFileName = el.getParentElement().getLocation();
					}
				}
				/*
				logger.info("getBookUrl: " + book.getURL());
				logger.info("pdfFileName: " + pdfFileName);
				logger.info("pageNr: " + pageNr);
				*/
				ServletOutputStream outStream = response.getOutputStream();
				try {
					response.setContentType("application/pdf");
					/*
					 * response.setHeader("Content-Disposition", " inline;
					 * filename=" + el.getBookName() + ".pdf");
					 */
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control",
							"must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");

					if(fromPage == false) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						PDFUtils.extractPage(pdfFileName, pageNr, baos);
						response.setContentLength(baos.size());
						baos.writeTo(outStream);
					} else {
						InputStream inStream = new FileInputStream(pdfFileName);
						byte[] buffer = new byte[BUFFER_SIZE];
						int n = 0;
						while (true) {
							n = inStream.read(buffer);
							if (n < 1) break;
							outStream.write(buffer, 0, n);
						}
						inStream.close();
					}
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (DocumentException e) {
					e.printStackTrace();
				}

				logger.info("Document served: "
						+ sDForm.getName()
						+ " ("
						+ currEl.getContentType()
						+ ")."
						+ " Duration: "
						+ MilliSecFormatter.toString((long) (System
								.currentTimeMillis() - start)) + "."
						+ " Query: " + sDForm.getQuery());
				//+ " names: " + namesBuffer.toString());
				// downloadMimeContent(new File(currEl.getLocation()), request,
				// response);
				return null;
			} else
				sDForm.setPageBody(content);

			// get highlights:
			if (sDForm.getQuery() != null && sDForm.getPageBody() != null
					&& !sDForm.getQuery().trim().equals("")
					&& currEl instanceof BookPage) {

				HtmlHighlighter h = new HtmlHighlighter(im.getQuery(sDForm
						.getQuery()), sDForm.getPageBody());
				sDForm.setPageBody(h.highLightText());
			}

			sDForm.setLinkedPath(Configuration.getElementLinkedPath(sDForm
					.getName())
					+ " " + currEl.getTitle());

		} catch (IOException e) {
			// Content was not found
			logger.error(e);
			return null;
		} catch (ParseException e) {
			logger.error("Malformed query: " + sDForm.getQuery() + ". Error: "
					+ e);
		} catch (ContentReadException e) {
			logger.error(e);
		}

		logger.info("Document served: " + sDForm.getName() 
				+ ". Duration: " + (System.currentTimeMillis() - start) 
				+ ". Query: " + sDForm.getQuery());

		return mapping.getInputForward();
	}
	
	/**
	 * Check if the "PageLocation" field points to an existent file or
	 * the "location" point to an existent file in the "pages" directory
	 * @param el - the current book page
	 * @param book - the current book
	 * @return the pdf file name if exists or null if it doesn't exist
	 * @throws IOException
	 */
	private String getPageFileName(BookPage el, Book book)  throws IOException {
		String pdfFileName = null;

		Field pageLocationFld = el.getField("PageLocation");
		if(pageLocationFld != null) {
			String pageLocation = pageLocationFld.stringValue();
			pdfFileName = book.getURL() + FS + pageLocation;
			File f = new File(pdfFileName); 
			if (f.exists()) {
				logger.info("exists: " + f.getAbsoluteFile());
				return pdfFileName;
			} else {
				logger.info("not exists: " + f.getAbsoluteFile());
				return null;
			}
		} else {
			String loc = el.getLocation();
			if(loc.endsWith(".pdf") 
				&& (loc.indexOf("/pages/") > -1 || loc.indexOf("\\pages\\") > -1)
				&& ((File) new File(loc)).exists() == true){
				logger.info("exists in pages: " + loc);
				pdfFileName = loc;
			}
		}
		return pdfFileName;
	}

	/**
	 * Check if there is a "RelativeLocation" field of the parent element of the 
	 * page and combine this with the book's URL. 
	 * @param el
	 * @param book
	 * @return
	 * @throws IOException
	 */
	private String getRelFileName(BookPage el, Book book) throws IOException {
		File pdfFileName = null;
		Field relLoc = ((BookPage)el.getParentElement()).getField("RelativeLocation");
		if(relLoc != null) {
			String pageLocation = relLoc.stringValue();
			pdfFileName = new File(book.getURL(), pageLocation);
			logger.info("pdfFileName (rel): " + pdfFileName.getAbsolutePath());
			if (pdfFileName.exists()) {
				logger.info("exists");
				return pdfFileName.getAbsolutePath();
			} else {
				logger.info("doesn't exists");
				return null;
			}
		}
		return pdfFileName.toString();
	}

	private void downloadMimeContent(File outFile, HttpServletRequest request,
			HttpServletResponse response) {

		if (outFile.isDirectory())
			return;

		/*
		 * response.setHeader("Content-disposition", "attachment; filename=\"" +
		 * outFile.getName() + "\"");
		 */
		String mimeType;
		mimeType = getServlet().getServletContext().getMimeType(
				outFile.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream'"; // default
		}
		response.setContentType(mimeType);

		if (!"HTTP 1.0".equals(request.getProtocol())) {
			response.setContentLength((int) outFile.length());
			response.setHeader("Transfer-Encoding", null);
		}

		try {
			OutputStream out = response.getOutputStream();
			InputStream inStream = new FileInputStream(outFile);

			byte buffer[] = new byte[BUFFER_SIZE];
			int n = 0;
			while (true) {
				n = inStream.read(buffer);

				if (n < 1)
					break;
				out.write(buffer, 0, n);
			}
			inStream.close();
			out.flush();
			out.close();
		}

		catch (IOException e) {
		}
	}
}