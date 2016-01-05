<?xml version='1.0' encoding="UTF-8"?>
<!-- Default indexsheet for HTML -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:in="http://www.tesujionline.com.org/default-html-indexsheet" >

    <!-- Keyword is a searchable word in the text -->
    <xsl:attribute-set name = "keyword" > 
       <xsl:attribute name = "indexed"         >true</xsl:attribute> 
       <xsl:attribute name = "stored"          >false</xsl:attribute> 
       <xsl:attribute name = "tokenized"       >true</xsl:attribute> 
       <xsl:attribute name = "termVectorStored">false</xsl:attribute> 
    </xsl:attribute-set>
        
    <xsl:attribute-set name = "text" > 
       <xsl:attribute name = "indexed"         >true</xsl:attribute> 
       <xsl:attribute name = "stored"          >false</xsl:attribute> 
       <xsl:attribute name = "tokenized"       >true</xsl:attribute> 
       <xsl:attribute name = "termVectorStored">false</xsl:attribute> 
    </xsl:attribute-set>

  <xsl:template match = "/" >
    <xsl:text>&#xa;</xsl:text>
    <in:index>
      <xsl:apply-templates/>
    </in:index>
    <xsl:text>&#xa;</xsl:text>
  </xsl:template>

    <xsl:template match='META'>
        <xsl:element name = "in:index" use-attribute-sets = "keyword"> 
            <xsl:attribute name = "field" > 
                <xsl:value-of select = "@name" /> 
            </xsl:attribute> 
      </xsl:element>
    </xsl:template>

    <xsl:template match='SPAN|span'>
      <xsl:element name = "in:index" use-attribute-sets = "text">
        <xsl:attribute name = "field" ><xsl:value-of select = "@class" /></xsl:attribute>
        <xsl:apply-templates/>
      </xsl:element> 
    </xsl:template>
  
  
    <xsl:template match='HEAD'>
        <xsl:apply-templates/>
    </xsl:template>
 
    <xsl:template match='TITLE'>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match='H1|H2|H3|H4|H5|H6|H7|H8|H9|H10|H11|H12'>
      <xsl:text>&#xa;</xsl:text>
      <xsl:element name="in:index" use-attribute-sets="text">
        <xsl:attribute name="field">content</xsl:attribute>
        <xsl:element name = "in:index" use-attribute-sets = "keyword"> 
          <xsl:attribute name = "field">title</xsl:attribute> 
          <xsl:apply-templates/>
        </xsl:element>
      </xsl:element>
    </xsl:template>
    

    <xsl:template match="P|p">
      <xsl:element name="in:index" use-attribute-sets = "text">
        <xsl:attribute name = "field">content</xsl:attribute>
        <!-- xsl:attribute name="orig" ><xsl:value-of select="name(.)" /></xsl:attribute -->
        <xsl:apply-templates/>
      </xsl:element>
    </xsl:template>

    <xsl:template match="BODY">
      <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="ADDRESS|BR|BLOCKQUOTE|BUTTON|CENTER|DD|DT|DIV|FORM|FRAME|HR|IFRAME|IMG|INPUT|ISINDEX|LI|NOFRAMES|NOSCRIPT|NOEMBED|OBJECT|OPTION|PRE|PLAINTEXT|SPACER|TR|TD|TH|TABLE|TEXTAREA|WBR">
        <xsl:apply-templates/>
    </xsl:template>
</xsl:stylesheet>

