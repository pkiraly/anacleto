/*
 * Created on Mar 16, 2005
 *
 */
package com.anacleto.parsing.filetype;


import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;

import com.anacleto.base.Logging;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.ParserException;


/**
 * 
 * @author robi
 *
 */
public class OLE2Properties implements FileTypeParser{

	private Logger   logger = Logging.getIndexingLogger();

    public void processStream(InputStream in, BookPage page) throws ParserException{
        POIFSReader r = new POIFSReader();
        
        POIFSListener listener = new POIFSListener();
        r.registerListener(listener, "\005SummaryInformation");
        
        try {
            r.read(in);
        } catch (IOException e) {
            logger.error(e);
        }
        
        if (listener.si == null)
        	return;
        
        page.addTextField("author",   listener.si.getAuthor());
        page.addTextField("keywords", listener.si.getKeywords());
        page.addTextField("title",    listener.si.getTitle());
        page.addTextField("comments", listener.si.getComments());
        page.addTextField("revision", listener.si.getRevNumber());
        page.addTextField("subject",  listener.si.getSubject());
    }

}

final class POIFSListener implements POIFSReaderListener
{
    public SummaryInformation si = null;
    
    public void processPOIFSReaderEvent(POIFSReaderEvent event)
    {
        try
        {
            si = (SummaryInformation)
                 PropertySetFactory.create(event.getStream());
        }
        catch (Exception ex)
        {
            throw new RuntimeException
                ("Property set stream \"" +
                 event.getPath() + event.getName() + "\": " + ex);
        }
    }
}
