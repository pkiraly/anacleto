<?xml version="1.0" encoding="iso-8859-2"?>
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
   version="2.0" >

<!-- ************************************************* -->

<!-- basic handles for divisions -->
<xsl:variable name="linkPrefix" />


<xsl:template match="front/div | front/div0 | front/div1 | front/div2 | front/div3 | front/div4 | front/div5 | front/div6">
  <xsl:variable name="depth">
    <xsl:apply-templates select="." mode="depth" />
  </xsl:variable>
  <xsl:choose>
    <xsl:when test="not($depth &gt; $splitLevel) and $splitFrontmatter">
      <xsl:if test="not($STDOUT='true')">
        <xsl:call-template name="outputChunk">
          <xsl:with-param name="ident">
            <xsl:apply-templates select="." mode="ident" />
          </xsl:with-param>
          <xsl:with-param name="content">
            <xsl:call-template name="writeDiv" />
          </xsl:with-param>
        </xsl:call-template>
      </xsl:if>
    </xsl:when>
    <xsl:otherwise>
      <div>
        <xsl:attribute name="class">
          <xsl:choose>
            <xsl:when test="@type">
              <xsl:value-of select="@type" />
            </xsl:when>
            <xsl:otherwise>teidiv</xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <xsl:call-template name="doDivBody">
          <xsl:with-param name="Type" select="$depth" />
        </xsl:call-template>
      </div>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>


<xsl:template match="back/div | back/div0 | back/div1 | back/div2 | back/div3 | back/div4 | back/div5 | back/div6">
  <xsl:variable name="depth">
     <xsl:apply-templates select="." mode="depth" />
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="not($depth &gt; $splitLevel) and $splitBackmatter">
      <xsl:if test="not($STDOUT='true')">
        <xsl:call-template name="outputChunk">
          <xsl:with-param name="ident">
            <xsl:apply-templates select="." mode="ident" />
          </xsl:with-param>
          <xsl:with-param name="content">
            <xsl:call-template name="writeDiv" />
          </xsl:with-param>
        </xsl:call-template>
      </xsl:if>
    </xsl:when>

    <xsl:otherwise>
      <div class="teidiv">
        <xsl:call-template name="doDivBody">
          <xsl:with-param name="Type" select="$depth" />
        </xsl:call-template>
      </div>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>


<xsl:template match="div | div0 | div1 | div2 | div3 | div4 | div5 | div6"> 

  <!-- establish section depth -->
  <xsl:variable name="depth">
    <xsl:apply-templates select="." mode="depth" />
  </xsl:variable>

  <!-- depending on depth and splitting level, 
       we may do one of two things: -->

  <!--* xsl:message>div <xsl:value-of select="@id" />: <xsl:value-of select="$depth" /></xsl:message *-->
  <xsl:choose>
    <!-- 1. our section depth is below the splitting level -->
    <xsl:when test="$depth &gt; $splitLevel">
      <div class="teidiv">
        <!--* xsl:message>[teihtml-struct.xsl:126] (div|div0)->doDivBody</xsl:message *-->
        <xsl:call-template name="doDivBody">
          <xsl:with-param name="Type" select="$depth" />
        </xsl:call-template>
        <xsl:if test="$sectionUpLink and $depth='0'">
          <xsl:call-template name="topLink" />
        </xsl:if>
      </div>
    </xsl:when>

    <!-- 2. we are at or above splitting level, so start a new file -->
    <xsl:when test="$depth &lt;= $splitLevel">
      <xsl:if test="not($STDOUT='true')">
        <!--* xsl:message>[teihtml-struct.xsl:139] div|div1.. ->outputChunk</xsl:message *-->
        <xsl:call-template name="outputChunk">
          <xsl:with-param name="ident">
            <xsl:apply-templates select="." mode="ident" />
          </xsl:with-param>
          <xsl:with-param name="content">
            <xsl:call-template name="writeDiv" />
          </xsl:with-param>
        </xsl:call-template>
      </xsl:if>
    </xsl:when>
  </xsl:choose>
</xsl:template>


<!-- table of contents -->
<xsl:template match="divGen[@type='toc']">
  <h2><xsl:value-of select="$tocWords" /></h2>
  <xsl:call-template name="maintoc" />
</xsl:template>


