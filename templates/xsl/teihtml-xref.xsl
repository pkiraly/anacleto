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

  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">


<!-- cross-referencing -->

<!-- work out an ID for a given <div> -->
<xsl:template match="*" mode="ident">
  <xsl:variable name="BaseFile">
    <xsl:value-of select="$masterFile"/>
    <!-- xsl:message>[teihtml-xref.xsl:43] mode="ident"->addCorpusID <xsl:value-of select="name()" /></xsl:message -->
    <xsl:call-template name="addCorpusID"/>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="@id">
      <!--* xsl:message>[teihtml-xref.xsl:49] mode="ident" has @id</xsl:message *-->
      <xsl:choose>
        <xsl:when test="$useIDs">
          <xsl:value-of select="@id"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$BaseFile"/>-<xsl:value-of select="name(.)"/>-<xsl:value-of select="generate-id()"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>

    <xsl:when test="self::div and not(ancestor::div)">
      <xsl:message>[teihtml-xref.xsl:61] mode="ident" self::div</xsl:message>
      <xsl:variable name="xpath">
        <xsl:for-each select="ancestor-or-self::*">
          <xsl:value-of select="name()" />
          <xsl:text />.<xsl:number />
          <xsl:if test="position() != last()">_</xsl:if>
        </xsl:for-each>
      </xsl:variable>
      <xsl:value-of select="substring-after($xpath,'TEI.2.1_text.1_')"/>
    </xsl:when>

    <xsl:when test="self::divGen">
      <xsl:message>[teihtml-xref.xsl:73] mode="ident" self::divGen</xsl:message>
      <xsl:variable name="xpath">
        <xsl:for-each select="ancestor-or-self::*">
          <xsl:value-of select="name()" />
          <xsl:text />.<xsl:number />
          <xsl:if test="position() != last()">_</xsl:if>
        </xsl:for-each>
      </xsl:variable>
      <xsl:value-of select="substring-after($xpath,'TEI_.1_text.1_')"/>
   </xsl:when>

   <xsl:otherwise>
     <!--* xsl:message>[teihtml-xref.xsl:85] mode="ident" otherwise</xsl:message *-->
     <xsl:variable name="hashDoc" select="concat('../', $filenamePrefix, 'hash.xml')" />
     
     <xsl:variable name="counter" select="document($hashDoc)/hash" />
     <xsl:variable name="id" select="generate-id()" />
     <!--* xsl:message>[teihtml-xref.xsl:86] <xsl:value-of select="$id"/> - <xsl:value-of select="$counter/counter[@id = $id]/@count" /> </xsl:message *-->
     <!-- xsl:value-of select="$BaseFile"/>-<xsl:value-of select="name(.)"/>-<xsl:value-of select="generate-id()"/ -->
     <xsl:value-of select="$counter/counter[@id = $id]/@count" />
     <!-- 
     <xsl:value-of select="count( ancestor::*[starts-with(name(),'div')]/head
	         | preceding::*[starts-with(name(),'div')]/head
	         | self::*/head )"/>
	   -->
   </xsl:otherwise>
 </xsl:choose>
</xsl:template>

<!-- when a <div> is referenced, see whether its  plain anchor, 
 or needs a parent HTML name prepended -->


<xsl:template match="TEI.2" mode="xrefheader">
  <xsl:message>[teihtml-xref.xsl:105] match="TEI.2" mode="xrefheader"</xsl:message>
  <xsl:variable name="BaseFile">
    <xsl:value-of select="$masterFile"/>
    <xsl:message>[teihtml-xref.xsl:108] "TEI.2" mode="xrefheader"->addCorpusID</xsl:message>
    <xsl:call-template name="addCorpusID"/>
  </xsl:variable>
  <xsl:value-of select="concat($BaseFile,$standardSuffix)"/>
</xsl:template>

