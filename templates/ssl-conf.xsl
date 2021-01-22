<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>

<xsl:template match="node"><![CDATA[#
LoadModule ssl_module modules/mod_ssl.so
Listen 443
AddType application/x-x509-ca-cert .crt
AddType application/x-pkcs7-crl .crl

<Files ~ "\.(cgi|shtml|phtml|php3?)$">
SSLOptions +StdEnvVars
</Files>

<Directory "/var/www/cgi-bin">
SSLOptions +StdEnvVars
</Directory>

NameVirtualHost *:443
<VirtualHost *:443>
SSLEngine on]]>
SSLCertificateFile /etc/httpd/certs/<xsl:value-of select="httpd-ssl-host"/>.crt
SSLCertificateKeyFile /etc/httpd/certs/<xsl:value-of select="httpd-ssl-host"/>.key
<![CDATA[ErrorLog logs/ssl_error_log 
TransferLog logs/ssl_access_log 
LogLevel warn
</VirtualHost>
]]>
</xsl:template> 

</xsl:stylesheet>

