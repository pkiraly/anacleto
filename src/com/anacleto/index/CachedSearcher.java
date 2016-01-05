package com.anacleto.index;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.collections.map.LRUMap;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import com.anacleto.base.Logging;
import com.anacleto.util.LinkedFloatList;
import com.anacleto.util.LinkedIntList;

/**
 * Decorates the Lucene IndexSearcher class to add extra functionality
 * - caching of sorted queries, and query rewrites
 * - custom search rewrite to support PhasePrefixQueries ("star *ars"),
 * 			path queries (path:/shelf1/shelf2/*
 * - custom result sorting
 * @author robi
 *
 */
public class CachedSearcher extends Searcher {
	
	Searcher searcher;
	
	private IndexReader reader;
	//TODO: implement normal (not sorted) query caching, with a
	//more advanced caching system
	private static LRUMap  rewriteCache      = new LRUMap(200);
	
	//private static LRUMap  sortedHitsCache   = new LRUMap(15);
	
	//private static LRUMap  unSortedHitsCache = new LRUMap(15);
	
    private static  Logger log = Logging.getUserEventsLogger();
    
    private HashMap pathMap = null;

	/**
	 * constructor 
	 * @param searcher 
	 * @param reader
	 */
    public CachedSearcher(Searcher searcher, IndexReader reader) {
		this.searcher = searcher;
		this.reader   = reader;
		clearCache();
	}
	
	/**
	 * @see org.apache.lucene.search.Searchable#close()
	 */
	public void close() throws IOException {
		searcher.close();
	}

	/**
	 * @see org.apache.lucene.search.Searchable#doc(int)
	 */
	public Document doc(int i) throws IOException {
		return searcher.doc(i);
	}

	/**
	 * @see org.apache.lucene.search.Searchable#docFreq(org.apache.lucene.index.Term)
	 */
	public int docFreq(Term term) throws IOException {
		return searcher.docFreq(term);
	}


	/**
	 * @see org.apache.lucene.search.Searchable#explain(org.apache.lucene.search.Query, int)
	 */
	public Explanation explain(Query query, int doc) throws IOException {
		return explain(query, doc);
	}

	/**
	 * @see org.apache.lucene.search.Searchable#maxDoc()
	 */
	public int maxDoc() throws IOException {
		return maxDoc();
	}

	public static void clearCache(){
		rewriteCache.clear();
		//sortedHitsCache.clear();
		//unSortedHitsCache.clear();
	}
	
	/**
	 * @see org.apache.lucene.search.Searchable#rewrite(org.apache.lucene.search.Query)
	 */
	public Query rewrite(Query query) throws IOException {
//		check the cache:
		// log.info("rewrite1 query: " + query.toString());
		Query q = (Query)rewriteCache.get(query);
		
		if (q == null){
			q = rewriteQueries(query);
			// log.info("after rewriteQueries: " + q.toString());
		
			// q = searcher.rewrite(q);
			// log.info("after searcher.rewrite: " + q.toString());
			rewriteCache.put(query, q);
			return q;
		} else {
			// log.info("after rewriteCache: " + q.toString());
			return q;
		}
	}

	/**
	 * @see org.apache.lucene.search.Searchable#rewrite(org.apache.lucene.search.Query)
	 */
	public Query rewrite(Query query, HashMap paths) throws IOException {
//		check the cache:
		// log.info("rewrite2 query: " + query.toString());
		this.pathMap = paths;
		Query q = (Query)rewriteCache.get(query);
		
		if (q == null){
			q = rewriteQueries(query);
			// log.info("after rewriteQueries: " + q.toString());
		
			// q = searcher.rewrite(q);
			// log.info("after searcher.rewrite: " + q.toString());
			rewriteCache.put(query, q);
			return q;
		} else {
			// log.info("after rewriteCache: " + q.toString());
			return q;
		}
	}

	/**
	 * @see org.apache.lucene.search.Searcher#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, org.apache.lucene.search.Sort)
	 */
	public Hits search(Query query, Filter filter, Sort sort) throws IOException {
		if (filter == null){
			return searcher.search(query, sort);
		}
		return searcher.search(query, filter, sort);
	}

	/**
	 * @see org.apache.lucene.search.Searcher#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter)
	 */
	public Hits search(Query query, Filter filter) throws IOException {
		/*
		if (filter == null){
			Hits retHits  = (Hits)unSortedHitsCache.get(query);
			if (retHits == null){
				retHits = searcher.search(query);
				unSortedHitsCache.put(query, retHits);
			}
			return retHits;
		}
		*/
		return searcher.search(query, filter);
	}

	/**
	 * @see org.apache.lucene.search.Searcher#search(org.apache.lucene.search.Query, org.apache.lucene.search.HitCollector)
	 */
	public void search(Query query, HitCollector results) throws IOException {
		/*
		GlobalCache cache = new GlobalCache(GlobalCache.HitCollectorCache);
		CacheableHitCollector cachedHitColl = 
			(CacheableHitCollector)cache.retrieve(query);
		
		if (cachedHitColl == null){
			CacheableHitCollector hc = new CacheableHitCollector();
			searcher.search(query, hc);
			cache.store(query, hc);
			cachedHitColl = hc;
		}
		
		for (int i = 0; i < cachedHitColl.length(); i++) {
			results.collect(cachedHitColl.getDoc(i), 
							cachedHitColl.getScore(i));
		}
		*/
		searcher.search(query, results);
	}

	/**
	 * @see org.apache.lucene.search.Searcher#search(org.apache.lucene.search.Query, org.apache.lucene.search.Sort)
	 */
	public Hits search(Query query, Sort sort) throws IOException {
		return search(query, (Filter)null, sort);
	}