<!-- anything with a head can go in the TOC -->
<xsl:template match="*" mode="maketoc">
  <xsl:param name="forcedepth" />
  <xsl:variable name="maketoc">true</xsl:variable>

  <xsl:if test="head or $autoHead='true'">

    <xsl:variable name="Depth">
      <xsl:choose>
        <xsl:when test="not($forcedepth='')">
          <xsl:value-of select="$forcedepth" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$tocDepth" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="thislevel">
      <xsl:choose>
        <xsl:when test="name(.) = 'div'">
          <xsl:value-of select="count(ancestor::div)" />
        </xsl:when>
        <xsl:when test="starts-with(name(.),'div')">
          <xsl:choose>
            <xsl:when test="ancestor-or-self::div0">
              <xsl:value-of select="substring-after(name(.),'div')" />
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="substring-after(name(.),'div') - 1" />
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>99</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="pointer">
      <xsl:apply-templates mode="xrefheader" select="." />
    </xsl:variable>

    <li class="toc">
      <!--* xsl:message>[teihtml-struct.xsl:203] calling header</xsl:message *-->
      <xsl:call-template name="header">
        <xsl:with-param name="toc" select="$pointer" />
        <xsl:with-param name="minimal"></xsl:with-param>
      </xsl:call-template>
      <xsl:if test="$thislevel &lt; $Depth">
        <xsl:call-template name="continuedToc" />
      </xsl:if>
    </li>

  </xsl:if>
</xsl:template>


<xsl:template name="continuedToc">
  <xsl:if test="div|div0|div1|div2|div3|div4|div5|div6">
    <ul class="toc">
      <xsl:apply-templates select="div|div0|div1|div2|div3|div4|div5|div6" mode="maketoc" />
    </ul>
  </xsl:if>
</xsl:template>

<xsl:template match="div|div0|div1|div2|div3|div4|div5|div6" mode="depth">
  <xsl:choose>
    <xsl:when test="name(.) = 'div'">
      <xsl:value-of select="count(ancestor::div)" />
    </xsl:when>
    <xsl:otherwise>
      <xsl:choose>
        <xsl:when test="ancestor-or-self::div0">
          <xsl:value-of select="substring-after(name(.),'div')" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="substring-after(name(.),'div') - 1" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="*" mode="depth">99</xsl:template>


<!-- headings etc -->
<xsl:template match="head">
  <xsl:variable name="parent" select="name(..)" />

  <xsl:if test="not(starts-with($parent,'div'))">
    <xsl:apply-templates/>
  </xsl:if>
</xsl:template>


<xsl:template mode="maketoc" match="head">
  <!--* xsl:message>[teihtml-struct.xsl:265] m.maketoc match="head"</xsl:message *-->
  <!--* xsl:message>[teihtml-struct.xsl:257] <i><xsl:copy-of select="@*|node()" /></i></xsl:message *-->

  <xsl:if test="preceding-sibling::head">
    <!--* xsl:message>[teihtml-struct.xsl:260] maketoc preceding-sibling=yes</xsl:message *-->
    <xsl:text> </xsl:text>
  </xsl:if>
  <xsl:apply-templates mode="plain" />
  <!--* xsl:message>[teihtml-struct.xsl:265] END m.maketoc match="head"</xsl:message -->
</xsl:template>

<xsl:template mode="makeDiv" match="head">
  <!--* xsl:message>[teihtml-struct.xsl:268] m.makeDiv match="head"</xsl:message *-->
  <xsl:message>[teihtml-struct.xsl:269] <xsl:value-of select="." /></xsl:message>

  <xsl:if test="preceding-sibling::head">
    <xsl:message>[teihtml-struct.xsl:260] maketoc preceding-sibling=yes</xsl:message>
    <xsl:text> </xsl:text>
  </xsl:if>
  <xsl:if test="@id">
    <a name="{@id}" />
  </xsl:if>
  <xsl:apply-templates />
</xsl:template>

<xsl:template mode="plain" match="head">
  <!--* xsl:message>[teihtml-struct.xsl:267] m.plain match="head"</xsl:message *-->
  <xsl:if test="preceding-sibling::head">
    <xsl:message>preceding-sibling</xsl:message>
    <xsl:text> </xsl:text>
  </xsl:if>
  <!-- xsl:apply-templates mode="plain"/ -->
  <xsl:apply-templates />
</xsl:template>

<xsl:template match="pb">

  <span class="oldalszam">
    <xsl:attribute name="title">
      <xsl:choose>
        <xsl:when test="@n">
          <xsl:choose>
            <xsl:when test="substring-after(@n,'page')"><xsl:value-of select="substring-after(@n,'page')" /></xsl:when>
            <xsl:otherwise><xsl:value-of select="@n" /></xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:when test="@id">
          <xsl:choose>
            <xsl:when test="substring-after(@id,'page')"><xsl:value-of select="substring-after(@id,'page')" /></xsl:when>
            <xsl:otherwise><xsl:value-of select="@id" /></xsl:otherwise>
          </xsl:choose>
        </xsl:when>
      </xsl:choose>
      <xsl:text>. oldal</xsl:text>
    </xsl:attribute>

    <a>
      <xsl:attribute name="name">
        <xsl:choose>
          <xsl:when test="@n">
            <xsl:value-of select="@n" />
          </xsl:when>
          <xsl:when test="@id">
            <xsl:value-of select="@id" />
          </xsl:when>
        </xsl:choose>
      </xsl:attribute>
    </a>

    <xsl:text>{</xsl:text>
      <xsl:choose>
        <xsl:when test="@n">
          <xsl:choose>
            <xsl:when test="substring-after(@n,'page')"><xsl:value-of select="substring-after(@n,'page')" /></xsl:when>
            <xsl:otherwise><xsl:value-of select="@n" /></xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:when test="@id">
          <xsl:choose>
            <xsl:when test="substring-after(@id,'page')"><xsl:value-of select="substring-after(@id,'page')" /></xsl:when>
            <xsl:otherwise><xsl:value-of select="@id" /></xsl:otherwise>
          </xsl:choose>
        </xsl:when>
      </xsl:choose>
    <xsl:text>.}</xsl:text>
  </span>
  <!-- szóköz az oldalszám után -->
  <xsl:text> </xsl:text>

