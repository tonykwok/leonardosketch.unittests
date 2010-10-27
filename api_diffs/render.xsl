<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="changes">
        <html>
        <head><title>API Changes</title></head>
        <body>
        
        <h1>API Changes</h1>
        <ul>
        <xsl:apply-templates/>
        </ul>
        </body>
        </html>        
    </xsl:template>
    
    
    <xsl:template match="classes/add">
        <li><b>+</b> Added class <i><xsl:value-of select="@qualifiedName"/></i></li>
    </xsl:template>
    
    <xsl:template match="classes/remove">
        <li><b>-</b> Removed class <i><xsl:value-of select="@qualifiedName"/></i></li>
    </xsl:template>
    
    <xsl:template match="methods/add">
        <li><b>+</b> Added method <i><xsl:value-of select="@name"/></i> to class <i><xsl:value-of select="@class"/></i></li>
    </xsl:template>
    
    <xsl:template match="methods/remove">
        <li><b>+</b> Removed method <i><xsl:value-of select="@name"/></i> from class <i><xsl:value-of select="@class"/></i></li>
    </xsl:template>
    
</xsl:stylesheet>
