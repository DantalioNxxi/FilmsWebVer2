<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!--<xsl:import href="/splitdirector.xsl"/>-->
    
<xsl:template match="/">
<movies>
    
    <xsl:for-each select="//div[@class='lister-item-content' and count(p/span[@class='runtime']) &gt; 0]">
        
        <movies>
            <title><xsl:value-of select="normalize-space(h3/a[1])"/></title>

            <description>
                <xsl:value-of select="normalize-space(p[@class='text-muted' and position() = 2])"/>
            </description>

            <fulldate><xsl:value-of select="normalize-space(h3/span[2])"/></fulldate>
            
            <!--<genres>-->
                <!--duration and genres instead to span 'ghost'-->
            <!--</genres>-->
            
<!--            <duration>
                <xsl:value-of select="normalize-space(p[@class='text-muted ' and position() = 1]/span[1])"/>
            </duration>-->
            
            <!--<directors>-->
                <fullname><xsl:value-of select="normalize-space(p[@class and position() = 3]/a[1])"/></fullname>
                <!--<lastname></lastname>-->
            <!--</directors>-->

            <actors>
            <xsl:for-each select="p[@class='']/span[@class='ghost']/following-sibling::a">
                <actors>
                    <xsl:value-of select="normalize-space(.)"/>
                </actors>
            </xsl:for-each>
            </actors>
            
<!--            <actors2>
                <xsl:value-of select="normalize-space(p[@class and position() = 3]/a[3])"/>
            </actors2>-->

        </movies>
        
    </xsl:for-each>
    
</movies>
</xsl:template>
    
<!--    <xsl:template name="splityear">
    </xsl:template>-->
    
</xsl:stylesheet>