</xsl:template>
<!-- pb.END -->


<xsl:template match="p">
  <xsl:choose>
    <xsl:when test="list">
      <xsl:apply-templates select="list[1]" mode="inpara" /> 
    </xsl:when> 
    <xsl:otherwise>
        <p>
          <xsl:choose>
            <xsl:when test="@rend and starts-with(@rend,'class:')">
              <xsl:attribute name="class">
                <xsl:value-of select="substring-after(@rend,'class:')" />
              </xsl:attribute>
            </xsl:when>

            <xsl:when test="@rend">
              <xsl:attribute name="class"><xsl:value-of select="@rend" /></xsl:attribute>
            </xsl:when>

            <xsl:when test="count(child::*) = 1 and child::figure and string-length(text()) = 0">
              <xsl:attribute name="class">figure</xsl:attribute>
            </xsl:when>
          </xsl:choose>

            <xsl:if test="@text-align">
              <xsl:attribute name="style">
                <xsl:text>text-align:</xsl:text>
                <xsl:choose>
                  <xsl:when test="@text-align = 'centered'">
                    <xsl:text>center;</xsl:text>
                    <xsl:text>text-indent:0;</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="@text-align" />
                  </xsl:otherwise>
                </xsl:choose>
                <xsl:text>;</xsl:text>
              </xsl:attribute>
            </xsl:if>

          <xsl:choose>
            <xsl:when test="@id">
              <a name="{@id}" />
            </xsl:when>
            <xsl:when test="$generateParagraphIDs='true'">
              <a name="{generate-id()}" />
            </xsl:when>
          </xsl:choose>

          <xsl:if test="$numberParagraphs='true'">
            <xsl:number/><xsl:text> </xsl:text>
          </xsl:if>

         <xsl:apply-templates/>
       </p>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>
<!-- p.END -->

<xsl:template match="list" mode="inpara">
  <p><xsl:apply-templates select="preceding-sibling::node()" /></p>
  <xsl:apply-templates select="." />
  <p><xsl:apply-templates select="following-sibling::node()" /></p>
</xsl:template>
<!-- list.END -->

<xsl:template match="gi" mode="plain">
  <xsl:text>&lt;</xsl:text>
   <xsl:apply-templates mode="plain"/>
  <xsl:text>&gt;</xsl:text>
</xsl:template>
<!-- gi.END -->

<xsl:template match="hi" mode="plain">
  <!--* xsl:message>[teihtml-struct.xsl:387] match="hi" mode="plain"</xsl:message *-->
  <xsl:apply-templates mode="plain"/>
</xsl:template>

<xsl:template match="lb|pb" mode="plain">
  <xsl:text> </xsl:text>
  <xsl:apply-templates mode="plain"/>
</xsl:template>

<xsl:template match="ref" mode="plain">
</xsl:template>

<xsl:template match="*" mode="plain">
  <!--* xsl:message>[teihtml-struct.xsl:396] inside plain match="*"</xsl:message *-->
  <xsl:apply-templates mode="plain"/>
</xsl:template>

<!-- why do this? it makes embedded elements fall over-->

<!--
<xsl:template match="text()" mode="plain">
  <xsl:value-of select="normalize-space(.)" />
</xsl:template>
-->

