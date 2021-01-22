<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>

<xsl:template match="node"><![CDATA[mysql ]]><xsl:value-of select='cmsdb-dbname'/><![CDATA[ -h ]]><xsl:value-of select="cmsdb-host"/><![CDATA[ -u ]]><xsl:value-of select="cmsdb-user"/><![CDATA[ -p]]><xsl:value-of select="cmsdb-pword"/></xsl:template>
</xsl:stylesheet>

