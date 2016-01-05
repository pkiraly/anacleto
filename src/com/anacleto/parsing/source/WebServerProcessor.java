package com.anacleto.parsing.source;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import websphinx.*;

import com.anacleto.base.BookException;
import com.anacleto.base.Logging;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexManager;
import com.anacleto.parsing.ParserException;
import com.anacleto.parsing.filetype.FileContentParser;

public class WebServerProcessor extends SourceTypeHandler {

	Logger logger = Logging.getIndexingLogger();

	public void indexBook(Book book) throws BookException, IOException {

		IndexManager inMan = new IndexManager();
		inMan.deleteBook(book.getName());

		CrawlLinkListener list = new CrawlLinkListener(book, inMan);

		DownloadParameters dlPars = new DownloadParameters();
		dlPars = dlPars.changeMaxThreads(0);
		dlPars = dlPars.changeMaxPageSize(-1);


		Crawler c = new Crawler();
		c.setRoot(new Link(book.getLocation()));
		c.addClassifier(new StandardClassifier());
		c.setDownloadParameters(dlPars);

		c.addLinkListener(list);
		c.setDomain(Crawler.SERVER);
		c.setLinkType(Crawler.HYPERLINKS);
		c.setMaxDepth(15);
		c.run();


	}

	public ContentBean getContent(BookPage page) throws ContentReadException {
		// TODO Auto-generated method stub
		return null;
	}

}

class CrawlLinkListener implements LinkListener{

	private IndexManager im;
	private Book         book;
	static int counter = 0;

	Logger logger = Logging.getIndexingLogger();

	CrawlLinkListener(Book book, IndexManager im){
		this.im = im;
		this.book = book;
	}

	public void crawled(LinkEvent le) {

		switch (le.getID()) {
			case LinkEvent.ERROR:
				logger.error("Crawling error occured during download of URL: "
					+   le.getLink().getURL()
					+ " Root cause:	" +	le.getException());
				break;

			case LinkEvent.TOO_DEEP:
			case LinkEvent.SKIPPED:
				logger.warn("Crawling event: " + le.getName()
					+ ", URL: " +  le.getLink().getURL());
				break;
			default:
				break;
		}

		if (le.getID() != LinkEvent.DOWNLOADED)
			return;

		logger.info("Crawling event: " + le.getName()
					+ ", URL: " +  le.getLink().getURL());

		Link l = le.getLink();
		Page p = l.getPage();

		BookPage page = new BookPage();
		page.setBookName(book.getName());
		page.setName(book.getName() + counter);
		page.setParentName(book.getName());
		page.setTitle(p.getTitle());
		page.setLocation(p.getURL().toString());
		page.setChildCount(counter);
		page.setContentType(book.getContentType());
		page.setPath(p.getURL().getPath());

		System.out.println(p.getContentBytes());
		try {

			ByteArrayInputStream is = new ByteArrayInputStream(
					p.getContentBytes());

			FileContentParser parser = new FileContentParser();
			parser.processContentType(p.getContentType(),
					is, page);

		//} catch (UnsupportedEncodingException e1) {
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			im.addPage(page);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		counter++;
	}

}