<xsl:template name="writeDiv">
  <!--* xsl:message>[teihtml-struct.xsl:409] writeDiv</xsl:message *-->

  <xsl:variable name="BaseFile">
    <xsl:value-of select="$masterFile" />
    <!--* xsl:message>[teihtml-struct.xsl:413] writeDiv->addCorpusID</xsl:message *-->
    <xsl:call-template name="addCorpusID" />
  </xsl:variable>
  <html>
    <xsl:call-template name="addLangAtt" />
    <xsl:comment>[teihtml-struct.xsl:405] THIS IS A GENERATED FILE. DO NOT EDIT</xsl:comment>

    <!--* xsl:message>[teihtml-struct.xsl:407] writeDiv.head</xsl:message *-->
    <head>
      <!--* xsl:message>[teihtml-struct.xsl:409] writeDiv.$pagetitle=<xsl:value-of select="pagetitle" /></xsl:message *-->
      <xsl:variable name="pagetitle">
        <xsl:call-template name="generateDivtitle" />
      </xsl:variable>
      <xsl:message>[teihtml-struct.xsl:413] writeDiv.$pagetitle=<xsl:value-of select="$pagetitle" /></xsl:message>

      <title>
        <xsl:value-of select="concat( $docMainTitle, ' / ', $pagetitle )" />
      </title>
      <xsl:call-template name="headHook" />
      <xsl:call-template name="includeCSS" />
      <xsl:call-template name="includeJS" />
      <xsl:call-template name="metaHook">
        <xsl:with-param name="title" select="$pagetitle" />
      </xsl:call-template>
      <xsl:call-template name="javaScript" />
    </head>

    <!--* xsl:message>[teihtml-struct.xsl:427] writeDiv.body</xsl:message *-->
    <body>
      <xsl:attribute name="bgcolor" ><xsl:value-of select="$preferred_bgcolor" /></xsl:attribute>

      <!--* xsl:message>[teihtml-struct.xsl:431] writeDiv->bodyHook</xsl:message *-->
      <xsl:call-template name="bodyHook" />
      <!--* xsl:message>[teihtml-struct.xsl:433] writeDiv->bodyJavaScript</xsl:message *-->
      <xsl:call-template name="bodyJavaScript" />
      <a name="TOP" />
      <div class="teidiv">

        <xsl:variable name="xrefpanel">
          <xsl:if test="$topNavigationPanel='true' or $bottomNavigationPanel='true'">
            <!--* xsl:message>[teihtml-struct.xsl:440] writeDiv->xrefpanel</xsl:message *-->
            <xsl:call-template name="xrefpanel">
              <xsl:with-param name="homepage" select="concat($BaseFile,$standardSuffix)" />
              <xsl:with-param name="mode" select="name(.)" />
            </xsl:call-template>
          </xsl:if>
        </xsl:variable>
       
        <xsl:if test="$topNavigationPanel='true'">
          <xsl:copy-of select="$xrefpanel" />
          <hr width="50%"/>
        </xsl:if>

        <!--* xsl:comment>teihtml-struct.xsl::writeDiv::call stdheader</xsl:comment *-->
        <!--* xsl:message>[teihtml-struct.xsl:449] writeDiv->stdheader</xsl:message *-->
        
        <h3 class="docMainTitle"><xsl:value-of select="$docMainTitle" /></h3>
        <xsl:message>[teihtml-struct.xsl:454] writeDiv->generateDivheading [H1]</xsl:message>
        <h1><xsl:call-template name="generateDivheading" /></h1>
        <xsl:message>[teihtml-struct.xsl:454] writeDiv->generateDivheading [/H1]</xsl:message>
<!--
        <xsl:call-template name="stdheader">
          <xsl:with-param name="title">
            <xsl:message>[teihtml-struct.xsl:456] p.title - writeDiv->generateDivheading()</xsl:message>
            <xsl:call-template name="generateDivheading" />
          </xsl:with-param>
        </xsl:call-template>
-->
        <!--* xsl:comment>/teihtml-struct.xsl::writeDiv::stdheader</xsl:comment *-->


        <xsl:if test="$subTocDepth &gt;= 0">
          <!--* xsl:message>[teihtml-struct.xsl:465] writeDiv->subtoc</xsl:message *-->
          <xsl:call-template name="subtoc" />
        </xsl:if>

        <!--* xsl:message>[teihtml-struct.xsl:469] writeDiv->startHook</xsl:message *-->
        <xsl:call-template name="startHook" />
        
        <!--* xsl:message>[teihtml-struct.xsl:472] writeDiv->doDivBody</xsl:message *-->
        <xsl:call-template name="doDivBody" />

        <xsl:if test="descendant::note[@place='foot'] and $footnoteFile=''">
          <hr/>
          <p><b>Notes</b></p>
          <!--* xsl:message>[teihtml-struct.xsl:478] writeDiv->printDivnotes</xsl:message *-->
          <xsl:call-template name="printDivnotes" />
        </xsl:if>

        <xsl:if test="$bottomNavigationPanel='true'">
          <hr width="50%"/>
          <xsl:copy-of select="$xrefpanel" />
        </xsl:if>

                    


        <!--
        <xsl:message>[teihtml-struct.xsl:493] writeDiv->stdfooter</xsl:message>
        <xsl:call-template name="stdfooter">
          <xsl:with-param name="date">
            <xsl:message>[teihtml-struct.xsl:496] p.$date: writeDiv->generateDate</xsl:message>
            <xsl:call-template name="generateDate" />
          </xsl:with-param>
          <xsl:with-param name="author">
            <xsl:message>[teihtml-struct.xsl:500] p.$author: writeDiv->generateAuthorList</xsl:message>
            <xsl:call-template name="generateAuthorList" />
          </xsl:with-param>
        </xsl:call-template>
        -->
      </div>
    </body>
  </html>
