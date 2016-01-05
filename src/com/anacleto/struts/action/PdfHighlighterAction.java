package com.anacleto.struts.action;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhrasePrefixQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFHighlighter;

import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
import com.anacleto.base.Logging;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.index.IndexManager;
import com.anacleto.query.highlight.PdfHighlighter;
import com.anacleto.struts.form.PdfHighlighterForm;

/**
 * MyEclipse Struts Creation date: 02-07-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/termList" name="termListForm"
 *                input="/form/termList.jsp" scope="request" validate="true"
 */
public class PdfHighlighterAction extends Action {

	private MultiValueTreeMap queriesByWords;
	Logger log = Logging.getUserEventsLogger();


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
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ConfigurationException {

		PdfHighlighterForm pHForm = (PdfHighlighterForm) form;
		String url = request.getQueryString();
/*	
		url = url.substring(url.indexOf("query=")+6);
		url = url.substring(0, url.indexOf("&"));
		String query = URLDecoder.decode( url, "ISO-8859-1");
			//pHForm.getQuery();
			 
*/
		String query = pHForm.getQuery();
		//pHForm.getQuery();
		log.info("query: " + query);

		String name = pHForm.getName();
		
		if(query == null || query.trim().length() == 0) {
			pHForm.setContent("");
		}

		else if(name == null || name.trim().length() == 0) {
			pHForm.setContent("");
		}

		else if (!query.equals("") && !name.equals("")) {
			
			PDDocument doc = null;
			try {
				PdfHighlighter hi = new PdfHighlighter(query);
				
				IndexManager im = new IndexManager();
		    	Document ldoc = im.getPage(pHForm.getName());
		    	
		    	String xml = hi.highLightDocument(ldoc, "content");
		    			
		    	
		    	/*
		    	Query imQuery = im.getQuery(query);
		    	log.info("imQuery: " + imQuery);
		    	queriesByWords = new MultiValueTreeMap();
		    	extractQuery(imQuery);
		    	Set keys = queriesByWords.keySet();
		    	String[] termList = new String[keys.size()];
		    	int i = 0;
		    	for (Iterator it=keys.iterator(); it.hasNext( ); ) { 
		    	    String term = (String) it.next();
		    	    log.info("term: " + term);
		    	    termList[i] = term;
		    	    i++;
		    	}
				
		    	    
				HierarchicalElement currEl = Configuration.getElement(name);
				String fileName = currEl.getLocation();
				
				// StringWriter strWriter = new StringWriter();
				response.setContentType("text/xml");
				StringWriter out2 = new StringWriter();
				//		new OutputStreamWriter(
				//				response.getOutputStream()));
				// out.write("<?xml version=\"1.0\"?>\n");
				
				doc = PDDocument.load(fileName);
				PDFHighlighter highlighter = new PDFHighlighter();
				highlighter.generateXMLHighlight(doc, termList, out2);
				log.debug(out2.getBuffer().toString());
				*/
				OutputStream out = response.getOutputStream();
				log.debug("PDFxml: " + xml);
				out.write(xml.getBytes());
				out.flush();
				out.close();

			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (doc != null) {
						doc.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

    private String convertString(String text, String fromCharset, 
    		String toCharset) throws UnsupportedEncodingException {
		log.info("stringForconversion: " + text);
		String result = text;
		byte[] help = text.getBytes(fromCharset);
		log.info("help: " + help.length);

		Charset utfCharset = Charset.forName(fromCharset);
		CharsetDecoder decoder = utfCharset.newDecoder();
		ByteBuffer utfBytes = ByteBuffer.wrap(help);
		log.info("utfBytes: " + utfBytes);
		CharBuffer helpChars = null;
		try {
			helpChars = decoder.decode(utfBytes);
		} catch (CharacterCodingException e) {
			log.error("Error decoding: " + e);
		}
		log.info("helpChars: " + helpChars);

		Charset asciiCharset = Charset.forName(toCharset);
		CharsetEncoder encoder = asciiCharset.newEncoder();
		ByteBuffer asciiBytes = null;
		try {
			asciiBytes = encoder.encode(helpChars);
		} catch (CharacterCodingException e) {
			log.error("Error encoding: " + e);
		}
		log.info("asciiBytes: " + asciiBytes);

		byte newHelp[] = asciiBytes.array();
		log.info("newHelp: " + newHelp);
		try {
			result = new String(newHelp, toCharset);
		} catch (UnsupportedEncodingException e) {
			log.error("Error encoding: " + e);
		}
		log.info("result: " + result);
		return result;
	}

    private void extractQuery(Query query) {
        
    	if (query instanceof BooleanQuery)
    		extractBooleanQuery((BooleanQuery) query);
        
        else if (query instanceof PhraseQuery)
        	extractPhraseQuery((PhraseQuery) query);
        
        else if (query instanceof PhrasePrefixQuery)
        	extractPhrasePrefixQuery((PhrasePrefixQuery) query);
        
        else if (query instanceof TermQuery)
        	extractTermQuery((TermQuery) query);
    }

    private void extractBooleanQuery(BooleanQuery query) {
        BooleanClause[] queryClauses = query.getClauses();
        int i;

        for (i = 0; i < queryClauses.length; i++) {
            if (!queryClauses[i].prohibited)
            	extractQuery(queryClauses[i].query);
        }
    }

    private void extractPhraseQuery(PhraseQuery query) {
        Term[] queryTerms = query.getTerms();
        int i;

        for (i = 0; i < queryTerms.length; i++) {
        	queriesByWords.put(queryTerms[i].text(), query);
        }
    }

    private void extractPhrasePrefixQuery(PhrasePrefixQuery query) {
    	
    	int[] positions = query.getPositions();
    	for (int i = 0; i < positions.length; i++) {
			Term[] queryTerms = query.getTerms(positions[i]);
			for (int j = 0; j < queryTerms.length; j++) {
				queriesByWords.put(queryTerms[j].text(), query);
	        }	
		}
        
    }

    private void extractTermQuery(TermQuery query) {
    	queriesByWords.put(query.getTerm().text(), query);
    }

}

final class MultiValueTreeMap extends TreeMap {
	
	private static final long serialVersionUID = -5761458079838799580L;

	public Object put(Object arg0, Object arg1) {
		Collection queryColl;
		if (containsKey(arg0)){
			queryColl = (Collection)get(arg0);
		} else {
			queryColl = new ArrayList();
		}
		queryColl.add(arg1);
		
		return super.put(arg0, queryColl);
	}
}

