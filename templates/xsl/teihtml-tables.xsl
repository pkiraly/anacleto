<!-- 
Text Encoding Initiative Consortium XSLT stylesheet family
$Date$, $Revision$, $Author$

XSL stylesheet to format TEI XML documents to HTML or XSL FO

 
Copyright 1999-2003 Sebastian Rahtz / Text Encoding Initiative Consortium
                          
    This is an XSLT stylesheet for transforming TEI (version P4) XML documents

    Version 3.1. Date Mon Feb 16 20:22:48 GMT 2004

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
                                                                                
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
                                                                                
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
                                                                                
    The author may be contacted via the e-mail address

    sebastian.rahtz-services.oxford.ac.uk--> 
<xsl:stylesheet
  xmlns:tei="http://www.tei-c.org/ns/1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  version="1.0">

<xsl:template match="table[@rend='simple']">
  <table>
    <xsl:for-each select="@*">
      <xsl:if test="name(.)='summary'
                 or name(.) = 'width'
                 or name(.) = 'border'
                 or name(.) = 'frame'
                 or name(.) = 'rules'
                 or name(.) = 'cellspacing'
                 or name(.) = 'cellpadding'">
        <xsl:copy-of select="."/>
      </xsl:if>
    </xsl:for-each>
    <xsl:call-template name="makeAnchor"/>
    <xsl:apply-templates/>
  </table>
</xsl:template>

<xsl:template match="table|x_table">
 <div>
   <xsl:attribute name="align">
     <xsl:choose>
       <xsl:when test="@align">
         <xsl:value-of select="@align"/>
       </xsl:when>

       <xsl:otherwise>
         <xsl:value-of select="$tableAlign"/>
       </xsl:otherwise>
     </xsl:choose>
   </xsl:attribute>

   <xsl:if test="head">
     <p><xsl:apply-templates select="." mode="header"/></p>
   </xsl:if>

   <xsl:if test="@id">
     <a name="{@id}" />
   </xsl:if>

   <table>
     <xsl:if test="@rend='frame' or @rend='rules'">
       <xsl:attribute name="rules">all</xsl:attribute>
       <xsl:attribute name="border">1</xsl:attribute>
     </xsl:if>

     <xsl:for-each select="@*">
        <xsl:if test="name(.) = 'summary'
                   or name(.) = 'id'
                   or name(.) = 'width'
                   or name(.) = 'border'
                   or name(.) = 'frame'
                   or name(.) = 'rules'
                   or name(.) = 'cellspacing'
                   or name(.) = 'cellpadding'">
         <xsl:copy-of select="."/>
       </xsl:if>
     </xsl:for-each>
     <xsl:apply-templates/>
   </table>
 </div>
</xsl:template>

<xsl:template match="x_tcaption">
  <caption>
     <xsl:for-each select="@*">
       <xsl:copy-of select="."/>
     </xsl:for-each>
    <xsl:apply-templates/>
  </caption>
</xsl:template>

<xsl:template match="x_tfoot">
  <tfoot>
     <xsl:for-each select="@*">
       <xsl:copy-of select="."/>
     </xsl:for-each>
    <xsl:apply-templates/>
  </tfoot>
</xsl:template>

<xsl:template match="x_tbody">
  <tbody>
     <xsl:for-each select="@*">
       <xsl:copy-of select="."/>
     </xsl:for-each>
    <xsl:apply-templates/>
  </tbody>
</xsl:template>

<xsl:template match="x_thead">
  <thead>
     <xsl:for-each select="@*">
       <xsl:copy-of select="."/>
     </xsl:for-each>
    <xsl:apply-templates/>
  </thead>
</xsl:template>

<xsl:template match='row|x_tr'>
 <tr valign="top">
   <xsl:if test="@rend and starts-with(@rend,'class:')">
     <xsl:attribute name="class">
       <xsl:value-of select="substring-after(@rend,'class:')"/>
     </xsl:attribute>
   </xsl:if>
   
   <xsl:if test="not(@role = 'data') and not(@role='')">
     <xsl:attribute name="class"><xsl:value-of select="@role"/></xsl:attribute>
   </xsl:if>
   <xsl:apply-templates/>
 </tr>
