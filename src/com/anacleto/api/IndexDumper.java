package com.anacleto.api;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

public class IndexDumper {
	
	public IndexDumper(){
	}
	
	public void destroy(){
	}
	
	public void getDump(String indexdir, String filename) throws IOException {
		
		File f = new File(filename);
		Writer w = new FileWriter(f);
		IndexReader reader   = IndexReader.open(indexdir);
		
		Collection fields = reader.getFieldNames();
		int docNum = reader.maxDoc();
		for (int i = 0; i < docNum; i++) {
			if (reader.isDeleted(i))
				continue;
			
			Document doc = reader.document(i);
			Iterator it = fields.iterator();
			while (it.hasNext()) {
				String field = (String) it.next();
				if (field.equals("content"))
					continue;
				
				String val = doc.get(field);
				if (val == null)
					continue;
				
				if (val.length() > 100)
					val = val.substring(0, 100);
				w.write(field + ":" + val + "\t");
			}
			w.write("\n");
		}
		
		w.close();
	}
	public static void main(String[] args) {
		IndexDumper d = new IndexDumper();
		try {
			d.getDump("g:\\home\\moki\\gutenberg\\admin\\index", "c:\\dump.txt");
			d.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
