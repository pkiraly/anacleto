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
  
<xsl:import href="teicommon.xsl"/>
<xsl:import href="teihtml-param.xsl"/>

<xsl:include href="teihtml-bibl.xsl"/>
<xsl:include href="teihtml-chunk.xsl"/>
<xsl:include href="teihtml-corpus.xsl"/>
<xsl:include href="teihtml-drama.xsl"/>
<xsl:include href="teihtml-figures.xsl"/>
<xsl:include href="teihtml-frames.xsl"/>
<xsl:include href="teihtml-front.xsl"/>
<xsl:include href="teihtml-lists.xsl"/>
<xsl:include href="teihtml-main.xsl"/>
<xsl:include href="teihtml-math.xsl"/>
<xsl:include href="teihtml-misc.xsl"/>
<xsl:include href="teihtml-notes.xsl"/>
<xsl:include href="teihtml-pagetable.xsl"/>
<xsl:include href="teihtml-poetry.xsl"/>
<xsl:include href="teihtml-struct.xsl"/>
<xsl:include href="teihtml-tables.xsl"/>
<xsl:include href="teihtml-xref.xsl"/>

</xsl:stylesheet>
