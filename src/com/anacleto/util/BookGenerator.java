package com.anacleto.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.collections.list.TreeList;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.*;

import com.anacleto.view.TermBean;

/**
 * Utility class to generate books.xml file from a directory structure
 * 
 * @author robi
 * 
 */
public class BookGenerator {

    public String listXml(IndexSearcher is, File currentFile, int level) throws Exception {
        String retStr = "";
        //File currentFile = new File(location);
        if (!currentFile.exists())
            throw new Exception("Inexistent file");

        String name = currentFile.getName();
        if (currentFile.isDirectory()) {
            String subLevel = "";
            File child[] = currentFile.listFiles();
            for (int i = 0; i < child.length; i++) {
                 subLevel = subLevel + listXml(is, child[i], level + 1);
            }
            
            if (level == 1){
                //find first child:
                File childOfChild[] = child[0].listFiles();
                String nameCC = childOfChild[0].getName();
                nameCC = nameCC.substring(0, nameCC.length() - 4);
                BooleanQuery query = new BooleanQuery();
                query.add(new TermQuery(new Term("name", nameCC)), true, false);
                query.add(new TermQuery(new Term("parentName", nameCC)), true, false);
                
                Hits hits = is.search(query);
                String author = "";
                for (int i = 0; i < hits.length(); i++) {
                    
                    Field auth = hits.doc(i).getField("author");
                    if (auth != null){
                        author = auth.stringValue();
                        System.out.println(author);
                    }
                }
                
                retStr = retStr + "<shelf name=\"" + name + 
                			"\" title=\"" + author + "\">\n";
                retStr = retStr + subLevel;
                retStr = retStr + "</shelf>\n";
            } else{
                retStr = retStr + subLevel;
            }
            

        } else {
            String bookName = name.substring(0, name.length() - 4);
            String bookLoca = currentFile.getCanonicalPath();
            
            retStr = retStr + getBookInfo(is, bookName, bookLoca);
        }
        return retStr;
    }

    public String getBookInfo(IndexSearcher is, String name, String location) throws IOException, ParseException {
        String retStr = "";

        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("name", name)), true, false);
        query.add(new TermQuery(new Term("parentName", name)), true, false);
        
        Hits hits = is.search(query);
        
        for (int i = 0; i < hits.length(); i++) {
            String title = hits.doc(i).getField("title").stringValue();
            title = replaceUnwantedChars(title);
            retStr = retStr + "<book name=" +  "\""+name+"\"" 
            + " title=\"" +  title + "\""
            + " location=\"" + location + "\" />\n";
        }
        
          
        return retStr;
    }
    
    private String replaceUnwantedChars(String in) {
        String out = in.replace('"', ' ');
        out = out.replace('\'', ' ');
        out = out.replace('\n', ' ');
        out = out.replace('\r', ' ');
        return out;
    }
    
    public String getAuthors() throws IOException, ParseException {
        IndexReader reader = IndexReader
                .open("C:/java/jboss-4.0.1/server/default/deploy/jbossweb-tomcat50.sar/ROOT.war/indexdir");
        IndexSearcher is = new IndexSearcher(
                "C:/java/jboss-4.0.1/server/default/deploy/jbossweb-tomcat50.sar/ROOT.war/indexdir");
        Collection termColl = new TreeList();

        String retStr = "";
        TermEnum terms = reader.terms();
        while (terms.next()) {
            if (terms.term().field().toLowerCase().equals("author")) {
                termColl.add(new TermBean(terms.term().text(),  
                		terms.docFreq(), null));
                retStr = retStr + "<shelf name=\"" + terms.term().text()
                        + "\" title=\"" + terms.term().text() + "\">\n";
                //System.out.println(terms.term().text());

                BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
                //Query query = QueryParser.parse( terms.term().text(),
                // "author", new StandardAnalyzer());

                Query query = new TermQuery(new Term("author", terms.term()
                        .text()));
                query = is.rewrite(query);
                Hits hits = is.search(query);
                for (int i = 0; i < hits.length(); i++) {
                    Document doc = hits.doc(i);
                    retStr = retStr + "<book ";
                    retStr = retStr + " name=\""
                            + doc.getField("name").stringValue() + "\"\n";
                    retStr = retStr + " title=\""
                            + doc.getField("title").stringValue() + "\"\n";
                    retStr = retStr + " location=\""
                            + doc.getField("location").stringValue() + "\"\n";
                    retStr = retStr + "/>\n";
                }
                retStr = retStr + "</shelf>\n";
            }

        }
        return retStr;
    }

    public static void main(String[] args) {

        BookGenerator book = new BookGenerator();
        try {
            IndexSearcher is = new IndexSearcher(
            "C:/java/jboss-4.0.1/server/default/deploy/jbossweb-tomcat50.sar/ROOT.war/indexdir");
            
            try {
                FileWriter out = new FileWriter("c:/java/temp/book.txt");
                BufferedWriter buffWriter = new BufferedWriter(out);
                buffWriter.write(book.listXml(is, new File("c:/java/temp/archivio"), 0));
                buffWriter.close();
                //book.listXml(new File("c:/java/temp/archivio_frag"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}