</xsl:template>


<xsl:template name="subtoc">
  <xsl:message>[teihtml-struct.xsl:512] subtoc</xsl:message>
  <xsl:if test="child::div|div1|div2|div3|div4|div5|div6">
    <xsl:variable name="parent">
      <xsl:choose>
        <xsl:when test="ancestor::div">
          <xsl:apply-templates select="ancestor::div[last()]" mode="ident" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="." mode="ident" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="depth">
      <xsl:apply-templates select="." mode="depth" />
    </xsl:variable>

    <p><span class="subtochead"><xsl:value-of select="$tocWords" /></span></p>
    <div class="subtoc">
      <ul class="subtoc">
        <xsl:for-each select="div|div1|div2|div3|div4|div5|div6">
          <xsl:variable name="innerdent">
            <xsl:apply-templates select="." mode="xrefheader" />
          </xsl:variable>
          <li class="subtoc">
            <xsl:call-template name="makeHyperLink">
              <xsl:with-param name="url">
                <xsl:value-of select="$innerdent" />
              </xsl:with-param>
              <xsl:with-param name="class">
                <xsl:value-of select="$class_subtoc" />
              </xsl:with-param>
              <xsl:with-param name="body">
                <!--* xsl:message>[teihtml-struct.xsl:545] subtoc->header</xsl:message *-->
                <xsl:call-template name="header" mode="maketoc" >
                  <xsl:with-param name="caller">subtoc</xsl:with-param>
                </xsl:call-template>
              </xsl:with-param>
            </xsl:call-template>
          </li>
        </xsl:for-each>
      </ul>
    </div>
  </xsl:if>
</xsl:template> 


<xsl:template name="maintoc"> 
  <xsl:param name="force" />

  <xsl:comment>tocFront</xsl:comment>
  <xsl:if test="$tocFront">
    <xsl:for-each select="ancestor-or-self::TEI.2/text/front">
      <xsl:if test="div|div0|div1|div2|div3|div4|div5|div6">
        <ul class="toc{$force}">
          <xsl:apply-templates select="div|div0|div1|div2|div3|div4|div5|div6" mode="maketoc">
            <xsl:with-param name="forcedepth" select="$force" />
          </xsl:apply-templates>
        </ul>
      </xsl:if>
    </xsl:for-each>
  </xsl:if>

  <xsl:comment>body</xsl:comment>
  <xsl:for-each select="ancestor-or-self::TEI.2/text/body">
    <xsl:if test="div|div0|div1|div2|div3|div4|div5|div6">
      <ul class="toc{$force}">
        <xsl:apply-templates select="div|div0|div1|div2|div3|div4|div5|div6" mode="maketoc">
          <xsl:with-param name="forcedepth" select="$force" />
        </xsl:apply-templates>
      </ul>
    </xsl:if>
  </xsl:for-each>

  <xsl:comment>tocBack</xsl:comment>
  <xsl:if test="$tocBack">
    <xsl:for-each select="ancestor-or-self::TEI.2/text/back">
      <xsl:if test="div|div0|div1|div2|div3|div4|div5|div6">
        <ul class="toc{$force}">
          <xsl:apply-templates select="div|div0|div1|div2|div3|div4|div5|div6" mode="maketoc">
            <xsl:with-param name="forcedepth" select="$force" />
          </xsl:apply-templates>
        </ul>
      </xsl:if>
    </xsl:for-each>
  </xsl:if>
</xsl:template>
<!-- maintoc.END -->


