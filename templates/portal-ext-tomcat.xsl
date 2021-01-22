<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>

<xsl:template match="node"><![CDATA[schema.run.enabled=true
schema.run.minimal=false

openoffice.server.enabled=true
openoffice.server.host=127.0.0.1
openoffice.server.port=8100
openoffice.cache.enabled=true

jdbc.default.driverClassName=com.mysql.jdbc.Driver
jdbc.default.url=jdbc:mysql://]]><xsl:value-of select="cmsdb-host"/>/<xsl:value-of select="cmsdb-dbname"/><![CDATA[?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false]]>
jdbc.default.username=<xsl:value-of select="cmsdb-user"/>
jdbc.default.password=<xsl:value-of select="cmsdb-pword"/>

jdbc.default.jndi.name=<xsl:value-of select="cmsdb-jndi"/>

ldap.password.policy.enabled=false
ldap.base.provider.url=ldap://<xsl:value-of select="ldap-host"/>:<xsl:value-of select="ldap-port"/>
ldap.base.dn=<xsl:value-of select="ldap-dn"/>
ldap.security.principal=<xsl:value-of select="ldap-cn"/>
ldap.security.credentials=<xsl:value-of select="ldap-pword"/>
ldap.referral=follow
ldap.auth.enabled=true
ldap.auth.required=false
ldap.user.default.object.classes=top,Person,inetOrgPerson,organizationalPerson
ldap.user.mappings=screenName=cn\npassword=userPassword\nemailAddress=mail\nfirstName=givenName\nlastName=sn\njobTitle=title
ldap.group.mappings=groupName=cn\ndescription=description\nuser=uniqueMember
ldap.import.enabled=true
ldap.import.on.startup=true
ldap.import.interval=10
ldap.import.user.search.filter=(objectClass=inetOrgPerson)
ldap.import.group.search.filter=(objectClass=groupOfUniqueNames)
ldap.import.method=user
ldap.export.enabled=true
ldap.users.dn=ou=People,<xsl:value-of select="ldap-dn"/>
ldap.groups.dn=ou=Groups,<xsl:value-of select="ldap-dn"/>

mail.session.mail.transport.protocol=smtp
mail.session.mail.smtp.auth=false
mail.session.mail.smtp.host=localhost
mail.session.mail.smtp.password=
mail.session.mail.smtp.port=25
mail.session.mail.smtp.user=
</xsl:template> 

</xsl:stylesheet>