</xsl:template>

<xsl:template match="cell | x_td | x_th">
  <xsl:variable name="cellName">
    <xsl:choose>
   	<xsl:when test="name(.) = 'x_th'">th</xsl:when>
   	<xsl:otherwise>td</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

 <xsl:element name="{$cellName}">
 
   <xsl:choose>
     <xsl:when test="@rend and starts-with(@rend,'width:')">
       <xsl:attribute name="width">
         <xsl:value-of select="substring-after(@rend,'width:')"/>
       </xsl:attribute>
     </xsl:when>

     <xsl:when test="@rend and starts-with(@rend,'class:')">
       <xsl:attribute name="class">
         <xsl:value-of select="substring-after(@rend,'class:')"/>
       </xsl:attribute>
     </xsl:when>

     <xsl:when test="@rend and starts-with(@rend,'text-valign:')">
       <xsl:variable name="my_valign"><xsl:value-of select="translate(substring-after(@rend,'text-valign:'), ' ;', '')"/></xsl:variable>
       <xsl:if test="$my_valign != 'top'">
         <xsl:attribute name="valign">
           <xsl:value-of select="$my_valign" />
         </xsl:attribute>
       </xsl:if>
     </xsl:when>
     
     <xsl:when test="@rend">
       <xsl:attribute name="class"><xsl:value-of select="@rend"/></xsl:attribute>
     </xsl:when>

     <!-- xsl:when test="@rend">
       <xsl:attribute name="bgcolor"><xsl:value-of select="@rend"/></xsl:attribute>
     </xsl:when -->

   </xsl:choose>
   
   <xsl:if test="@width">
     <xsl:attribute name="width"><xsl:value-of select="@width" /></xsl:attribute>
   </xsl:if>

   <xsl:if test="@cols">
     <xsl:attribute name="colspan"><xsl:value-of select="@cols" /></xsl:attribute>
   </xsl:if>

   <xsl:if test="@colspan">
     <xsl:attribute name="colspan"><xsl:value-of select="@colspan" /></xsl:attribute>
   </xsl:if>

   <xsl:if test="@valign">
     <xsl:attribute name="valign"><xsl:value-of select="@valign" /></xsl:attribute>
   </xsl:if>

   <xsl:if test="@rows">
     <xsl:attribute name="rowspan"><xsl:value-of select="@rows"/></xsl:attribute>
   </xsl:if>
   
   <xsl:if test="@rowspan">
     <xsl:attribute name="rowspan"><xsl:value-of select="@rowspan"/></xsl:attribute>
   </xsl:if>
   
   <xsl:choose>
     <xsl:when test="@align">
       <xsl:attribute name="align"><xsl:value-of select="@align"/></xsl:attribute>
     </xsl:when>

     <xsl:when test="@text-align and ( @text-align = 'centered' )">
       <xsl:attribute name="align"><xsl:text>center</xsl:text></xsl:attribute>
     </xsl:when>

     <xsl:when test="@text-align">
       <xsl:attribute name="align"><xsl:value-of select="@text-align"/></xsl:attribute>
     </xsl:when>
     
     <xsl:when test="not($cellAlign='left')">
       <xsl:attribute name="align"><xsl:value-of select="$cellAlign"/></xsl:attribute>
     </xsl:when>
   </xsl:choose>

   <xsl:if test="not(@role = 'data') and not(@role='')">
     <xsl:attribute name="class"><xsl:value-of select="@role"/></xsl:attribute>
   </xsl:if>
   
   <xsl:if test="@id"><a name="{@id}"/></xsl:if>

   <xsl:apply-templates/>

  </xsl:element>
</xsl:template>


</xsl:stylesheet>
