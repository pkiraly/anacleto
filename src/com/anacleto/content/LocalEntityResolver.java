/*
 * Created on Apr 15, 2005
 *
 */
package com.anacleto.content;

import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author robi
 *
 */
public class LocalEntityResolver implements EntityResolver{

    public LocalEntityResolver() {
    }
    
    public InputSource resolveEntity(String publicId, String systemId)
            throws IOException, SAXException {
        InputSource retval = null;

        if (retval == null) {
            // we're sooo paranoid here!!
             retval = new InputSource(new StringReader(""));
            retval.setSystemId(systemId);
            retval.setPublicId(publicId);
        }
        // if we returned null, the systemId would would
        // be dereferenced using standard URL handling.
        return retval;
    }
}
