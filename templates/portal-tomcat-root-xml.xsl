<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>

<xsl:template match="node"><![CDATA[<Context path="" crossContext="true">
<!-- JAAS 
<Realm
className="org.apache.catalina.realm.JAASRealm"
appName="PortalRealm"
userClassNames="com.liferay.portal.security.jaas.PortalPrincipal"
roleClassNames="com.liferay.portal.security.jaas.PortalRole"
debug="99"
useContextClassLoader="false"
/>
-->
<!-- Uncomment the following to disable persistent sessions across reboots.  -->
<!--<Manager pathname="" />-->

<!-- Uncomment the following to not use sessions. See the property "session.disabled" in portal.properties.  -->
<!--<Manager className="com.liferay.support.tomcat.session.SessionLessManagerBase" />-->

<Resource
name="jdbc/LiferayPool"
auth="Container"
type="javax.sql.DataSource"
driverClassName="com.mysql.jdbc.Driver"
url="jdbc:mysql://]]><xsl:value-of select="cmsdb-host"/>:<xsl:value-of select="cmsdb-port"/>/<xsl:value-of select="cmsdb-dbname"/><![CDATA[?useUnicode=true&amp;characterEncoding=UTF-8&amp;"
username="]]><xsl:value-of select="cmsdb-user"/><![CDATA["
password="]]><xsl:value-of select="cmsdb-pword"/><![CDATA["
maxActive="100"
maxIdle="30"
maxWait="10000"
/>

<Resource
name="mail/MailSession"
auth="Container"
type="javax.mail.Session"
mail.transport.protocol="smtp"
mail.smtp.host="localhost"
/>

</Context>
]]>

</xsl:template> 

</xsl:stylesheet>