<!-- xref to previous and last sections -->
<xsl:template name="xrefpanel">
  <xsl:param name="homepage" />
  <xsl:param name="mode" />
  <!--* xsl:message>[teihtml-struct.xsl:607] xrefpanel: <xsl:value-of select="$homepage" /></xsl:message *-->
  <xsl:comment><xsl:value-of select="$homepage" /></xsl:comment>

  <p class="navbar">

    <xsl:variable name="Parent">
      <xsl:call-template name="locateParent" />
      <xsl:value-of select="$standardSuffix" />
    </xsl:variable>

      <table class="navbar" width="100%">
       <tr valign="top">
        <td width="33%" align="left">

          <xsl:choose>
            <xsl:when test="ancestor-or-self::TEI.2[@rend='nomenu']">
              <xsl:message>[teihtml-struct.xsl:617] xrefpanel->upLink</xsl:message>
              <xsl:call-template name="upLink">
                <xsl:with-param name="up" select="$homepage" />
                <xsl:with-param name="title" select="$contentsWord" />
                  <!-- xsl:call-template name="contentsWord" />
                </xsl:with-param -->
              </xsl:call-template>
            </xsl:when>
      
            <xsl:when test="$mode = 'div' and preceding-sibling::div">
              <xsl:call-template name="previousLink">
                <xsl:with-param name="previous"  select="preceding-sibling::div[1]" />
              </xsl:call-template>
            </xsl:when>

            <xsl:when test="$mode = 'div' and parent::body/preceding-sibling::front/div">
              <xsl:call-template name="previousLink">
                <xsl:with-param name="previous" select="parent::body/preceding-sibling::front/div[head][last()]" />
              </xsl:call-template>
            </xsl:when>

            <xsl:when test="$mode = 'div' and parent::back/preceding-sibling::body/div">
              <xsl:call-template name="previousLink">
                <xsl:with-param name="previous"  select="parent::back/preceding-sibling::body/div[last()]" />
              </xsl:call-template>
            </xsl:when>

            <xsl:otherwise>
              <!--* xsl:message>[teihtml-struct.xsl:665] xrefpanel->previousLink</xsl:message *-->
              <xsl:call-template name="previousLink">
                <xsl:with-param name="previous" 
                 select="( ancestor::*[ starts-with( name(.), 'div' ) and
                                       substring-after( name(.), 'div' ) &lt;= $splitLevel 
                                     ]/head
                          |
                          preceding::*[ starts-with( name(.), 'div' ) and
                                        substring-after( name(.), 'div' ) &lt;= $splitLevel 
                                      ]/head
                        )[last()]/parent::*" />
              </xsl:call-template>
            </xsl:otherwise>
          </xsl:choose>

        </td>
        <td width="33%" align="center" bgcolor="#eeeedd">

          <!--* xsl:message>[teihtml-struct.xsl:671] p.Parent: <xsl:value-of select="$Parent" /></xsl:message *-->
          <!--* xsl:message>[teihtml-struct.xsl:672] p.standardSuffix: <xsl:value-of select="$standardSuffix" /></xsl:message *-->
          <xsl:choose>
            <xsl:when test="$Parent = $standardSuffix">
              <!--* xsl:message>[teihtml-struct.xsl:706] xrefpanel->upLink</xsl:message *-->
              <xsl:call-template name="upLink">
                <xsl:with-param name="up" select="$homepage" />
                <xsl:with-param name="title" select="$contentsWord" />
                  <!-- xsl:call-template name="contentsWord" />
                </xsl:with-param -->
              </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
              <xsl:message>[teihtml-struct.xsl:684] xrefpanel->generateUpLink</xsl:message>
              <xsl:call-template name="generateUpLink" />
            </xsl:otherwise>
          </xsl:choose>

        </td>
        <td width="33%" align="right">

          <xsl:choose>
            <xsl:when test="ancestor-or-self::TEI.2[@rend='nomenu']">
            </xsl:when>

            <xsl:when test="$mode = 'div'">
              <xsl:call-template name="nextLink">
                <xsl:message>[teihtml-struct.xsl:695] xrefpanel->nextLink</xsl:message>
                <xsl:with-param name="next" select="(following-sibling::div[head][1]|parent::body/following-sibling::back/div[1]|parent::front/following-sibling::body/div[1])[1]" />
              </xsl:call-template>
            </xsl:when>

            <xsl:otherwise>
                  <!--* xsl:message>[teihtml-struct.xsl:720] xrefpanel->nextLink</xsl:message *-->
                  <xsl:call-template name="nextLink">
                    <xsl:with-param name="next" 
                      select="( descendant::*[ starts-with( name(.), 'div' ) and
                                   substring-after( name(.), 'div' ) &lt;= $splitLevel 
                                   ]/head
                                 |
                                 following::*[ starts-with( name(.), 'div' ) and
                                    substring-after( name(.), 'div' ) &lt;= $splitLevel 
                                    ]/head
                               )[1]/parent::*" />
                  </xsl:call-template>

            </xsl:otherwise>
          </xsl:choose>

        </td>
      </tr>
    </table>
  </p>
</xsl:template>


<xsl:template name="upLink">
  <!--* xsl:message>[teihtml-struct.xsl:733] upLink</xsl:message *-->
  <xsl:param name="up" />
  <xsl:param name="title" />
  <!--* xsl:message>[teihtml-struct.xsl:736] p.up <xsl:value-of select="$up" /></xsl:message *-->
  <!--* xsl:message>[teihtml-struct.xsl:737] p.title <xsl:value-of select="$title" /></xsl:message *-->

  <xsl:if test="$up and not($makeFrames='true')">
    <!-- i>
      <xsl:text> </xsl:text>
      <xsl:value-of select="$upWord" />
      <xsl:text>: </xsl:text>
    </i -->

    <a class="navlink"> 
      <xsl:choose>
        <xsl:when test="$title">
          <xsl:attribute name="href">
            <xsl:value-of select="$linkPrefix" />
            <xsl:value-of select="$up" />
          </xsl:attribute>
          <xsl:value-of select="$title" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="href">
            <xsl:value-of select="$linkPrefix" />
            <xsl:apply-templates mode="xrefheader" select="$up" />
          </xsl:attribute>
          <xsl:for-each select="$up">
            <xsl:call-template name="header">
              <xsl:with-param name="minimal" select="$minimalCrossRef" />
            </xsl:call-template>
          </xsl:for-each>
        </xsl:otherwise>
      </xsl:choose>
    </a>
  </xsl:if>
