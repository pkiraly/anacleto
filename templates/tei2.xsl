<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:in="http://www.tesujionline.com/default-tei2-indexsheet" >
<xsl:strip-space elements="*" />
        <!-- Keyword is a searchable word in the text -->
 <!--       <xsl:attribute-set name = "keyword" > 
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
-->
        <!-- 
<xsl:template match="/" >
  <xsl:apply-templates/>
</xsl:template>
        -->

<xsl:template match="text" >
  <xsl:message>front</xsl:message>
  <xsl:apply-templates select="front" />

  <xsl:message>body</xsl:message>
  <xsl:apply-templates select="body"  />

  <xsl:message>back</xsl:message>
  <xsl:apply-templates select="back"  />
</xsl:template>
        
<xsl:template match="teiHeader" >

  <xsl:element name="in:index">        
    <xsl:attribute name="name" >
    </xsl:attribute>
  
    <xsl:attribute name="title">
      <xsl:call-template name="getTitle" />
    </xsl:attribute>
    
    <xsl:attribute name="author">
      <xsl:call-template name="getAuthor" />
    </xsl:attribute>
  
    <xsl:attribute name="editor">
      <xsl:value-of select="fileDesc/sourceDesc/bibl/editor" />
    </xsl:attribute>
  
    <xsl:attribute name="publisher">
      <xsl:value-of select="fileDesc/sourceDesc/bibl/publisher" />
    </xsl:attribute>
  
    <xsl:attribute name="pubPlace">
       <xsl:value-of select="fileDesc/sourceDesc/bibl/pubPlace" />
    </xsl:attribute>
  
    <xsl:attribute name="pubDate">
      <xsl:value-of select="fileDesc/sourceDesc/bibl/date" />
    </xsl:attribute>         
             
    <xsl:attribute name="location">
      <xsl:for-each select="ancestor-or-self::*">
        <xsl:value-of select="concat( '/', name() )" />
        <xsl:choose>
          <xsl:when test="(count( following-sibling::*[name()=name(current())] ) + count( preceding-sibling::*[name()=name(current())] ) ) = 0 ">
            <xsl:value-of select="''" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="concat( '[', count( preceding-sibling::*[name()=name(current())] ) + 1, ']' )" />
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </xsl:attribute>
    <xsl:apply-templates />
  </xsl:element>
  <xsl:comment><xsl:text>* /</xsl:text><xsl:value-of select="name()" /><xsl:text> *</xsl:text></xsl:comment>
  <xsl:text>&#xa;</xsl:text>
</xsl:template>

<xsl:template match="front | back" >
  <xsl:element name="in:index" >
    
    <xsl:attribute name="name">
      <xsl:value-of select="generate-id()" />
    </xsl:attribute>
    
    <xsl:attribute name="bookTitle">
      <xsl:call-template name="getTitle" />
    </xsl:attribute>

    <xsl:attribute name="bookAuthor">
      <xsl:call-template name="getAuthor" />
    </xsl:attribute>

    <xsl:attribute name="title">
      <xsl:choose>
        <!--* van head *-->
        <xsl:when test="head">
          <xsl:value-of select="head" />
        </xsl:when>

        <!--* nincs head, a div type-ja epistola, es van opener/dateline *-->
        <xsl:when test="@type = 'epistola' and opener/dateline">
          <xsl:value-of select="opener/dateline" />
        </xsl:when>

        <!--* nincs head, a div type-ja epistola, es van opener/salute *-->
        <xsl:when test="@type = 'epistola' and opener/salute">
          <xsl:value-of select="opener/salute" />
        </xsl:when>

        <!--* nincs head, a div type-ja epistola, es van opener/salute *-->
        <xsl:when test="parent::*[name() = 'front'] and opener/salute">
          <xsl:value-of select="opener/salute" />
        </xsl:when>

        <xsl:when test="descendant::*/text()">
          <xsl:value-of select="descendant::*/text()" />
        </xsl:when>

        <!--* egyeb esetben megjegyzes a logba, illetve 'w/o title' szoveg *-->
        <xsl:otherwise>
          <xsl:message>nem kezelt title elem <xsl:value-of select="concat( '$prefix', generate-id(.) )"/></xsl:message>
          <xsl:value-of select="'w/o title'" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>

    <xsl:variable name="location">
      <xsl:for-each select="ancestor-or-self::*">
        <xsl:value-of select="concat( '/', name() )" />
        <xsl:choose>
          <xsl:when test="(count( following-sibling::*[name()=name(current())] ) + count( preceding-sibling::*[name()=name(current())] ) ) = 0 ">
            <xsl:value-of select="''" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="concat( '[', count( preceding-sibling::*[name()=name(current())] ) + 1, ']' )" />
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </xsl:variable>

    <xsl:attribute name="location">
      <xsl:value-of select="$location" />
    </xsl:attribute>
            
    <xsl:variable name="parentName">
      <xsl:choose>
        <xsl:when test="name( parent::* ) = 'body' ">
          <xsl:value-of select="''" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="generate-id( parent::* )" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:attribute name="parentName">
      <xsl:value-of select="$parentName" />
    </xsl:attribute>
                    
    <xsl:attribute name="childcount">
      <xsl:value-of select="count( preceding-sibling::*[name()=name(current())] )" />        
    </xsl:attribute>

    <xsl:apply-templates mode="front">
      <xsl:with-param name="location" select="$location" />
      <xsl:with-param name="parentName" select="$parentName" />
    </xsl:apply-templates>

  </xsl:element>
  <xsl:text>&#xa;</xsl:text>

  <xsl:comment><xsl:text>* /</xsl:text><xsl:value-of select="name()" /><xsl:text> *</xsl:text></xsl:comment>
  <xsl:text>&#xa;</xsl:text>

