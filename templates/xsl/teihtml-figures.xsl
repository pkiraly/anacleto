<!-- 
Text Encoding Initiative Consortium XSLT stylesheet family
$Date$, $Revision$, $Author$

XSL stylesheet to format TEI XML documents to HTML or XSL FO

 
Copyright 1999-2003 Sebastian Rahtz / Text Encoding Initiative Consortium
                          
    This is an XSLT stylesheet for transforming TEI (version P4) XML documents

    Version 3.1. Date Mon Feb 16 20:22:47 GMT 2004

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

<xsl:variable name="img_width" select="'150'" />
<xsl:variable name="img_height" select="'150'" />

<xsl:template match="figure">

  <xsl:variable name="File">
    <xsl:choose> 

      <xsl:when test="@file">
        <xsl:value-of select="@file"/>
        <xsl:if test="not(contains(@file,'.'))">
          <xsl:value-of select="$graphicsSuffix"/>
        </xsl:if>
      </xsl:when>

      <xsl:when test="@figure-id"> <!-- eredetileg @file -->
        <xsl:value-of select="@figure-id" />
        <xsl:if test="not(contains(@figure-id,'.'))">
          <xsl:value-of select="$graphicsSuffix"/>
        </xsl:if>
      </xsl:when>

      <xsl:when test="@url">
        <xsl:value-of select="@url"/>
        <xsl:if test="not(contains(@url,'.'))">
          <xsl:value-of select="$graphicsSuffix"/>
        </xsl:if>
      </xsl:when>

      <xsl:otherwise>
        <xsl:variable name="entity">
          <xsl:value-of select="unparsed-entity-uri(@entity)"/>
        </xsl:variable>
 
        <xsl:choose>
          <xsl:when test="starts-with($entity,'file:')">
            <xsl:value-of select="substring-after($entity,'file:')"/>
          </xsl:when>

          <xsl:otherwise>
            <xsl:value-of select="$entity"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:otherwise>

    </xsl:choose>
  </xsl:variable>

  <xsl:if test="@id">
    <a name="{@id}" />
  </xsl:if>

  <xsl:choose>
    <xsl:when test="$showFigures='true' and ( @figure-id or @file or @url )">
      <!-- table background="black" -->
      <!-- span style="background-color: gray; padding: 5" -->

        <xsl:if test="@id">
          <xsl:attribute name="name"><xsl:value-of select="@id"/></xsl:attribute>
        </xsl:if>

        <xsl:if test="@rend and string-length(@rend) &gt; 0">
          <xsl:attribute name="class"><xsl:value-of select="@rend"/></xsl:attribute>
        </xsl:if>

        <xsl:choose>
          <xsl:when test="@width and @width &gt; $img_width and @height &gt; 30">
            <a>
            <xsl:attribute name="href">
               <xsl:if test="not($outputDir='')">
				     <xsl:text>../</xsl:text>
				   </xsl:if>
               <xsl:value-of select="concat( $graphicsPrefix, $File )" />
             </xsl:attribute>

             <img>

               <xsl:attribute name="src">
                 <xsl:if test="not($outputDir='')">
                   <xsl:text>../</xsl:text>
                 </xsl:if>
                 <xsl:value-of select="$graphicsPrefix" />
                 <xsl:text>thumb/</xsl:text>
                 <xsl:value-of select="$File" />
               </xsl:attribute>

               <xsl:choose>
                 <xsl:when test="@rend and @rend='inline'">
                   <xsl:attribute name="border">0</xsl:attribute>
                 </xsl:when>
                 <xsl:otherwise>
                   <xsl:attribute name="border">1</xsl:attribute>
                 </xsl:otherwise>
               </xsl:choose>

               <xsl:if test="@width &gt; 0 and not( contains( @width, 'in' ) )">
                 <xsl:attribute name="width"><xsl:value-of select="$img_width" /></xsl:attribute>  
               </xsl:if>

               <xsl:if test="@height &gt; 0 and not( contains( @height, 'in' ) )">
                 <xsl:attribute name="height">
                   <xsl:value-of select="floor( ( @height * $img_height ) div @width)"/>
                 </xsl:attribute>
               </xsl:if>

               <xsl:attribute name="alt">
                 <xsl:choose>
                   <xsl:when test="figDesc">
                     <xsl:value-of select="figDesc//text()"/>
                   </xsl:when>

                  <xsl:otherwise>
                    <xsl:value-of select="head/title/text()"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:attribute>

              <xsl:attribute name="title">
                <xsl:choose>
                  <xsl:when test="figDesc">
                    <xsl:value-of select="figDesc//text()"/>
                  </xsl:when>

                  <xsl:otherwise>
                    <xsl:value-of select="head/title/text()"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:attribute>

              <xsl:attribute name="align">absmiddle</xsl:attribute>
              <xsl:attribute name="border">0</xsl:attribute>

              <xsl:call-template name="imgHook"/>
            </img>
          </a>
        </xsl:when>
        <xsl:otherwise>
          <img>

            <xsl:attribute name="src">
              <xsl:if test="not($outputDir='')">
                <xsl:text>../</xsl:text>
              </xsl:if>
              <xsl:value-of select="concat( $graphicsPrefix, $File )" />
            </xsl:attribute>

               <xsl:choose>
                 <xsl:when test="@rend and @rend='inline'">
                   <xsl:attribute name="border">0</xsl:attribute>
                 </xsl:when>
                 <xsl:otherwise>
                   <xsl:attribute name="border">1</xsl:attribute>
                 </xsl:otherwise>
               </xsl:choose>

            <xsl:if test="@width &gt; 0 and not( contains( @width, 'in' ) )">
              <xsl:attribute name="width"><xsl:value-of select="@width"/></xsl:attribute>
            </xsl:if>
          
            <xsl:if test="@height &gt; 0 and not( contains( @height, 'in' ) )">
              <xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute>
            </xsl:if>

            <xsl:attribute name="alt">
              <xsl:choose>
                <xsl:when test="figDesc">
                  <xsl:value-of select="figDesc//text()"/>
                </xsl:when>

                <xsl:otherwise>
                  <xsl:value-of select="head/title/text()"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>

            <xsl:attribute name="title">
              <xsl:choose>
                <xsl:when test="figDesc">
                  <xsl:value-of select="figDesc//text()"/>
                </xsl:when>

                <xsl:otherwise>
                  <xsl:value-of select="head/title/text()"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            
            <xsl:attribute name="align">absmiddle</xsl:attribute>
            <xsl:attribute name="border">0</xsl:attribute>


            <xsl:call-template name="imgHook"/>
          </img>
        </xsl:otherwise>
      </xsl:choose>

      <!-- /span -->
    </xsl:when>

    <!-- xsl:otherwise>
      <hr/>
      <p>
        Figure <xsl:number level="any"/>
        file <xsl:value-of select="$File"/>
        <xsl:if test="figDesc">
          [<xsl:apply-templates select="figDesc//text()"/>]
        </xsl:if>
      </p>
      <hr/>
    </xsl:otherwise -->
  </xsl:choose>

  <xsl:if test="head">
    <xsl:choose>
      <xsl:when test="@figure-id or @file or @url">
        <div class="kepala">
          <xsl:if test="$numberFigures='true'" >
             Figure <xsl:number level="any"/>.<xsl:text> </xsl:text>
          </xsl:if>
          <xsl:apply-templates select="head"/>
        </div>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>&#xD;&#xA;</xsl:text>
        <div class="kepala">
          <xsl:apply-templates select="head"/>
        </div>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:if>
</xsl:template>

<xsl:template name="imgHook" />

<xsl:template match="figDesc" />

</xsl:stylesheet>
