package com.anacleto.parsing.filetype;

import java.io.File;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * Tei.2 indexer: Structure: name: book:
 * 
 * @author robi
 */
public class TeiIndexer {

    private javax.xml.transform.Transformer transformer;

    public TeiIndexer(File xslFile) throws TransformerConfigurationException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        transformer = tFactory.newTransformer(new StreamSource(xslFile));
    }


/*
    private void processNode(String bookPrefix, Node currNode,
            int parentChildCount, String parentName) throws Exception {
        NamedNodeMap attribs = currNode.getAttributes();
        Node child = currNode.getFirstChild();

        String content = "";
        int childCount = 0;
        while (child != null) {
            //here we have to see the parent's attributes, how to index a
            // field:
            if (attribs != null
                    && child.getNodeType() == org.w3c.dom.Document.TEXT_NODE) {
                String nodeVal = child.getNodeValue();
                if (nodeVal != null && !nodeVal.trim().equals("")) {
                    content = content + " " + nodeVal;
                }
            }

            processNode(bookPrefix, child, childCount, getNodeAttrubute(
                    attribs, "name"));
            child = child.getNextSibling();
            childCount++;
        }

        if (attribs != null) {
            org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();

            doc.add(Field.Keyword("name", bookPrefix
                    + getNodeAttrubute(attribs, "name")));
            doc.add(Field.Keyword("parentName", bookPrefix + parentName));

            doc.add(Field.Keyword("title", getNodeAttrubute(attribs, "title")));
            doc.add(Field.UnIndexed("location", getNodeAttrubute(attribs,
                    "location")));

            //doc.add( Field.UnIndexed("encoding", page.getEncoding()));
            doc.add(Field.UnIndexed("childcount", String.valueOf(childCount)));
            content = clearText(content);
            if (content != null) {
                doc.add(Field.Text("content", content));
            }
        }

    }

    private String clearText(String text) {

        if (text == null)
            return null;

        text = text.replaceAll("\n", "");
        text = text.replaceAll("\r", "");
        text = text.trim();

        return text;
    }

    private String getNodeAttrubute(NamedNodeMap attribs, String itemName) {

        if (attribs == null)
            return "";

        Node itemNode = attribs.getNamedItem(itemName);
        if (itemNode != null) {
            return itemNode.getNodeValue();
        }
        return "";
    }
    */
}