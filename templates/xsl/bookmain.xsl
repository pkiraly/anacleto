<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 
<xsl:output
  method="html"
  encoding="utf-8"
  indent="yes"
  omit-xml-declaration="yes"
  />
<xsl:strip-space elements="*" />

<xsl:template match="/" >
  <xsl:apply-templates select="bibl" />
</xsl:template>

<xsl:template match="bibl" >
  <html>
  <head>
    <title><xsl:value-of select="concat( author, ': ', title )"/></title>
  <style>
  <![CDATA[
    body         { font-family: garamond, serif; }
    table.cimlap { font-size: 200%; align: center; text-align: center; }
    td.author    { font-size: 150%; font-style: italic; }
    td.title     { font-size: 200%; font-weight: bold; }
    td.rest      { font-size: 80%; }
    span.edition { font-size: 60%; vertical-align: 5pt; }
  ]]>
  </style>
  </head>
  <body>
  <table align="center" class="cimlap"> 
  <xsl:apply-templates select="author"/>
  <xsl:apply-templates select="title"/>
  <xsl:apply-templates select="editor"/>
  <xsl:call-template name="publication"/>
  
  </table> 
  </body>
  </html>
</xsl:template>
        
<xsl:template match="author" >
  <tr><td class="author"><xsl:value-of select="." /></td></tr>
</xsl:template>

<xsl:template match="title" >
  <tr><td class="title"><xsl:value-of select="." /></td></tr>
</xsl:template>

<xsl:template match="editor" >
  <tr><td class="rest"><xsl:value-of select="." /><xsl:text> (ed.)</xsl:text></td></tr>
</xsl:template>

<xsl:template name="publication" >
  <tr>
    <td class="rest">
      <xsl:value-of select="pubPlace" />
      <xsl:text> : </xsl:text>
      <xsl:value-of select="publisher" />
      <xsl:text>, </xsl:text>
      <xsl:value-of select="pubDate" /><xsl:apply-templates select="edition"/>
    </td>
  </tr>
</xsl:template>

<xsl:template match="edition" >
  <span class="edition"><xsl:value-of select="." /></span>
</xsl:template>


</xsl:stylesheet>