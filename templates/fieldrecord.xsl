<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>

<xsl:template name="id">
<xsl:text>'id' = '</xsl:text><xsl:value-of select="/fieldreport/id"/><xsl:text>', </xsl:text>
</xsl:template>

<xsl:template match="/fieldreport/report">
<xsl:text>REPLACE INTO ISSUES SET </xsl:text><xsl:call-template name="id"/>
<xsl:for-each select="*">
<xsl:text>'</xsl:text><xsl:value-of select="local-name()"/>
<xsl:text>' = '</xsl:text><xsl:value-of select="."/>
<xsl:if test="position() = last()">
<xsl:text>';</xsl:text>
</xsl:if>
<xsl:if test="position() != last()">
<xsl:text>', </xsl:text>
</xsl:if>
</xsl:for-each>
</xsl:template>

<xsl:template match="/fieldreport/review"><xsl:text>REPLACE INTO REVIEWS SET </xsl:text><xsl:call-template name="id"/> 
<xsl:for-each select="*">
<xsl:text>'</xsl:text><xsl:value-of select="local-name()"/>
<xsl:text>' = '</xsl:text><xsl:value-of select="."/>
<xsl:if test="position() = last()">
<xsl:text>';</xsl:text>
</xsl:if>
<xsl:if test="position() != last()">
<xsl:text>', </xsl:text>
</xsl:if>
</xsl:for-each>
</xsl:template>

</xsl:stylesheet>
