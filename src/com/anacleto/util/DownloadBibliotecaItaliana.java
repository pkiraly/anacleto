/*
 * Created on Feb 10, 2005
 *
 */
package com.anacleto.util;

import java.io.*;
import java.net.URL;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author robi
 * 
 */
public class DownloadBibliotecaItaliana {

    public static void parseUrl(String urlStr) throws IOException {

        String prefix = "http://www.bibliotecaitaliana.it";
        System.out.println("CurrentURL: " + prefix + urlStr);
        URL url = new URL(prefix + urlStr);
        InputSource inp = new InputSource(url.openStream());

        DOMParser parser = new DOMParser();
        try {
            parser.parse(inp);
        } catch (SAXException e) {
            //System.out.println("Error while parsing file: " + fileName );
        } catch (IOException e) {
            //System.out.println("Error while reading file: " + fileName );
        }

        Document domSrc = parser.getDocument();
        NodeList lst = domSrc.getElementsByTagName("a");
        for (int i = 0; i < lst.getLength(); i++) {
            Node link = lst.item(i);
            NamedNodeMap attr = link.getAttributes();

            for (int j = 0; j < attr.getLength(); j++) {
                String nodeVal = attr.item(j).getNodeValue();
                if (nodeVal.startsWith(urlStr)
                        && nodeVal.length() > urlStr.length()) {

                    if (nodeVal.endsWith(".xml")) {
                        System.out.println("Download: " + nodeVal);
                        downLoad(prefix, nodeVal);

                    } else {
                        System.out.println("Parse: " + nodeVal);
                        parseUrl(nodeVal);
                    }
                }

                //System.out.println(attr.item(j).getNodeValue());
            }
            //System.out.println( attr.getNamedItem("HREF"));
        }
    }

    public static void downLoad(String prefix, String urlStr)
            throws IOException {
        URL url = new URL(prefix + urlStr);
        InputStream inp = url.openStream();

        File f = new File("C:/java/temp" + urlStr);
        String parent = f.getParent();
        File parentDir = new File(parent);
        parentDir.mkdirs();
        System.out.println("Downloading: " + f.getCanonicalPath());
        f.createNewFile();

        OutputStream out = new FileOutputStream(f);
        byte buffer[] = new byte[256];
        int n = 0;
        while (true) {
            n = inp.read(buffer);
            if (n < 1)
                break;
            out.write(buffer, 0, n);
        }

    }

    public static OutputStream getFileURL(String urlStr) throws IOException {
        String prefix = "http://www.bibliotecaitaliana.it";

        URL url = new URL(prefix + urlStr);
        InputStream inp = url.openStream();

        OutputStream out = new FileOutputStream("C:/java/temp/temp.html");
        byte buffer[] = new byte[256];
        int n = 0;
        while (true) {
            n = inp.read(buffer);
            if (n < 1)
                break;
            out.write(buffer, 0, n);
        }
        return out;
    }

    public static void main(String[] args) {
        try {
            //URL url = new URL("http://www.bibliotecaitaliana.it/archivio/");

            /*
             * Object test = url.getContent(); System.out.println(
             * test.getClass().getName());
             * 
             * PlainTextInputStream s = (PlainTextInputStream)test; byte read[] =
             * new byte[256]; s.read(read);
             */
            //InputSource inp = new InputSource(url.openStream());
            /*
             * FileOutputStream out = new
             * FileOutputStream("C:/java/temp/temp.html"); byte buffer[] = new
             * byte[256]; int n = 0; while (true) { n = inp.read(buffer); if (n <
             * 1) break; out.write(buffer, 0, n); }
             */
            parseUrl("/archivio");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}