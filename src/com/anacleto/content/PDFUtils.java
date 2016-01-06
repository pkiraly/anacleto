package com.anacleto.content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.anacleto.base.Logging;
import com.anacleto.parsing.source.PdfTocConfig;
import com.anacleto.parsing.source.PdfTocConfigException;
import com.anacleto.util.MilliSecFormatter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PRTokeniser;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;

public class PDFUtils {

	protected static Logger log = Logging.getUserEventsLogger();

	/**
	 * Extract a page from an existing pdf file to a new file
	 * 
	 * @param pdfFileName
	 *            The name of the source PDF file
	 * @param pageno
	 *            The page number to extract
	 * @param outputFile
	 *            The output file name
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void extractPage(String pdfFileName, int pageno, String outputFile) throws DocumentException, IOException {

		OutputStream outStream = new FileOutputStream(outputFile);
		extractPage(pdfFileName, pageno, outStream);
	}

	/**
	 * Extract a page from an existing pdf file to an output stream
	 * 
	 * @param pdfFileName
	 *            The name of the source PDF file
	 * @param pageno
	 *            The page number to extract
	 * @param outStream
	 *            The outputStream to send the content
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void extractPage(String pdfFileName, int pageno, OutputStream outStream) 
			throws DocumentException, IOException {
		
		log.info("extracting " + pdfFileName + " p. " + pageno);

		PDFReferences refs = new PDFReferences();
		PdfReader reader;
		RandomAccessFileOrArray ra = new RandomAccessFileOrArray(pdfFileName, false, true);

		if (!PDFReferences.checkIndexFile(pdfFileName)){			
			reader = new PdfReader(ra, new byte[0]);
			if (!reader.isNewXrefType()){
				refs.setXref(reader.getXref());
				refs.setTrailerpos(reader.getTrailerpos());
				refs.saveIndexInfo(pdfFileName);
			}
		} else {
			refs.loadIndexInfo(pdfFileName);
			//For new xref types we do not optimize
			if (refs.getTrailerpos() != 0)
				reader = new PdfReader(ra, new byte[0], refs.getXref(), refs.getTrailerpos());
			else
				reader = new PdfReader(ra, new byte[0]);
		}


		Document document = new Document(reader.getPageSizeWithRotation(pageno));
		PdfWriter writer = PdfWriter.getInstance(document, outStream);
		document.open();

		PdfContentByte cb = writer.getDirectContent();
		PdfImportedPage page = writer.getImportedPage(reader, pageno);
		
		int rotation = reader.getPageRotation(pageno);
		if (rotation == 90 || rotation == 270) {
			cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader
					.getPageSizeWithRotation(pageno).getHeight());
		} else {
			cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
		}
		document.close();
		reader.close();
	}
	
	public static void writePageContentStreamToFile(String pdfFileName, 
			int pageNo,  String outFileName) throws IOException {
		RandomAccessFileOrArray ra = new RandomAccessFileOrArray(
				pdfFileName, false, true);
		PdfReader reader = new PdfReader(ra, new byte[0]);
		byte[] contentStream = reader.getPageContent(pageNo);
		
		OutputStream out = new FileOutputStream(outFileName);
		try {
			out.write(contentStream);
		} finally {
			out.close();
		}
	}
    
	public static String getPageContent(String pdfFileName, int pageNo)
			throws IOException 
	{

		RandomAccessFileOrArray ra = new RandomAccessFileOrArray(
			pdfFileName, false, true);
		PdfReader reader = new PdfReader(ra, new byte[0]);
		byte[] contentStream = reader.getPageContent(pageNo);
	
		PRTokeniser tokenizer = new PRTokeniser(contentStream);
	
		StringBuffer sb = new StringBuffer();
		int prevTokenType = -1;
		String prevStringValue = "";
		float lastYCoordinate = -1;
		while (tokenizer.nextToken()) {
			if(tokenizer.getTokenType() == PRTokeniser.TK_STRING){
				if(! (prevTokenType == 3 && prevStringValue.equals("Lang")))
					sb.append(tokenizer.getStringValue());
			} else if(tokenizer.getTokenType() == PRTokeniser.TK_OTHER){
				if(tokenizer.getStringValue().equals("ET")) {
					// sb.append("\n");
				}
				// Tm = Text matrix. The Tm is composed of six long values.
				// the last two are the x and y coordinates. The 0,0 point
				// is the left bottom corner of the page
				if(tokenizer.getStringValue().equals("Tm")) {
					float currentYCoordinate = Float.parseFloat(prevStringValue);
					// the avarage distinction between line is about 8-10
					// don't know the measure (point, mm?), just the values...
					if(lastYCoordinate != -1 
						&& Math.abs(currentYCoordinate - lastYCoordinate) > 5)
						sb.append("\n");
					lastYCoordinate = currentYCoordinate;
				}
			}
			prevTokenType = tokenizer.getTokenType();
			prevStringValue = tokenizer.getStringValue();
		}
		byte[] latin1 = sb.toString().getBytes("ISO8859-1");
		String content = new String(latin1, "ISO8859-2");
		/*
		log.info("content.length: " + content.length());// + " " + content.substring(0, 100));
		int i = content.indexOf("Mid");
		if(i > -1) {
			String a = content.substring(i, i+5);
			StringBuffer b = new StringBuffer();
			for(int j = 0; j<a.length(); j++) {
				char c = a.charAt(j);
			}
			log.info(a + " " + a.replace("\365", "ő"));
		} else {
			//log.info("not found");
		}
		
		String content = sb.toString()
					.replaceAll("\\0365", "�")  // ő=245=\365=\337
					.replaceAll("\\0373", "�"); // ű=251=\373=\369
		*/
	
