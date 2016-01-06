package com.anacleto.query.highlight;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.PhrasePrefixQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import com.anacleto.base.Configuration;
import com.anacleto.base.Logging;
import com.anacleto.index.IndexManager;

public class PdfHighlighter extends HighlightMethods{

	String highlightStr;
	
	HighlightFragment lastFrag;

	private static  Logger log = Logging.getUserEventsLogger();
	
	public PdfHighlighter(String queryStr) throws IOException, ParseException {
       	IndexManager im = new IndexManager();
       	
       	log.debug("Query string to highlight: " + queryStr); 
    	
    	query = im.getQuery(queryStr);
    	log.debug("parsedQuery: " + query); 
    	
    	queriesByWords = new MultiValueTreeMap();
    	
    	//extract terms from the query:
    	extractQuery(this.query);
		
	}
	
	public String highLightDocument(Document doc, String field) throws IOException{
	    //Field docField = doc.getField(field);
    	StringBuffer retText = new StringBuffer();
    	
    	retText.append("<XML>\n<Body units=characters " + 
                " version=2>\n<Highlight>\n");

    	//int anchor = 0;
    	scoreDoc(doc, field, retText);
    	retText.append("</Highlight>\n</Body>\n</XML>");
    	return retText.toString();
    	
	}
	
	public String highLightDocument(String text) throws FileNotFoundException{
	    
		return null;
	    	
	}
	
	public String highLightDocument(InputStream docStream){
		return null;
	}
	public String highLightDocument(InputStream docStream, int pageNo){
		return null;
	}
	
    /**
     * Get scores for a given lucene document
     * @return
     * @throws IOException 
     */
    private void scoreDoc(Document doc, String field, StringBuffer retText) throws IOException{
    	highlightStr = "";
    	lastFrag = null;
    	
    	LuceneDocumentReader tokenizedReader = new LuceneDocumentReader(doc, field);
    	tokenizedReader.setSkipLineBreak(true);
    	Analyzer analyzer = Configuration.getAnalyzer();
    	TokenStream tS= analyzer.tokenStream(
    			Configuration.getDefaultQueryField(), 
    			tokenizedReader);

    	LuceneDocumentReader docReader = new LuceneDocumentReader(doc, field);
    	docReader.setSkipLineBreak(true);
    	ReaderWithMemory memReader = new ReaderWithMemory(docReader, 50);
    	 	
    	Collection tokenListeners = new ArrayList();
//    	first get term positions
    	Token token;
    	int lastPos = 0;
    	try {
			while ((token = tS.next()) != null) {
								
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
								addFragment(HighlightMethods.scoreTermQuery(memReader, token),
										retText);
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
							addFragment(HighlightMethods.scoreListener(lis, memReader),
									retText);
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
    }
    
    
    private void addFragment(HighlightFragment frag, StringBuffer retText){
        
    	retText.append("    <loc " +
                "pg=0" 
                + " pos=" + (frag.matchStartPos - 1)
                + " len="+ (frag.matchEndPos - frag.matchStartPos)
                + ">\n");
    	
    }
    
    public static void main(String[] args) throws IOException {
		Writer w = new StringWriter();
		PDDocument doc = PDDocument.load("/home/moki/work/arcanum_pdf/content/1867.pdf");
		
		PdfPageExtractor ext = new PdfPageExtractor();
		ext.extractText(doc, w, 1);
	}
}


class PdfPageExtractor extends PDFTextStripper{

	public PdfPageExtractor() throws IOException {
		super();
        super.setLineSeparator( "" );
        super.setPageSeparator( "" );
        super.setWordSeparator( "" );
        super.setShouldSeparateByBeads( false );
        super.setSuppressDuplicateOverlappingText( false );

	}
	
	public void extractText(PDDocument doc, Writer outputStream, int pageNo) throws IOException{
		setStartPage(pageNo);
		setEndPage(pageNo);
		extractText(doc, outputStream);
	}
	
	public void extractText(PDDocument doc, Writer outputStream) throws IOException{
		writeText(doc, outputStream);
	}
	
	
}
