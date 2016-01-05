package com.anacleto.query.highlight;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.anacleto.base.Constants;

/**
 * Reader that reads a Lucene document
 * @author robi
 *
 */
public class LuceneDocumentReader extends Reader {

	private Document    doc;
	private String      field = null;
	private Enumeration fields;
	private Field 		currField;
	private Reader 		reader;
	
	private boolean		skipLineBreak = false;
	/**
	 * Read a lucene document
	 * @param doc
	 */
	public LuceneDocumentReader(Document doc) {
		this.doc = doc;
		init();
	}
	
	/**
	 * Read a field of a lucene document
	 * @param doc
	 */
	public LuceneDocumentReader(Document doc, String field) {
		this.doc = doc;
		this.field = field;
		init();
	}
	
	
	public boolean isSkipLineBreak() {
		return skipLineBreak;
	}

	public void setSkipLineBreak(boolean skipLineBreak) {
		this.skipLineBreak = skipLineBreak;
	}

	public int read(char[] cbuf, int off, int len) throws IOException{
		
		if (reader == null)
			return -1;
		
		int totCharsRead = 0;
		while (totCharsRead < len){
			if (reader == null)
				return totCharsRead;
			
			int charsToRead = len - totCharsRead;
			
			int charsRead = reader.read(cbuf, off + totCharsRead, charsToRead);

			if (charsRead > 0){
				if (skipLineBreak){
					//replace line breaks if needed
					String readStr = new String(cbuf, off + totCharsRead, charsRead);
					int oldLen = readStr.length();
					readStr = readStr.replaceAll( "\n", "" );
					readStr = readStr.replaceAll( "\r", "" );
					int newLen = readStr.length();
					if (newLen < oldLen) {
						System.arraycopy(readStr.toCharArray(), 0, cbuf, off + totCharsRead, newLen);
						totCharsRead = totCharsRead - (oldLen - newLen);
					}
				}
				totCharsRead = totCharsRead + charsRead;
			} else {
				stepToNextField();
			}
			
			
			
		}
		return totCharsRead;
	}
	
	private void init(){
		if (this.field == null)
			fields = doc.fields();
		else {
			Field[] fieldsArr = doc.getFields(this.field);
			List list = Arrays.asList(fieldsArr);
			Vector v = new Vector(list);
			fields = v.elements();
		}
			
		stepToNextField();		
	}
	
	private void stepToNextField(){
		while (fields.hasMoreElements()){
			
			currField = (Field) fields.nextElement();
			if (currField == null){
				reader = null;
				return;
			} else if (Constants.isReservedField(currField.name())){
				continue;
			}
				
			
			reader = currField.readerValue();
			if (reader == null)
				reader = new StringReader(" " + currField.stringValue());
			
			
			if (reader != null)
				return;
			
		}
		reader = null;
	}

	public void close() throws IOException {
	}

	/**
	 * @return Returns the currField.
	 */
	public String getCurrFieldName() {
		return currField.name();
	}
}
 