</xsl:template>

<xsl:template name="previousLink">
  <xsl:param name="previous" />

  <xsl:if test="$previous">

    <!-- i>
      <xsl:text> </xsl:text>
      <xsl:value-of select="$previousWord" />
      <xsl:text>: </xsl:text>
    </i --> 

    <a class="navlink">
      <xsl:attribute name="href">
        <xsl:value-of select="$linkPrefix" />
        <xsl:apply-templates mode="xrefheader" select="$previous" />
      </xsl:attribute>

      <xsl:for-each select="$previous">
        <xsl:call-template name="header">
          <xsl:with-param name="minimal" select="$minimalCrossRef" />
          <!-- xsl:with-param name="toc"></xsl:with-param -->
          <xsl:with-param name="caller">navlink</xsl:with-param>
        </xsl:call-template>
      </xsl:for-each>
    </a>
  </xsl:if>
</xsl:template>

<xsl:template name="nextLink">
  <xsl:param name="next" />
  <!--* xsl:message>[teihtml-struct.xsl:834] nextLink</xsl:message *-->
  <!--* xsl:message>[teihtml-struct.xsl:835] p.next <xsl:value-of select="$next/head" /></xsl:message *-->

  <xsl:if test="$next">
    <!-- i>
      <xsl:text> </xsl:text>
      <xsl:value-of select="$nextWord" />
      <xsl:text>: </xsl:text>
    </i -->

    <a class="navlink">
      <xsl:attribute name="href">
        <xsl:value-of select="$linkPrefix" />
        <xsl:apply-templates mode="xrefheader" select="$next" />
      </xsl:attribute>

      <xsl:for-each select="$next">
        <!--* xsl:message>[teihtml-struct.xsl:802] nextLink->header</xsl:message *-->
        <xsl:call-template name="header">
          <xsl:with-param name="minimal" select="$minimalCrossRef" />
          <!-- xsl:with-param name="toc"></xsl:with-param -->
          <xsl:with-param name="caller">navlink</xsl:with-param>
        </xsl:call-template>
      </xsl:for-each>
    </a>
  </xsl:if>
</xsl:template> 

<xsl:template name="generateDivtitle">
  <xsl:message>[teihtml-struct.xsl:777] generateDivtitle head:<xsl:value-of select="head" /></xsl:message>
  <xsl:value-of select="head" />
  <!-- xsl:apply-templates select="head" / -->
</xsl:template> 

<xsl:template name="generateDivheading">
  <!--* xsl:message>[teihtml-struct.xsl:858] generateDivheading :: calling "." mode="header"</xsl:message *-->
  <!-- xsl:message>generateDivheading <xsl:value-of select="name(.)" /></xsl:message -->
  <xsl:call-template name="header" >
    <xsl:with-param name="caller">generateDivheading</xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template name="startHook" />

<xsl:template name="doDivBody">
  <!--* xsl:message>[teihtml-struct.xsl:868] doDivBody</xsl:message *-->
  <xsl:param name="Type" />

  <xsl:call-template name="startDivHook" />

  <xsl:variable name="ident">
    <xsl:apply-templates select="." mode="ident" />
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="$leftLinks and $Type =''">
      <xsl:message>[teihtml-struct.xsl:864] doDivBody test="$leftLinks and $Type =''"</xsl:message>
      <xsl:call-template name="linkList">
        <xsl:with-param name="side" select="'left'" />
       </xsl:call-template>
    </xsl:when>

    <xsl:when test="$rightLinks and $Type  =''">
      <xsl:message>[teihtml-struct.xsl:871] doDivBody test="$rightLinks and $Type =''"</xsl:message>
      <xsl:call-template name="linkList">
        <xsl:with-param name="side" select="'right'" />
      </xsl:call-template>
    </xsl:when>

    <xsl:when test="parent::div/@rend='multicol'">
      <xsl:message>[teihtml-struct.xsl:878] doDivBody test="parent::div/@rend='multicol'"</xsl:message>
      <td valign="top">
        <xsl:if test="not($Type = '')">
          <xsl:message terminate="no">[teihtml-struct.xsl:869] h{$Type + $divOffset}</xsl:message>
          <xsl:element name="h{$Type + $divOffset}">
            <a name="{$ident}" />
            <xsl:call-template name="header" />
          </xsl:element>
        </xsl:if>
        <xsl:apply-templates/>
      </td>
    </xsl:when>

    <xsl:when test="@rend='multicol'">
      <xsl:message>[teihtml-struct.xsl:892] doDivBody test="@rend='multicol'"</xsl:message>
      <xsl:apply-templates select="*[not(name(.)='div')]" />
      <table>
        <tr>
          <xsl:apply-templates select="div" />
        </tr>
      </table>
    </xsl:when>

    <xsl:otherwise>
      <!--* xsl:message>[teihtml-struct.xsl:956] doDivBody otherwise</xsl:message *-->
      <xsl:if test="not($Type = '')">
        <xsl:element name="h{$Type + $divOffset}">
          <a name="{$ident}"></a><xsl:call-template name="header" />
        </xsl:element>
      </xsl:if>
      <xsl:apply-templates/>
    </xsl:otherwise>
  </xsl:choose>
  <!--* xsl:message>[teihtml-struct.xsl:912] doDivBody END</xsl:message *-->
