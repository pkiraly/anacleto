<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:in="http://www.tesujionline.com/default-tei2-indexsheet" >

<xsl:strip-space elements="*" />

<!-- Keyword is a searchable word in the text -->
<xsl:attribute-set name = "keyword" >
	<xsl:attribute name = "indexed"         >true</xsl:attribute> 
	<xsl:attribute name = "stored"          >true</xsl:attribute> 
	<xsl:attribute name = "tokenized"       >false</xsl:attribute> 
	<xsl:attribute name = "termVectorStored">false</xsl:attribute> 
</xsl:attribute-set>

<xsl:attribute-set name = "text" >
	<xsl:attribute name = "indexed"         >true</xsl:attribute>
	<xsl:attribute name = "stored"          >false</xsl:attribute>
	<xsl:attribute name = "tokenized"       >true</xsl:attribute>
	<xsl:attribute name = "termVectorStored">false</xsl:attribute>
</xsl:attribute-set>

<xsl:template match="/" >
	<xsl:apply-templates select="articles/record" />
</xsl:template>

<xsl:template match="record">
  <xsl:element name="in:index">     
    <xsl:attribute name="id">
      <xsl:value-of select="id" />
    </xsl:attribute>
  </xsl:element>
  <xsl:text>&#xa;</xsl:text>
</xsl:template>

</xsl:stylesheet>