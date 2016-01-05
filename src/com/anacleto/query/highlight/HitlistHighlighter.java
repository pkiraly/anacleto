package com.anacleto.query.highlight;

import java.io.IOException;
import java.util.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.*;

import com.anacleto.base.Configuration;
import com.anacleto.index.IndexManager;

public class HitlistHighlighter extends HighlightMethods{

	private static int MAX_FRAGMENTS = 3;
	private static int TIMEOUT_IN_MILIS = 2000;
	
	
	
	String highlightStr;
	private int fragmentCounter;
	
	HighlightFragment lastFrag;
	
	/**
     * Init highlighter for a query 
     * @param query
     *            a Lucene query (ideally rewritten using query.rewrite before
     *            being passed to this class and the searcher)
	 * @throws IOException 
	 * @throws ParseException 
     */
    public HitlistHighlighter(String queryStr) throws IOException, ParseException {
       	IndexManager im = new IndexManager();
    	query = im.getQuery(queryStr);
    	queriesByWords = new MultiValueTreeMap();
    	
    	//extract terms from the query:
    	extractQuery(this.query);
    }

    /**
     * Get scores for a given lucene document
     * @return
     * @throws IOException 
     */
    public String scoreDoc(Document doc) throws IOException{
    	highlightStr = "";
    	lastFrag = null;
    	fragmentCounter = 0;
    	
    	LuceneDocumentReader tokenizedReader = new LuceneDocumentReader(doc);
    	Analyzer analyzer = Configuration.getAnalyzer();
    	TokenStream tS= analyzer.tokenStream(
    			Configuration.getDefaultQueryField(), 
    			tokenizedReader);

    	LuceneDocumentReader docReader = new LuceneDocumentReader(doc);
    	ReaderWithMemory memReader = new ReaderWithMemory(docReader, 50);
    	
    	long expires = System.currentTimeMillis() + TIMEOUT_IN_MILIS;
    	
    	Collection tokenListeners = new ArrayList();
//    	first get term positions
    	Token token;
    	int lastPos = 0;
    	try {
			while ((token = tS.next()) != null) {
				if (fragmentCounter >= MAX_FRAGMENTS)
					break;
				
				if (System.currentTimeMillis() >= expires)
					break;
				
				Collection queryColl = (Collection)queriesByWords.get(
						token.termText());
				if (queryColl != null){
					
					//position not tokenized reader to the start of the token:
					memReader.read(token.startOffset()-lastPos);
					lastPos = token.startOffset();
					
					Term currTerm = new Term(docReader.getCurrFieldName(), 
							token.termText());	
					//this is a matching token, find queries that are relevant
					
					//investigate relevant queries
					Iterator it = queryColl.iterator();
					while (it.hasNext()) {
						Query query = (Query) it.next();
						if (query instanceof TermQuery) {
							TermQuery tq = (TermQuery) query;
	
							//it is a termQuery match
							if (currTerm.field().equals(tq.getTerm().field())){								
								addFragment(HighlightMethods.scoreTermQuery(memReader, token));
							}
							
						} else if (query instanceof PhraseQuery) {
							PhraseQuery pq = (PhraseQuery) query;
							TokenListener lis = new PhraseQueryListener(pq, 
									memReader.getPostBuffer(),
									token.startOffset());
							tokenListeners.add(lis);
							
						} else if (query instanceof PhrasePrefixQuery) {
							PhrasePrefixQuery pq = (PhrasePrefixQuery) query;
							TokenListener lis = new PhrasePrefixQueryListener(pq, 
									memReader.getPostBuffer(),
									token.startOffset());
							tokenListeners.add(lis);
						}
					}
				}
	
				
				Collection newtokenListeners = new ArrayList();
				Iterator it = tokenListeners.iterator();
				while (it.hasNext()) {
					
					TokenListener lis = (TokenListener) it.next();
					lis.addToken(token);
					if (!lis.active){
						if (lis.match){
							addFragment(HighlightMethods.scoreListener(lis, memReader));
						}
					} else {
						newtokenListeners.add(lis);
					}
				}
				tokenListeners = newtokenListeners;
				
				
			}
		} catch (IOException e) {
		} finally {
			if (tS != null) {
                try {
                	tS.close();
                } catch (IOException e) {
                }
            }
			memReader.close();
			
		}
		
    	return replaceUnwantedChars(highlightStr);
    }
    
    public Map scoreText(String text){
    	return null;
    }
    
    public Map scoreHtml(String text){
    	return null;
    }
    
    /**
     * Add found fragment to the results
     * @param frag
     */
    private void addFragment(HighlightFragment frag){
    	
    	fragmentCounter++;
    	
    	calculatePositions(frag);  	
    	if (lastFrag != null )
    		calculatePositions(lastFrag);
    	
    	deleteLeadout(frag);
		frag.leadOut = frag.leadOut + "...<br>";
		
    	int startPos = frag.startPos;
    	//if the lead out part is overlapping the new fragment,
		//create a continous fragment:
    	if (lastFrag != null 
    			&& frag.startPos < lastFrag.endPos){
    		
    		startPos = Math.max(lastFrag.matchEndPos, frag.startPos);
    		int posToDelete = lastFrag.endPos-startPos;
    		highlightStr = highlightStr.substring(0, 
    				highlightStr.length() - posToDelete);
    	
    		if (startPos < frag.matchStartPos){
    			//overlapping in the leadin
    			frag.leadIn = frag.leadIn.substring(
    					startPos - frag.startPos);
    			
    			highlightStr = highlightStr + frag.leadIn + "<b>" + 
    					frag.match + "</b>" + frag.leadOut;
    			
    		} else if ( startPos < frag.matchEndPos){
    			//overlapping in the match
    			frag.match = frag.match.substring(
    					startPos - frag.matchStartPos);
    			highlightStr = highlightStr + "<b>" + frag.match + "</b>" 
    				+ frag.leadOut;
    		} else {
    			//overlapping in the lead out, do nothing
    		}
    	} else {
    		deleteLeadin(frag);
    		
    		highlightStr = highlightStr + "..." +
    				frag.leadIn + "<b>" + frag.match + "</b>" + frag.leadOut;
    				
    	}
    	
    	lastFrag = frag;
    	
    }
    
    private void calculatePositions(HighlightFragment frag){
    	frag.startPos = frag.matchStartPos - frag.leadIn.length();
    	frag.endPos   = frag.matchEndPos   + frag.leadOut.length();
    }
    
    //make lead in start with a space
    private void deleteLeadin(HighlightFragment frag){
    	while (frag.leadIn.length() > 0 
    			&& !Character.isWhitespace(frag.leadIn.charAt(0))){
			frag.leadIn = frag.leadIn.substring(1);
		}
    }
    
//  make lead out end with a space
    private void deleteLeadout(HighlightFragment frag){
    	while (frag.leadOut.length() > 0 
    			&& !Character.isWhitespace(
    					frag.leadOut.charAt(frag.leadOut.length()-1))){
    		
			frag.leadOut = frag.leadOut.substring(0, frag.leadOut.length()-1);
		}
    }
    
	private String replaceUnwantedChars(String in) {
		String out = in.replace('"', ' ');
		out = out.replace('\'', ' ');
		out = out.replace('\n', ' ');
		out = out.replace('\r', ' ');
		return out;
	}
	


}
