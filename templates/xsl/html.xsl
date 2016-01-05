<?xml version='1.0' encoding="UTF-8"?>
<!-- Default indexsheet for HTML -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:in="http://www.tesujionline.com.org/default-html-indexsheet" >

    <!-- Keyword is a searchable word in the text -->
    <xsl:attribute-set name = "keyword" > 
       <xsl:attribute name = "indexed"         >true</xsl:attribute> 
       <xsl:attribute name = "stored"          >true</xsl:attribute> 
       <xsl:attribute name = "tokenized"       >false</xsl:attribute> 
       <xsl:attribute name = "termVectorStored">false</xsl:attribute> 
    </xsl:attribute-set>
        
    <xsl:attribute-set name = "text" > 
       <xsl:attribute name = "indexed"         >true</xsl:attribute> 
       <xsl:attribute name = "stored"          >true</xsl:attribute> 
       <xsl:attribute name = "tokenized"       >true</xsl:attribute> 
       <xsl:attribute name = "termVectorStored">false</xsl:attribute> 
    </xsl:attribute-set>

	<xsl:template match = "/" >
		<in:index>
	 	 	<xsl:apply-templates/>
		</in:index>
	</xsl:template>
	
    <xsl:template match='META'>
        <xsl:element name = "in:index" use-attribute-sets = "keyword"> 
            <xsl:attribute name = "field" > 
                <xsl:value-of select = "@name" /> 
            </xsl:attribute> 
    	</xsl:element>
    </xsl:template>

    <xsl:template match='SPAN'>
    	<xsl:element name = "in:index" use-attribute-sets = "text"> 
            <xsl:attribute name = "field" > 
                <xsl:value-of select = "@class" /> 
                </xsl:attribute> 
        	<xsl:apply-templates/>
    	</xsl:element> 
    </xsl:template>
  
 	
    <!-- Neither hit-anchor nor hit-hilite is allowed within HTML "HEAD" element -->
    <!-- A hit can occur within HTML "HEAD" element when indexing TITLE or other text in heading -->
    <xsl:template match='HEAD'>
        <xsl:apply-templates/>
    </xsl:template>
 
    <!-- It is better to not index title when it is the same for all documents or the same as first heading  -->
    <!-- However the HTML "TITLE" element can be indexed as long as a rule is used to not allow hit-anchor nor hit-hilite  -->
    <xsl:template match='TITLE'>
        <xsl:apply-templates/>
    </xsl:template>

 
    <!-- Generate sub-document table of contents (TOC) hierarchy from HTML headings H1 to H6 -->
    <!-- The first heading found is used as document title -->
    <xsl:template match='H1|H2|H3|H4|H5|H6'>
    	<xsl:element name = "in:index" use-attribute-sets = "keyword"> 
            <xsl:attribute name = "field">title</xsl:attribute> 
        	<xsl:apply-templates/>
    	</xsl:element>
    </xsl:template>
    

    <!-- proximity="paragraph" marks paragraphs for paragraph proximity searching and automatic abstract generation -->
    <!-- break-word is needed when the P element is used without surrounding whitespace to prevent words from being stuck together -->
    <xsl:template match="P">
    	<xsl:element name="in:index" use-attribute-sets = "text"> 
            <xsl:attribute name = "field">content</xsl:attribute> 
        	<xsl:apply-templates/>
    	</xsl:element>
    </xsl:template>

    <!-- A hit-total replace tag is placed at end of BODY element.  The form generatd by it is required for next/prev hit functionality  -->
    <xsl:template match="BODY">
    	<xsl:apply-templates/>
    </xsl:template>

    <!-- break-word rule is needed when the following elements are used without surrounding whitespace to prevent words from being stuck together -->
    <!-- Word breaks rules are now included by default, but can optionally be edited or removed from indexsheet for HTML -->
    <xsl:template match="ADDRESS|BR|BLOCKQUOTE|BUTTON|CENTER|DD|DT|DIV|FORM|FRAME|HR|IFRAME|IMG|INPUT|ISINDEX|LI|NOFRAMES|NOSCRIPT|NOEMBED|OBJECT|OPTION|PRE|PLAINTEXT|SPACER|TR|TD|TH|TABLE|TEXTAREA|WBR">
        <xsl:apply-templates/>
    </xsl:template>
</xsl:stylesheet>