</xsl:template>


<xsl:template match="div0 | div1 | div2 " >
  
  <xsl:text>&#xa;</xsl:text>
  <xsl:element name="in:index" >
    
    <xsl:attribute name="name">
      <xsl:value-of select="generate-id()" />
    </xsl:attribute>
    
    <xsl:attribute name="bookTitle">
      <xsl:call-template name="getTitle" />
    </xsl:attribute>

    <xsl:attribute name="bookAuthor">
      <xsl:call-template name="getAuthor" />
    </xsl:attribute>

    <xsl:attribute name="title">
      <xsl:choose>
        <!--* van head *-->
        <xsl:when test="head">
          <xsl:value-of select="head" />
        </xsl:when>

        <!--* nincs head, a div type-ja epistola, es van opener/dateline *-->
        <xsl:when test="@type = 'epistola' and opener/dateline">
          <xsl:value-of select="opener/dateline" />
        </xsl:when>

        <!--* nincs head, a div type-ja epistola, es van opener/salute *-->
        <xsl:when test="@type = 'epistola' and opener/salute">
          <xsl:value-of select="opener/salute" />
        </xsl:when>

        <!--* nincs head, a div type-ja epistola, es van opener/salute *-->
        <xsl:when test="parent::*[name() = 'front'] and opener/salute">
          <xsl:value-of select="opener/salute" />
        </xsl:when>

        <!--* egyeb esetben megjegyzes a logba, illetve 'w/o title' szoveg *-->
        <xsl:otherwise>
          <xsl:message>nem kezelt title elem <xsl:value-of select="generate-id(.)"/></xsl:message>
          <xsl:value-of select="'w/o title'" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>

    <xsl:variable name="location">
      <xsl:for-each select="ancestor-or-self::*">
        <xsl:value-of select="concat( '/', name() )" />
        <xsl:choose>
          <xsl:when test="(count( following-sibling::*[name()=name(current())] ) + count( preceding-sibling::*[name()=name(current())] ) ) = 0 ">
            <xsl:value-of select="''" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="concat( '[', count( preceding-sibling::*[name()=name(current())] ) + 1, ']' )" />
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </xsl:variable>

    <xsl:attribute name="location">
      <xsl:value-of select="$location" />
    </xsl:attribute>
            
    <xsl:variable name="parentName">
      <xsl:choose>
        <xsl:when test="name( parent::* ) = 'body' ">
          <xsl:value-of select="''" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="generate-id( parent::* )" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:attribute name="parentName">
      <xsl:value-of select="$parentName" />
    </xsl:attribute>
                    
    <xsl:attribute name="childcount">
      <xsl:value-of select="count( preceding-sibling::*[name()=name(current())] )" />        
    </xsl:attribute>

    <!--* pelda az 'osszegyujtott' mezore
     | <xsl:attribute name="speaker">
     |   <xsl:for-each select="descendant::*[name() = 'speaker']">
     |     <xsl:if test="position() &gt; 1"><xsl:value-of select="', '" /></xsl:if>
     |     <xsl:value-of select="." />
     |   </xsl:for-each>
     | </xsl:attribute>
     *-->
    
    <xsl:apply-templates>
      <xsl:with-param name="location" select="$location" />
      <xsl:with-param name="parentName" select="$parentName" />
    </xsl:apply-templates>

  </xsl:element>
  <xsl:text>&#xa;</xsl:text>

  <xsl:comment><xsl:text>* /</xsl:text><xsl:value-of select="name()" /><xsl:text> *</xsl:text></xsl:comment>
  <xsl:text>&#xa;</xsl:text>

</xsl:template>

<!-- tovabbi nem onallo oldalon szereplo mezok
 | <xsl:template match="sp | speaker | stage">
 |  <xsl:param name="location" />
 |  <xsl:param name="parentName" />
 |
 |  <xsl:text>&#xa;</xsl:text>
 |  <xsl:element name="in:index">
 |    <xsl:attribute name="location">
 |      <xsl:value-of select="$location" />
 |    </xsl:attribute>
 |
 |    <xsl:attribute name="parentName">
 |      <xsl:value-of select="$parentName" />
 |    </xsl:attribute>
 |    
 |    <xsl:attribute name="field">
 |      <xsl:value-of select="name()" />
 |    </xsl:attribute>
 |
 |    <xsl:apply-templates>
 |      <xsl:with-param name="location" select="$location" />
 |      <xsl:with-param name="parentName" select="$parentName" />
 |    </xsl:apply-templates>
 |
 |  </xsl:element>
 |  <xsl:text>&#xa;</xsl:text>
 |  <xsl:comment>
 |    <xsl:text>* /</xsl:text>
 |    <xsl:value-of select="name()" />
 |    <xsl:text> *</xsl:text>
 |  </xsl:comment>
 |  <xsl:text>&#xa;</xsl:text>
 | </xsl:template>
 *-->


<xsl:template name="getTitle">
  <xsl:value-of select="/TEI.2/teiHeader/fileDesc/titleStmt/title" />
</xsl:template>

<xsl:template name="getAuthor">
  <xsl:value-of select="/TEI.2/teiHeader/fileDesc/titleStmt/author" />
</xsl:template>


</xsl:stylesheet>