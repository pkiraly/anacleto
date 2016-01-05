/*
 * Created on Mar 29, 2005
 *
 */
package com.anacleto.parsing.filetype;

import java.io.*;

import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.ParserException;
import com.anacleto.parsing.UnhandledMimeTypeException;

/**
 * Class to give support for the various file formats. Uses parsers for these
 * formats
 * 
 * @author robi
 * 
 */
public class FileContentParser {

    public void processContentType(File file, BookPage page)
            throws FileNotFoundException, ParserException,
            UnhandledMimeTypeException {

        String mime = MimeTypes.getMimeForFileName(file.getName());
        processContentType(mime, file, page);
    }

    public void processContentType(String mime, File file, BookPage page)
            throws ParserException, UnhandledMimeTypeException {

        try {
            InputStream is = new FileInputStream(file);
            processContentType(mime, is, page);
        } catch (FileNotFoundException e) {
            throw new ParserException("Unable to parse file: "
                    + file.getAbsoluteFile() + " Root cause: " + e);
        }

    }

    public void processContentType(String mime, InputStream is, BookPage page)
            throws ParserException, UnhandledMimeTypeException {

        try {
            mime = MimeTypes.getBaseMime(mime);
            mime = MimeTypes.getAliasMime(mime);

            if (mime.equals(MimeTypes.getAliasMime("application/excel"))) {
                ByteArrayInputStream bis = new ByteArrayInputStream(
                        streamToByteArray(is));
                FileTypeParser parser = new ExcelParser();
                parser.processStream(bis, page);

                bis.reset();
                parser = new OLE2Properties();
                parser.processStream(bis, page);

            } else if (mime
                    .equals(MimeTypes.getAliasMime("application/msword"))) {
                ByteArrayInputStream bis = new ByteArrayInputStream(
                        streamToByteArray(is));
                FileTypeParser parser = new WordParser();
                parser.processStream(bis, page);

                bis.reset();
                parser = new OLE2Properties();
                parser.processStream(bis, page);

            } else if (mime.equals(MimeTypes.getAliasMime("application/pdf"))) {
                FileTypeParser parser = new PdfParser();
                parser.processStream(is, page);

            } else if (mime.equals(MimeTypes.getAliasMime("text/html"))) {
                FileTypeParser parser = new HtmlParser();
                parser.processStream(is, page);

            } else if (mime.equals(MimeTypes.getAliasMime("text/xml"))) {
                FileTypeParser parser = new HtmlParser();
                parser.processStream(is, page);

            } else if (mime.equals(MimeTypes.getAliasMime("text/plain"))) {
                FileTypeParser parser = new TextFileParser();
                parser.processStream(is, page);
            } else {
                throw new UnhandledMimeTypeException("Unhandled mime type: "
                        + mime);
            }

        } catch (IOException e) {
            throw new ParserException("IOException occured: " + e);
        }
    }

    private byte[] streamToByteArray(InputStream is) throws IOException {

        byte buffer[] = new byte[1024];
        byte mem[] = new byte[0];
        int n;
        while (true) {
            n = is.read(buffer);
            if (n < 1)
                break;

            byte newMem[] = new byte[mem.length + n];
            System.arraycopy(mem, 0, newMem, 0, mem.length);
            System.arraycopy(buffer, 0, newMem, mem.length, n);
            mem = newMem;
        }
        return mem;
    }

}