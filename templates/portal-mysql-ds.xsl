<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>
<xsl:template match="node"><![CDATA[<?xml version="1.0" encoding="UTF-8"?>
<datasources>
  <local-tx-datasource>
    <jndi-name>]]><xsl:value-of select="cmsdb-jndi"/><![CDATA[</jndi-name>
    <connection-url>jdbc:mysql://]]><xsl:value-of select="cmsdb-host"/>:3306/<xsl:value-of select="cmsdb-dbname"/><![CDATA[?useUnicode=true&amp;characterEncoding=UTF-8&amp;useFastDateParsing=false</connection-url>
    <driver-class>com.mysql.jdbc.Driver</driver-class>
    <user-name>]]><xsl:value-of select="cmsdb-user"/><![CDATA[</user-name>
    <password>]]><xsl:value-of select="cmsdb-pword"/><![CDATA[</password>
    <min-pool-size>0</min-pool-size>
  </local-tx-datasource>
</datasources>
]]>
</xsl:template> 
</xsl:stylesheet>