<xsl:template match="*" mode="xrefheader">
  <!-- xsl:message>[teihtml-xref.xsl:115] match="*" mode="xrefheader"</xsl:message -->

  <xsl:variable name="ident">
    <xsl:apply-templates select="." mode="ident"/>
  </xsl:variable>

  <xsl:variable name="depth">
    <xsl:apply-templates select="." mode="depth"/>
  </xsl:variable>

  <xsl:variable name="Hash">
    <xsl:if test="$makeFrames='true' and not($STDOUT='true')">
      <xsl:value-of select="$masterFile"/>
      <xsl:message>[teihtml-xref.xsl:114] "*" mode="xrefheader"->addCorpusID</xsl:message>
      <xsl:call-template name="addCorpusID"/>
      <xsl:text>.html</xsl:text>
    </xsl:if>
    <xsl:text>#</xsl:text>
  </xsl:variable>

  <xsl:choose>

    <xsl:when test="$rawIE='true' and $depth &lt;= $splitLevel">
      <xsl:text>JavaScript:void(gotoSection('','</xsl:text>
      <xsl:value-of select="$ident"/>
      <xsl:text>'));</xsl:text>
    </xsl:when>

    <xsl:when test="$STDOUT='true' and $depth &lt;= $splitLevel">
      <xsl:value-of select="$masterFile"/>
      <xsl:value-of select="$urlChunkPrefix"/>
      <xsl:value-of select="$ident"/>
    </xsl:when>

    <xsl:when test="ancestor::back and not($splitBackmatter)">
      <xsl:value-of select="concat($Hash,$ident)"/>
    </xsl:when>

    <xsl:when test="ancestor::front and not($splitFrontmatter)">
      <xsl:value-of select="concat($Hash,$ident)"/>
    </xsl:when>

    <xsl:when test="$splitLevel= -1 and ancestor::teiCorpus.2">
      <xsl:value-of select="$masterFile"/>
      <xsl:message>[teihtml-xref.xsl:145] "*" mode="xrefheader"->addCorpusID</xsl:message>
      <xsl:call-template name="addCorpusID"/>
      <xsl:value-of select="$standardSuffix"/>
      <xsl:value-of select="concat($Hash,$ident)"/>
    </xsl:when>

    <xsl:when test="$splitLevel= -1">
      <xsl:value-of select="concat($Hash,$ident)"/>
    </xsl:when>

    <xsl:when test="$depth &lt;= $splitLevel">
      <xsl:value-of select="concat($ident,$standardSuffix)"/>
    </xsl:when>

    <xsl:otherwise>

      <xsl:variable name="parent">
        <xsl:call-template name="locateParentdiv"/>
      </xsl:variable>
      
      <xsl:choose>

        <xsl:when test="$rawIE='true'">
          <xsl:message>$rawIE='true'</xsl:message>
          <xsl:text>JavaScript:void(gotoSection("</xsl:text>
          <xsl:value-of select="$ident"/>
          <xsl:text>","</xsl:text>
          <xsl:value-of select="$parent"/>
          <xsl:text>"));</xsl:text>
        </xsl:when>

        <xsl:when test="$STDOUT='true'">
          <xsl:message>$STDOUT='true'</xsl:message>
          <xsl:value-of select="$masterFile"/>
          <xsl:text>.ID=</xsl:text>
          <xsl:value-of select="$parent"/>
          <xsl:value-of select="concat($standardSuffix,'#')"/>
          <xsl:value-of select="$ident"/>
        </xsl:when>

        <xsl:otherwise>
          <!-- xsl:message>otherwise <xsl:value-of select="$parent"/></xsl:message -->
          <xsl:value-of select="$parent"/>
          <xsl:value-of select="concat($standardSuffix,'#')"/>
          <xsl:value-of select="$ident"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="locateParentdiv">
  <!-- xsl:message>[teihtml-xref.xsl:225] locateParentdiv <xsl:value-of select="$splitLevel" /></xsl:message -->

  <xsl:choose>
    <xsl:when test="ancestor-or-self::div and $splitLevel &lt; 0">
       <xsl:apply-templates select="ancestor::div[last()]" mode="ident"/>
    </xsl:when>
    <xsl:when test="ancestor-or-self::div">
      <xsl:apply-templates select="ancestor::div[last() - $splitLevel]" mode="ident"/>
    </xsl:when>

    <xsl:otherwise>
      <!--xsl:message>locateParentdiv <xsl:apply-templates select="(ancestor::div3)[last()]/@id" /></xsl:message-->
      <xsl:choose>
        <xsl:when test="$splitLevel = 0">
          <xsl:apply-templates select="ancestor::div1|ancestor::div0" mode="ident"/>
        </xsl:when>
        <xsl:when test="$splitLevel = 1">
          <xsl:apply-templates select="(ancestor::div2|ancestor::div1|ancestor::div0)[last()]" mode="ident"/>
        </xsl:when>
        <xsl:when test="$splitLevel = 2">
          <xsl:apply-templates select="(ancestor::div3|ancestor::div2|ancestor::div1|ancestor::div0)[last()]" mode="ident"/>
        </xsl:when>
        <xsl:when test="$splitLevel = 3">
          <xsl:apply-templates select="(ancestor::div3|ancestor::div2|ancestor::div1|ancestor::div0)[last()]" mode="ident"/>
        </xsl:when>
        <xsl:when test="$splitLevel = 4">
          <xsl:apply-templates select="(ancestor::div0|ancestor::div1|ancestor::div2|ancestor::div3|ancestor::div4)[last()]" mode="ident"/>
        </xsl:when>
        <xsl:when test="$splitLevel = 5">
          <xsl:apply-templates select="(ancestor::div0|ancestor::div1|ancestor::div2|ancestor::div3|ancestor::div4|ancestor::div5)[last()]" mode="ident"/>
        </xsl:when>
        <xsl:when test="$splitLevel = 6">
          <xsl:apply-templates select="(ancestor::div0|ancestor::div1|ancestor::div2|ancestor::div3|ancestor::div4|ancestor::div5|ancestor::div6)[last()]" mode="ident"/>
        </xsl:when>
        <xsl:when test="$splitLevel = 7">
          <xsl:apply-templates select="(ancestor::div0|ancestor::div1|ancestor::div2|ancestor::div3|ancestor::div4|ancestor::div5|ancestor::div6|ancestor::div7)[last()]" mode="ident"/>
        </xsl:when>
        <xsl:when test="$splitLevel = 8">
          <xsl:apply-templates select="(ancestor::div0|ancestor::div1|ancestor::div2|ancestor::div3|ancestor::div4|ancestor::div5|ancestor::div6|ancestor::div7|ancestor::div8)[last()]" mode="ident"/>
        </xsl:when>
        <xsl:when test="$splitLevel = 9">
          <xsl:apply-templates select="(ancestor::div0|ancestor::div1|ancestor::div2|ancestor::div3|ancestor::div4|ancestor::div5|ancestor::div6|ancestor::div7|ancestor::div8|ancestor::div9)[last()]" mode="ident"/>
        </xsl:when>
      </xsl:choose>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="locateParent">
  <xsl:choose>
  <xsl:when test="self::div">
  <xsl:apply-templates select="ancestor::div[last() - $splitLevel + 1]" mode="ident"/>
  </xsl:when>
  <xsl:when test="ancestor::div">
  <xsl:apply-templates select="ancestor::div[last() - $splitLevel]" mode="ident"/>
  </xsl:when>
  <xsl:otherwise>
   <xsl:choose>
    <xsl:when test="$splitLevel = 0">
      <xsl:apply-templates select="ancestor::div1|ancestor::div0" mode="ident"/>
    </xsl:when>
    <xsl:when test="$splitLevel = 1">
      <xsl:apply-templates select="ancestor::div2|ancestor::div1|ancestor::div0" mode="ident"/>
    </xsl:when>
    <xsl:when test="$splitLevel = 2">
      <xsl:apply-templates select="ancestor::div3|ancestor::div2" mode="ident"/>
    </xsl:when>
    <xsl:when test="$splitLevel = 3">
      <xsl:apply-templates select="ancestor::div4|ancestor::div3" mode="ident"/>
    </xsl:when>
    <xsl:when test="$splitLevel = 4">
      <xsl:apply-templates select="ancestor::div5|ancestor::div4" mode="ident"/>
    </xsl:when>
   </xsl:choose>
  </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<!-- 
  <ref id="bevezeto-1f" target="bevezeto-1j">1</ref>
  <ref id="bevezeto-1j" target="bevezeto-1f">1</ref>
-->
<xsl:template match="ref">
  <xsl:call-template name="makeHyperLink">
    <xsl:with-param name="url">
      <!--* xsl:value-of select="concat('@',@target,'@')" / *-->
      <xsl:message>ref->makeHyperLink->url = xrefheader->key('IDS',@target) target: <xsl:value-of select="@target" /> - name: <xsl:value-of select="name(key('IDS',@target))" /></xsl:message>
      <xsl:apply-templates mode="xrefheader" select="key('IDS',@target)" />
    </xsl:with-param>
    <xsl:with-param name="target">
      <xsl:value-of select="@target"/>
    </xsl:with-param>
    <xsl:with-param name="class">
      <xsl:choose>
        <xsl:when test="@rend">
          <xsl:value-of select="@rend"/>
        </xsl:when>
        <xsl:when test="@type">
          <xsl:value-of select="@type"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$class_ref"/>
        </xsl:otherwise>     
      </xsl:choose>
    </xsl:with-param>
    <xsl:with-param name="body">
      <xsl:apply-templates/>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="anchor">
   <a name="{@id}"/>
</xsl:template>

<xsl:template match="note" mode="xrefheader">
  <!--* xsl:message>[teihtml-xref.xsl:317] match="note" mode="xrefheader"</xsl:message *-->
  <xsl:text>#Note</xsl:text>
  <xsl:call-template name="noteID"/>
</xsl:template>


<xsl:template match="label|figure|table|item|p|bibl|anchor|cell|lg|list|sp" 
  mode="xrefheader">
  <!--* xsl:message>[teihtml-xref.xsl:322] label..</xsl:message *-->

  <xsl:variable name="ident">
    <xsl:apply-templates select="." mode="ident"/>
  </xsl:variable>

  <xsl:variable name="file">
    <xsl:apply-templates 
      select="ancestor::*[starts-with(name(),'div')][1]"  
      mode="xrefheader"/>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="starts-with($file,'#')">
      <xsl:text>#</xsl:text><xsl:value-of select="$ident"/>
    </xsl:when>

    <xsl:when test="contains($file,'#')">
      <xsl:value-of select="substring-before($file,'#')"/>
      <xsl:text>#</xsl:text><xsl:value-of select="$ident"/>
    </xsl:when>

    <xsl:otherwise>
      <xsl:value-of select="$file"/>
      <xsl:text>#</xsl:text><xsl:value-of select="$ident"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="ptr">
  <xsl:message>ptr</xsl:message>

  <xsl:call-template name="makeHyperLink">     
    <xsl:with-param name="url">
      <xsl:apply-templates mode="xrefheader" select="key('IDS',@target)"/>
    </xsl:with-param>
    <xsl:with-param name="class">
      <xsl:choose>
        <xsl:when test="@rend"><xsl:value-of select="@rend"/></xsl:when>
        <xsl:otherwise><xsl:value-of select="$class_ptr"/></xsl:otherwise>     
      </xsl:choose>
    </xsl:with-param>
    <xsl:with-param name="body">
      <xsl:variable name="xx">
        <xsl:apply-templates mode="header" select="key('IDS',@target)">
          <xsl:with-param name="minimal" select="$minimalCrossRef"/>
        </xsl:apply-templates>
      </xsl:variable>
      <xsl:value-of select="normalize-space($xx)"/>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="xref">
  <xsl:variable name="url">
    <xsl:call-template name="lookupURL"/>
  </xsl:variable>
  <xsl:call-template name="makeHyperLink">     
    <xsl:with-param name="url"><xsl:value-of select="$url"/></xsl:with-param>
    <xsl:with-param name="class">
      <xsl:choose>
        <xsl:when test="@rend"><xsl:value-of select="@rend"/></xsl:when>
        <xsl:otherwise><xsl:value-of select="$class_quicklink"/></xsl:otherwise>     
      </xsl:choose>
    </xsl:with-param>
    <xsl:with-param name="body">
      <xsl:apply-templates/>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="xptr">
  <xsl:variable name="url">
    <xsl:call-template name="lookupURL"/>
  </xsl:variable>
  <xsl:variable name="URL">
    <xsl:choose>
      <xsl:when test="starts-with($url,'mailto:')">
        <xsl:value-of select="substring-after($url,'mailto:')"/>
      </xsl:when>
      <xsl:when test="starts-with($url,'file:')">
        <xsl:value-of select="substring-after($url,'file:')"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$url"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:call-template name="makeHyperLink">     
    <xsl:with-param name="url"><xsl:value-of select="$url"/></xsl:with-param>
    <xsl:with-param name="class">
      <xsl:choose>
        <xsl:when test="@rend"><xsl:value-of select="@rend"/></xsl:when>
        <xsl:otherwise><xsl:value-of select="$class_quicklink"/></xsl:otherwise>
      </xsl:choose>
    </xsl:with-param>
    <xsl:with-param name="body">
      <xsl:element name="{$fontURL}">
        <xsl:value-of select="$URL"/>
      </xsl:element>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template name="lookupURL">
  <xsl:choose>
    <xsl:when test="@url"><xsl:value-of select="@url"/></xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="unparsed-entity-uri(@doc)"/>
      <xsl:choose>
        <xsl:when test="contains(@from,'id (')">
          <xsl:text>_#</xsl:text>
           <xsl:value-of select="substring(@from,5,string-length(normalize-space(@from))-1)"/>
        </xsl:when>
      </xsl:choose>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="makeAnchor">
  <xsl:if test="@id"><a name="{@id}"/></xsl:if>  
</xsl:template>

<xsl:template name="makeHyperLink">    
  <xsl:param name="url"/>
  <xsl:param name="target"/>
  <xsl:param name="class"/>
  <xsl:param name="body"/>
  <!-- xsl:message>
  url: <xsl:value-of select="$url"/>
  target: <xsl:value-of select="$target"/>
  class: <xsl:value-of select="$class"/>
  body: <xsl:value-of select="$body"/>
  </xsl:message -->
  <a>

    <!--* name *-->
    <xsl:if test="@id">
      <xsl:attribute name="name">
        <xsl:value-of select="@id"/>
      </xsl:attribute>
    </xsl:if>

    <!--* href *-->
    <xsl:if test="$class = 'footnote' or string-length($url) &gt; 0">
      <xsl:attribute name="href">
        <xsl:choose>
          <xsl:when test="$class = 'footnote'" >
            <xsl:text>javascript:void(0)</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$url" />
          </xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
    </xsl:if>

    <!--* onClick *-->
    <xsl:choose>
      <xsl:when test="$class = 'footnote'" >
        <xsl:attribute name="onClick">
          <xsl:text>javascript:showmenu( event, '</xsl:text>
          <xsl:message><xsl:value-of select="@target" /></xsl:message>
          <xsl:value-of select="@target" />
          <xsl:text>');</xsl:text>
        </xsl:attribute>
      </xsl:when>
    </xsl:choose>

    <!--* class *-->
    <xsl:attribute name="class">
      <xsl:choose>
        <xsl:when test="$class = 'footnote'" >
          <xsl:text>note</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$class" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>

    <!--* target *-->
    <xsl:choose>
      <xsl:when test="@rend='noframe'">
        <xsl:attribute name="target">_top</xsl:attribute>
      </xsl:when>
      <xsl:when test="@rend='new'">
        <xsl:attribute name="target">_blank</xsl:attribute>
      </xsl:when>
      <xsl:when test="contains($url,'://') or starts-with($url,'.') or starts-with($url,'/')">
        <xsl:attribute name="target">_top</xsl:attribute>
      </xsl:when>
      <xsl:when test="substring($url,string-length($url),1)='/'">
        <xsl:attribute name="target">_top</xsl:attribute>
      </xsl:when>
      <xsl:when test="$splitLevel=-1">
        <xsl:attribute name="target">_top</xsl:attribute>
      </xsl:when>
    </xsl:choose>

    <!-- link title from "n" attribute -->
    <xsl:if test="@n">
      <xsl:attribute name="title">
        <xsl:value-of select="@n"/>
      </xsl:attribute> 
    </xsl:if>
    <!-- deal with extra attributes -->
    <xsl:call-template name="xrefHook"/>
    <xsl:value-of select="$body"/>
  </a>
</xsl:template>

</xsl:stylesheet>
