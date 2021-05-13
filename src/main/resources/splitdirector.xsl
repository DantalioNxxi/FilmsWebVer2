<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : splitdirector.xsl
    Created on : 12 апреля 2018 г., 23:39
    Author     : Пользователь
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" exclude-result-prefixes="xsl">

    <xsl:template name="token">
     <xsl:param name="token"/>
     <li>
      <xsl:value-of select="$token"/>
     </li>
    </xsl:template>

    <xsl:template name="split" match="text()" mode="split">
    <xsl:param name="str" select="."/>
    <xsl:param name="worddiv" select="','"/>

    <xsl:choose>

        <xsl:when test="contains($str,$worddiv)">
         <xsl:call-template name="token">
          <xsl:with-param name="token" select="substring-before($str, $worddiv)"/>
         </xsl:call-template>
         <xsl:call-template name="split"> 
          <xsl:with-param name="str" select="substring-after($str, $worddiv)"/>
          <xsl:with-param name="worddiv" select="$worddiv"/>
         </xsl:call-template>
        </xsl:when>

        <xsl:otherwise>
         <xsl:call-template name="token">
          <xsl:with-param name="token" select="$str"/>
         </xsl:call-template>
        </xsl:otherwise>

       </xsl:choose>
    
    </xsl:template>
    
</xsl:stylesheet>