		return content;
	}

	public static String getPageTokenContent(String pdfFileName, int pageNo)
		throws IOException 
	{

		RandomAccessFileOrArray ra = new RandomAccessFileOrArray(
				pdfFileName, false, true);
		PdfReader reader = new PdfReader(ra, new byte[0]);
		byte[] contentStream = reader.getPageContent(pageNo);
		
		PRTokeniser tokenizer = new PRTokeniser(contentStream);
		
		StringBuffer sb = new StringBuffer();
		while (tokenizer.nextToken()) {
			if(tokenizer.getTokenType() == 2)
				sb.append("\t");
			sb.append(tokenizer.getTokenType() + ": ");
			sb.append(tokenizer.getStringValue() + "\n");
		}
		String content = sb.toString()
						.replaceAll("\\0365", "ő")  // ő=245=\365=\337
						.replaceAll("\\0373", "ű"); // ű=251=\373=\369
		
		return content;
	}
	
	public static PdfTocConfig readBookmarkInfo(String pdfFilePath, 
			String tocFilePath, PdfTocConfig pdfTocConfig) 
		throws FileNotFoundException, IOException, PdfTocConfigException {
		log.info("Processing toc file: " + tocFilePath);
		RandomAccessFileOrArray ra = new RandomAccessFileOrArray(pdfFilePath, false, true);
		PdfReader reader = new PdfReader(ra, new byte[0]);
		pdfTocConfig.setNumberOfPages(reader.getNumberOfPages());

		InputStream is = new FileInputStream(new File(tocFilePath));
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		Stack path = new Stack();
		int[] siblingCounters = new int[20];
		siblingCounters[0] = 0;
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			Pattern regex = Pattern.compile("^(\t*)([^\t]+?)(?:,([0-9]+))?$");
			Matcher matcher = regex.matcher(line);
			if(matcher.find()) {
				String tabs = matcher.group(1);
				String title = matcher.group(2);
				String pageNumber = matcher.group(3);
				if(tabs.length() == path.size()-1 && path.size() > 0) {
					path.pop();
				} else if(tabs.length() < path.size()) {
					while(tabs.length() < path.size()) {
						if(tabs.length()+1 < path.size())
							siblingCounters[path.size()] = 0;
						path.pop();
					}
				}
				// if page number is null, page = parent's page number
				if(pageNumber == null) {
					if(path.size() > 0) {
						String parent = (String)path.lastElement();
						pageNumber = parent.substring(
							pdfTocConfig.getPrefix().length() + pdfTocConfig.getYear().length(), 
							parent.indexOf(":"));
					} else {
						pageNumber = "1";
						log.info("no pageNumber and parent: " + line);
					}
				}
				PDFBookmarkEntry bookmark = new PDFBookmarkEntry(
						title, // title 
						new Integer(pageNumber), // pageNumber 
						pdfTocConfig.getPrefix() + pdfTocConfig.getYear() + pageNumber); // name
				path.push(bookmark.getName() + ':' + title);
				siblingCounters[path.size()]++;
				bookmark.setSiblingCounter(siblingCounters[path.size()]);
				bookmark.setPath(path);
				if(path.size() > 1) {
					bookmark.setLogicalParent((String) path.get(path.size()-2));
				} else {
					bookmark.setLogicalParent(
						pdfTocConfig.getPrefix() + pdfTocConfig.getYear() 
						+ ":" + pdfTocConfig.getYear());
				}

				String args[] = { bookmark.printPath(), title };
				String fieldName = pdfTocConfig.getFieldName(args);
				bookmark.setFieldName(fieldName);
				pdfTocConfig.addBookmarkEntry(bookmark);
			} else {
				System.out.println(line);
			}
		}
		pdfTocConfig.checkAllPages();
		return pdfTocConfig;

	}
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		try {
			PDFUtils.extractPage("/home/moki/work/arcanum_pdf/content/big.pdf", 100, 
					"/home/moki/work/arcanum_pdf/content/100.pdf");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Duration: " 
				+ MilliSecFormatter.toString(System.currentTimeMillis()-start));
	}
}
	
