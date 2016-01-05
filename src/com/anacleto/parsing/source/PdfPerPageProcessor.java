package com.anacleto.parsing.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Hits;

import com.anacleto.base.BookException;
import com.anacleto.base.Configuration;
import com.anacleto.base.Constants;
import com.anacleto.base.Logging;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexManager;
import com.anacleto.parsing.ParserException;
import com.anacleto.parsing.filetype.MimeTypes;
import com.anacleto.parsing.filetype.PdfPerPageIndexer;

public class PdfPerPageProcessor extends SourceTypeHandler {

	protected String collectionName;

	protected int count = 0;

	protected IndexManager indexMan;

	protected boolean searcherUpdateNeeded = false;

	protected Logger logger = Logging.getIndexingLogger();

	public ContentBean getContent(BookPage page) throws ContentReadException {
		// TODO Auto-generated method stub
		return null;
	}

	public void indexBook(Book book) throws BookException, IOException {
		// TODO Auto-generated method stub
		logger.debug("indexBook: " + book.getName());

		this.collectionName = book.getName();
		this.indexMan = new IndexManager();
		File rootDir = new File(book.getURL());
		searcherUpdateNeeded = false;

		if (!rootDir.exists())
			throw new BookException("Inexistent file: " + rootDir);

		try {
			// deleteInexistentIndexEntries();
			if (searcherUpdateNeeded)
				indexMan.updateSearcher();

			indexMan.openBatchWriter();
			processDir(rootDir, collectionName, 0);
		} catch (IOException e) {
			throw new BookException("Error while processing " + collectionName
					+ ". Root cause:" + e);
		} finally {
			try {
				indexMan.closeBatchWriter();
				indexMan.updateSearcher();
			} catch (IOException e2) {
				logger.error(e2);
			}
		}
	}

	private void deleteInexistentIndexEntries() throws IOException {
		Hits hits = indexMan.getBookEntries(collectionName);
		IndexReader reader = IndexReader.open(Configuration.params
				.getIndexDir());

		for (int i = 0; i < hits.length(); i++) {
			Document currDoc = indexMan.getFastDocument(reader, hits.id(i));
			File location = new File(currDoc.get("location"));
			logger.info("Checking location: " + location.getCanonicalPath());

			if (!location.exists()) {
				indexMan.deletePage(currDoc.get("name"));
				searcherUpdateNeeded = true;
				logger.debug("Entity deleted from index: "
						+ currDoc.get("name"));
			}
		}
	}

	private void processDir(File currentFile, String parentName, int childCount)
			throws IOException {

		logger.debug("Processing: " + currentFile.getName());

		count++;

		String name = parentName + "/" + currentFile.getName();
		name = name.substring(0, name.length() - 4);

		HashMap baseFields = new HashMap();
		baseFields.put("name", name);
		baseFields.put("path", "/" + name);
		baseFields.put("parentName", parentName);
		baseFields.put("childCount", (new Integer(childCount)).toString());
		// baseFields.put("title", currentFile.getName());
		baseFields.put("title", name);
		baseFields.put("BookName", collectionName);
		baseFields.put("Location", currentFile.getCanonicalPath());
		baseFields.put("ContentType", Constants.PdfPerPageContent);
		baseFields.put("indexedAt", String.valueOf(System.currentTimeMillis()));

		if (currentFile.isDirectory()) {
			// directory is added only if it is not present:

			/*
			 * BookPage page = new BookPage(); page.setName(name);
			 * page.setPath("/" + name); page.setParentName(parentName);
			 * page.setChildCount(childCount);
			 * page.setTitle(currentFile.getName());
			 * page.setBookName(collectionName);
			 * page.setLocation(currentFile.getCanonicalPath());
			 * page.setContentType(Constants.PdfPerPageContent);
			 * page.addTextField("indexedAt", String.valueOf(System
			 * .currentTimeMillis()));
			 * 
			 * if (indexMan.getPage(page.getName()) == null) {
			 * indexMan.addPageInBatch(page); logger.debug("Directory Entity
			 * added to index: " + page.getName()); } logger.debug("File Entity
			 * added to index: " + page.getName());
			 */

			File child[] = currentFile.listFiles();
			for (int i = 0; i < child.length; i++) {
				// processDir(child[i], page.getName(), i);
				processDir(child[i], parentName, i);
			}

		} else {
			try {
				// we do not want to reindex files that are not changed
				// since last indexing
				/*
				 * long indexedAt = 0; Document doc =
				 * indexMan.getPage(page.getName()); if (doc != null) indexedAt =
				 * Long.valueOf(doc.get("indexedAt")).longValue(); if (indexedAt >=
				 * currentFile.lastModified()) return;
				 * 
				 * FileContentParser fcp = new FileContentParser();
				 * fcp.processContentType(currentFile, page);
				 */

				String mime = MimeTypes.getMimeForFileName(currentFile
						.getName());
				try {

					InputStream is = new FileInputStream(currentFile);
					if (mime.equals(MimeTypes.getAliasMime("application/pdf"))) {

						PdfPerPageIndexer indexer = new PdfPerPageIndexer();
						indexer.processStream(is, baseFields, indexMan);
					}
				} catch (FileNotFoundException e) {
					throw new ParserException("File not found: "
							+ currentFile.getAbsoluteFile() + " Root cause: "
							+ e);
				}

				searcherUpdateNeeded = true;

				// indexMan.addPageInBatch(page);
				logger.debug("File Entity added to index: " + name);

			} catch (ParserException e) {
				// log this event, but not block index writing
				logger.warn("Unable to parse file: "
						+ currentFile.getCanonicalPath() + ". Cause:" + e);
			} catch (Exception e2) {
				// file was cancelled during the process. do not care, go on
				logger.error("Exception in indexing file: "
						+ currentFile.getCanonicalPath() + ". "
						+ " Root cause:" + e2.getMessage());
				e2.printStackTrace();
			}
		}
	}
}
