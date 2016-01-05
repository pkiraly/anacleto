package com.anacleto.parsing.source;

import com.anacleto.base.Logging;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class PdfTocConfigFactory {

	protected static Logger log = Logging.getIndexingLogger();

	private static HashMap tocConfigs;

	public PdfTocConfigFactory() {
		tocConfigs = new HashMap();
		/*
		tocConfigs.put("parlamenti_naplo",
				com.anacleto.parsing.source.PdfTocConfig4Naplo.class);
		tocConfigs.put("szazadok",
				com.anacleto.parsing.source.PdfTocConfig4Szazadok.class);
		*/
		tocConfigs.put("default",
				com.anacleto.parsing.source.PdfTocConfigDefault.class);
	}

	public PdfTocConfig getRegisteredTocConfigs(String prefix) {
		Class cl;
		cl = (Class) tocConfigs.get(prefix);
		if (cl == null) {
			log.error("TocConfigs class not found for prefix: " + prefix);
			cl = (Class) tocConfigs.get("default");
		}
		if (cl != null) {
			try {
				return (PdfTocConfig) cl.newInstance();
			} catch (Exception e) {
				log.error("Unable to create PdfTocConfig for prefix: " + prefix);
			}
		} else {
			log.error("No registered PdfTocConfig found for prefix: " + prefix);
		}
		return null;
	}
	
	public void registerClass(String prefix, Class className) {
		tocConfigs.put(prefix, className);
	}
}