class PDFReferences {
	
	int trailerpos;
	
	int xref[];

    static final int LogBuffSz = 16; // 64K buffer
    public static final int BuffSz = (1 << LogBuffSz);
    
    /**
     * File format: 
     * - first 4 bytes specify the trailer position
     * - 4 bytes: with the first bit to specify if.... 
     * @param pdfFileName
     * @throws IOException
     */
    public void saveIndexInfo(String pdfFileName) throws IOException{
		
		FileOutputStream fo = new FileOutputStream(getIndexFileName(pdfFileName));
		File f = new File(pdfFileName);
		long length = f.length();
		fo.write(intToByteArray((int)length));
		fo.write(intToByteArray(trailerpos));
		for (int i = 0; i < xref.length; i++) {
			fo.write(intToByteArray(xref[i]));
		}
		fo.flush();
		fo.close();
		
	}
	
    public static boolean checkIndexFile(String pdfFileName) throws IOException{
		
		byte[] b = new byte[4];
		File f = new File(getIndexFileName(pdfFileName));
		if (!f.exists())
			return false;
		
		FileInputStream fi = new FileInputStream(getIndexFileName(pdfFileName));
		
		int len = fi.read(b);
		if (len < 4){
			return false;
		}
		int filelength = byteArrayToInt(b, 0);
		File pdf = new File(pdfFileName);
		if ((int)pdf.length() != filelength){
			return false;
		}	
		return true;
		
    }
    
    
	public void loadIndexInfo(String pdfFileName) throws IOException{
		
		byte[] b = new byte[BuffSz];
		File f = new File(getIndexFileName(pdfFileName));
		long length = f.length();
		xref = new int[(int)(length/4 - 2 )];
		
		FileInputStream fi = new FileInputStream(getIndexFileName(pdfFileName));
		
		int pos = 0;
		
		int len = fi.read(b);
		int filelength = byteArrayToInt(b, 0);
		File pdf = new File(pdfFileName);
		
		if ((int)pdf.length() != filelength){
			throw new IOException("File does not corresponds with the index");
		}
		trailerpos = byteArrayToInt(b, 4);
		int bPos = 8;
		do {
			if (len <= 0)
				break;
		
			while (bPos < len ){
				xref[pos] = byteArrayToInt(b, bPos);
				pos++;
				bPos = bPos + 4;
			}
			bPos = 0;
			len = fi.read(b);
		} while (true);
		
		fi.close();
	}
	public int getTrailerpos() {
		return trailerpos;
	}

	public void setTrailerpos(int trailerpos) {
		this.trailerpos = trailerpos;
	}

	public int[] getXref() {
		return xref;
	}

	public void setXref(int[] xref) {
		this.xref = xref;
	}
	
	public static final byte[] intToByteArray(int value) {
		return new byte[]{
		(byte)(value >>> 24), (byte)(value >> 16 & 0xff), (byte)(value >> 8 & 0xff), (byte)(value & 0xff) };
	}
	
	public static final byte[] shortintToByteArray(int value) {
		return new byte[]{
		(byte)(value >> 8 & 0xff), (byte)(value & 0xff) };
	}
	
	public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
	
	public static int byteArrayToShortInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 2; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
	
	public static String getIndexFileName(String filename){
		return filename + ".ind";
	}
}