</xsl:template>


<xsl:template name="generateUpLink">
  <xsl:message>[teihtml-struct.xsl:917] generateUpLink</xsl:message>

  <xsl:variable name="BaseFile">
    <xsl:value-of select="$masterFile" />
    <xsl:message>[teihtml-struct.xsl:921] generateUpLink->addCorpusID</xsl:message>
    <xsl:call-template name="addCorpusID" />
  </xsl:variable>
<xsl:choose>
 <xsl:when test="name(.) = 'div'">
  <xsl:message>[teihtml-struct.xsl:902] generateUpLink->upLink</xsl:message>
  <xsl:call-template name="upLink">
    <xsl:with-param name="up" select="ancestor::div[last()]" />
  </xsl:call-template>
 </xsl:when>
 <xsl:otherwise>
  <xsl:choose>
    <xsl:when test="name(.)='div0'">
      <xsl:message>[teihtml-struct.xsl:910] generateUpLink->upLink</xsl:message>
      <xsl:call-template name="upLink">
        <xsl:with-param name="up" select="concat($BaseFile,$standardSuffix)" />
        <xsl:with-param name="title" select="$homeLabel" />
      </xsl:call-template>
    </xsl:when>
    <xsl:when test="name(.)='div1'">
      <xsl:message>[teihtml-struct.xsl:917] generateUpLink->upLink</xsl:message>
      <xsl:call-template name="upLink">
        <xsl:with-param name="up" select="concat($BaseFile,$standardSuffix)" />
        <xsl:with-param name="title" select="$homeLabel" />
      </xsl:call-template>
    </xsl:when>
    <xsl:when test="name(.)='div2'">
      <xsl:message>[teihtml-struct.xsl:924] generateUpLink->upLink</xsl:message>
      <xsl:call-template name="upLink">
       <xsl:with-param name="up" select="ancestor::div1" />
      </xsl:call-template>
    </xsl:when>
    <xsl:when test="name(.)='div3'">
      <xsl:message>[teihtml-struct.xsl:930] generateUpLink->upLink</xsl:message>
     <xsl:call-template name="upLink">
      <xsl:with-param name="up" select="ancestor::div2" />
     </xsl:call-template>
    </xsl:when>
    <xsl:when test="name(.)='div4'">
      <xsl:message>[teihtml-struct.xsl:936] generateUpLink->upLink</xsl:message>
      <xsl:call-template name="upLink">
       <xsl:with-param name="up" select="ancestor::div3" />
      </xsl:call-template>
    </xsl:when>
    <xsl:when test="name(.)='div5'">
     <xsl:message>[teihtml-struct.xsl:942] generateUpLink->upLink</xsl:message>
     <xsl:call-template name="upLink">
      <xsl:with-param name="up" select="ancestor::div4" />
     </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
     <xsl:message>[teihtml-struct.xsl:948] generateUpLink->upLink</xsl:message>
     <xsl:call-template name="upLink">
      <xsl:with-param name="up" select="(ancestor::div1|ancestor::div)[1]" />
     </xsl:call-template>
    </xsl:otherwise>
  </xsl:choose>
 </xsl:otherwise>
</xsl:choose>
</xsl:template>


<xsl:template name="addLangAtt">
  <xsl:variable name="supplied">
    <xsl:value-of select="ancestor-or-self::*[@lang][1]/@lang" />
  </xsl:variable>
  <xsl:attribute name="lang">
    <xsl:choose>
      <xsl:when test="$supplied">
        <xsl:text>en</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$supplied" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:attribute>
</xsl:template>

<xsl:template name="addCorpusID">
  <!--* xsl:message>[teihtml-struct.xsl:906] addCorpusID</xsl:message *-->

  <xsl:if test="ancestor-or-self::teiCorpus.2">
    <xsl:for-each select="ancestor-or-self::TEI.2">
      <xsl:text>-</xsl:text>
      <xsl:choose>
        <xsl:when test="@id"><xsl:value-of select="@id" /></xsl:when> 
        <xsl:otherwise><xsl:number/></xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:if>

</xsl:template>


</xsl:stylesheet>
