package com.anacleto.parsing;

import java.io.File;

import junit.framework.TestCase;

import com.anacleto.base.BooksFileParser;

public class TestSaxHandlerWithStack extends TestCase{
	
	private javax.xml.transform.Transformer transformer;

	public void testParse() throws Exception {
/*
		TransformerFactory tFactory = TransformerFactory.newInstance();
		transformer = tFactory.newTransformer();

		StreamSource src = new StreamSource(getClass().getClassLoader().
				getResourceAsStream("com/anacleto/parsing/good.xml"));
		SAXResult saxRes = new SAXResult();
		
		NodeHandler handler = new NodeHandler();
		saxRes.setHandler(handler);

		transformer.transform(src, saxRes);
*/
		/*
		src = new StreamSource(getClass().getClassLoader().
				getResourceAsStream("com/anacleto/parsing/bad.xml"));
		saxRes.setHandler(handler);

		transformer.transform(src, saxRes);
		*/

		/*
		BooksFileParser parser = new BooksFileParser();
		parser.parse(new File((getClass().getClassLoader().
				getResource("com/anacleto/parsing/good.xml")).getFile()));
		*/
		BooksFileParser parser = new BooksFileParser();
		parser.parse(new File((getClass().getClassLoader().
				getResource("com/anacleto/parsing/big.xml")).getFile()));
		

		//transformer.transform(src, saxRes);

	}

}

final class NodeHandler extends SAXHandlerWithStack {

	public void startElement(SAXHandlerNode node) {
		System.out.println("test start...");
	}
	/* (non-Javadoc)
	 * @see com.anacleto.parsing.SAXHandlerWithStack#endElement(com.anacleto.parsing.SAXHandlerNode)
	 */
	public void endElement(SAXHandlerNode node) {
		System.out.println("test end....");
	}


}