	/**
	 * @see org.apache.lucene.search.Searchable#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, org.apache.lucene.search.HitCollector)
	 */
	public void search(Query query, Filter filter, HitCollector results) throws IOException {
		searcher.search(query, filter, results);
	}

	/**
	 * @see org.apache.lucene.search.Searchable#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, int, org.apache.lucene.search.Sort)
	 */
	public TopFieldDocs search(Query query, Filter filter, int n, Sort sort) throws IOException {
		return searcher.search(query, filter, n, sort);
	}

	/**
	 * @see org.apache.lucene.search.Searchable#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, int)
	 */
	public TopDocs search(Query query, Filter filter, int n) throws IOException {
		return search(query, filter, n);
	}

	/**
	 * Custom rewrite of queries: escape special chars and rewrite 
	 * phraseQueries like "star war*"
	 * @return
	 * @throws IOException
	 */
	private Query rewriteQueries(Query q) throws IOException {
		// log.info("query: " + q.toString());

		if (q instanceof BooleanQuery){
			// log.info("-- BooleanQuery");
			
			BooleanQuery bq = (BooleanQuery)q;
			BooleanQuery retbq = new BooleanQuery();
			
			BooleanClause[] bc = bq.getClauses();
			for (int i = 0; i < bc.length; i++) {
				BooleanClause clause = bc[i];
				Query qc = clause.query;
				//log.info("qc: " + qc.toString());
				//if (qc instanceo f PhraseQuery){
				Query newPhraseQ = rewriteQueries(qc);
				//log.info("newPhraseQ: " + newPhraseQ.toString());
				bc[i] = new BooleanClause(newPhraseQ, 
						bc[i].required, bc[i].prohibited);
				//} 
				//log.info("bc[i]: " + bc[i].toString());
				retbq.add(bc[i]);
				//log.info("retbq: " + retbq.toString());
			}
			return retbq;
			
		} else if (q instanceof PrefixQuery){
			// log.info("-- PrefixQuery");
			//rewrite path expressions
			PrefixQuery tq = (PrefixQuery)q;
			if (tq.getPrefix().field().equals("path")) {
				PathQueryExtractor ex = new PathQueryExtractor();
				Query extractedQ;
				if(this.pathMap != null) {
					// log.info(tq.getPrefix().text());
					// log.info((String)this.pathMap.get(tq.getPrefix().text()));
					extractedQ = ex.extractPath((String)this.pathMap.get(tq.getPrefix().text()));
				} else {
					// log.info("pathMap is null");
					extractedQ = ex.extractPath(tq.getPrefix().text() + "*");
				}
				
				// log.info("extractedQ: " + extractedQ.toString());
				return extractedQ; //ex.extractPath(tq.getPrefix().text() + "*");
			}
			return q;
			
		} else if (q instanceof WildcardQuery){
			// log.info("-- WildcardQuery");
			//rewrite path expressions
			WildcardQuery wq = (WildcardQuery)q;
			if (wq.getTerm().field().equals("path")) {
				PathQueryExtractor ex = new PathQueryExtractor();
				
				return ex.extractPath(wq.getTerm().text());
			}
			return q;
			
		} else if (q instanceof TermQuery){
			// log.info("-- TermQuery");
			//rewrite path expressions
			TermQuery tq = (TermQuery)q;
			if (tq.getTerm().field().equals("path")) {
				PathQueryExtractor ex = new PathQueryExtractor();
				return ex.extractPath(tq.getTerm().text());
			}
			return q;
			
		} else if (q instanceof PhraseQuery){
			// log.info("-- PhraseQuery");
			PhraseQuery pq = (PhraseQuery)q;
			Term[] terms = pq.getTerms();
			
			//check if it is a phrasePrefixQuery or a simple phrasequery
			//TODO: when the phraseprefixQuery highlight problem resolved, remove this
			
			boolean found = false;
			for (int i = 0; i < terms.length; i++) {
				if (terms[i].text().indexOf("*") >= 0 ||
						terms[i].text().indexOf("?") >= 0){
					found = true;
					break;
				}
			}
			if (!found)
				return q;
			
			PhrasePrefixQuery retq = new PhrasePrefixQuery();
			
			retq.setSlop(pq.getSlop());
			for (int i = 0; i < terms.length; i++) {
				if (terms[i].text().indexOf("*") >= 0 ||
						terms[i].text().indexOf("?") >= 0){
					//term has a wildcard:
					WildcardTermEnum tes = new WildcardTermEnum(reader, 
			                terms[i]);
			        
					ArrayList termArr = new ArrayList();
			        while (tes.term()!= null){
			        	termArr.add(tes.term() );
			            tes.next();
			        }        
			        Term[] wterms = new Term[termArr.size()];
			        for (int j = 0; j < termArr.size(); j++) {
			        	wterms[j] = (Term)termArr.get(j);
					}
			        retq.add(wterms, i);
				} else{
					//term without wildcard
					retq.add(terms[i]);
				}
			}
			return retq;
		} else 
			return q;
		
	}

}

final class CacheableHitCollector extends HitCollector implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5200778686255680573L;
	
	LinkedIntList docList = new LinkedIntList();
	LinkedFloatList scoreList = new LinkedFloatList();
	
	public void collect(int doc, float score) {
		docList.add(doc);
		scoreList.add(score);
	}	
	
	public int length(){ 
		return docList.length();
	}
	
	public int getDoc(int n){
		return docList.get(n);
	}
	
	public float getScore(int n){
		return scoreList.get(n);
	}
}


