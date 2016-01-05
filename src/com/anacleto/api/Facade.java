package com.anacleto.api;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;

import com.anacleto.base.Configuration;
import com.anacleto.base.Constants;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexManager;
import com.anacleto.query.highlight.HitlistHighlighter;

public class Facade {

	/**
	 * Executes a query
	 * 
	 * @param query
	 *            the query to execute
	 * @param offset
	 *            results are returned from this offset
	 * @param length
	 *            the maximum number of results returned
	 * @param stats
	 *            statistical information like time cunsumed and number of total
	 *            results
	 * @param queryResults
	 *            a list of the results. Elements of this list are members of
	 *            class <code>ResultView</code>
	 * @throws IOException
	 *             occurs when indexing system is not ready
	 * @throws ParseException
	 *             occurs when query is malformatted
	 */
	public static void executeQuery(String query, int offset, int length,
			QueryStats stats, List queryResults) throws IOException,
			ParseException {

		long start = System.currentTimeMillis();

		BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
		IndexManager im = new IndexManager();

		if (query == null || query.trim().length() == 0)
			return;

		Hits hits = im.executeQuery(query);
		stats.setNumberOfHits(hits.length());

		HitlistHighlighter h = new HitlistHighlighter(query);

		if (queryResults == null)
			queryResults = new LinkedList();

		for (int i = offset; i < offset + length; i++) {
			if (i >= hits.length())
				break;

			ResultView rec = new ResultView();
			rec.setHitNo(i + 1);

			// page details:
			Document doc = hits.doc(i);
			BookPage page = new BookPage(doc);

			// set score:
			float score = hits.score(i);
			rec.setScore(String.valueOf((int) (score * 100)) + "%");

			page.setPathFromDocRoot(Configuration
					.getElementPath(page.getName()));
			rec.setPage(page);

			rec.setContextList(h.scoreDoc(doc));

			queryResults.add(rec);
		}

		long end = System.currentTimeMillis();

		stats.setQueryTimeInMillis((int) (end - start));
	}
	
	/**
	 * Executes a query
	 * 
	 * @param query
	 *            the query to execute
	 * @param offset
	 *            results are returned from this offset
	 * @param length
	 *            the maximum number of results returned
	 * @param stats
	 *            statistical information like time cunsumed and number of total
	 *            results
	 * @param queryResults
	 *            a list of the results. Elements of this list are members of
	 *            class <code>ResultView</code>
	 * @throws IOException
	 *             occurs when indexing system is not ready
	 * @throws ParseException
	 *             occurs when query is malformatted
	 */
	public static void executeQuery(String query, String[] selectedFields,
			int offset, int length,
			QueryStats stats, List queryResults) throws IOException,
			ParseException {

		long start = System.currentTimeMillis();

		BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
		IndexManager im = new IndexManager();

		if (query == null || query.trim().length() == 0)
			return;

		Hits hits = im.executeQuery(query);
		stats.setNumberOfHits(hits.length());

		HitlistHighlighter h = new HitlistHighlighter(query);

		if (queryResults == null)
			queryResults = new LinkedList();

		for (int i = offset; i < offset + length; i++) {
			if (i >= hits.length())
				break;

			ResultView rec = new ResultView();
			rec.setHitNo(i + 1);

			// page details:
			Document doc = hits.doc(i);
			BookPage page = new BookPage(doc, selectedFields);

			// set score:
			float score = hits.score(i);
			rec.setScore(String.valueOf((int) (score * 100)) + "%");

			page.setPathFromDocRoot(Configuration
					.getElementPath(page.getName()));
			rec.setPage(page);

			rec.setContextList(h.scoreDoc(doc));

			queryResults.add(rec);
		}

		long end = System.currentTimeMillis();

		stats.setQueryTimeInMillis((int) (end - start));
	}
	
	public static void executeQueryInAllFields(String query, int offset, int length,
			QueryStats stats, List queryResults) throws IOException,
			ParseException {
		
		String rewrittenQuery = query;
		if (query.indexOf(":") < 0){
			rewrittenQuery = rewriteQuery(query);
		}
		executeQuery(rewrittenQuery, offset, length, stats, queryResults);
	}
	
	public static void executeQueryInAllFields(String query, int offset, int length,
			String[] selectedFields,
			QueryStats stats, List queryResults) throws IOException,
			ParseException {
		
		String rewrittenQuery = query;
		if (query.indexOf(":") < 0){
			rewrittenQuery = rewriteQuery(query);
		}
		executeQuery(rewrittenQuery, offset, length, stats, queryResults);
	}
	
	private static String rewriteQuery(String query) throws IOException {
		StringBuffer searchTerm = new StringBuffer();
		
		IndexReader reader = null;
		try {
			reader = IndexReader.open(Configuration.params
						.getIndexDir());
			Iterator it = reader.getFieldNames().iterator();
			
			boolean isFirst = true;
			
			while (it.hasNext()) {
				String currField = (String) it.next();
				if (Constants.isReservedField(currField))
					continue;
				
				if (!isFirst && it.hasNext()){
					searchTerm.append(" OR ");
				} 
				
				isFirst = false;
				
				searchTerm.append(currField + ":" + query );
			}
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		} finally {
			reader.close();
		}
		

		return searchTerm.toString();
	}

	

}
