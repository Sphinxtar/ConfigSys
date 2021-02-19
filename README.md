<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<LINK REL="stylesheet" HREF="configdoc.css" TYPE="text/css" MEDIA="screen" />
<LINK REL="SHORTCUT ICON" HREF="/configsys/favicon.ico" />
<title>ConfigSys Documentation</title>
</head>
<body>
<div class="main-title">
ConfigSys
</div>
<br/>
<div class="main-byline"> Host and Application Configuration Management using XML and XSLT</div>
<div class="main-author">by Linus Crawford</div>
<p/>

<div class="section-title">
Chapter 1: The Madness
</div>
<br/>
<div class="section-text">
Say you have a lot of servers, big cloud, Hadoop, cluster forming a busy production web site. Odds are pretty good almost all of them fall into one of several categories such as Web, DBMS, Application, Datanodes/Task Trackers and it's also very likely all of those are running the same software components with only a few words and values scattered across each in a dozen or more places, all in differently formatted files with no coordination in between other than a careful System Administrator edited them and that's about all that actually separate one host from another.<br/>
You hatch another server whether virtual or real and then log in and edit all the files with a text editor to suit it's purpose every time you need one and if a server dies you start from scratch. Mistakes are made, maybe you skip a step or an entire file or just do it a bit differently and then start the debugging and head scratching. Once inconsistencies are introduced the smooth upgrade path and migration forward just became a Herculean task of compound frustration and manual labor instead of the automated sheer bliss it should be.<br/>
This document introduces the idea of centralizing all the information that separates them into a central XML file store and then by applying XSLT templates to create configuration files and even scraps of script to configure hosts in a reliable, repeatable fashion and aid in automating server build, software installation, deployments, maintenance and even moving builds from zone to zone with predictable results and lower the cost and possible errors of human intervention.
</div>
<br/>
<div class="section-title">
Chapter 2: The Method
</div>
<br/>
<div class="section-text">
Take all the minute details and errata that separates one host from another and put them all in one central place on your network. For this purpose we'll use XML since it has a variety of editors, becomes almost self documenting with good descriptive tag names and does not require a schema making the design even more flexible. Then use a template, for our purpose we'll use XSLT since it's robust, well documented and provides not only interpolation and text substitution but logic, math and more to make this even more flexible than it has to be. For this example let's pretend we are building portal servers that need a database connection, a directory server connection and a web server configuration all unique to each, we would start by creating a doc with all these unique bits of information for one host and it would look something like this:
<PRE><p style="font-size: 10pt;">&lt;?xml version="1.0"?&gt;
&lt;node&gt;
&lt;dbms-dbname&gt;<b>lportal3</b>&lt;/dbms-dbname&gt;
&lt;dbms-host&gt;<b>dbmsbox3</b>&lt;/dbms-host&gt;
&lt;dbms-jndi&gt;<b>jdbc/LiferayPool</b>&lt;/dbms-jndi&gt;
&lt;dbms-port&gt;<b>3306</b>&lt;/dbms-port&gt;
&lt;dbms-pword&gt;<b>fishm0nger</b>&lt;/dbms-pword&gt;
&lt;dbms-user&gt;<b>dbmsguy</b>&lt;/dbms-user&gt;
&lt;httpd-group&gt;<b>apache</b>&lt;/httpd-group&gt;
&lt;httpd-name&gt;<b>appserver3</b>&lt;/httpd-name&gt;
&lt;httpd-port&gt;<b>80</b>&lt;/httpd-port&gt;
&lt;httpd-ssl-key-name&gt;<b>lportal</b>&lt;/httpd-ssl-key-name&gt;
&lt;httpd-user&gt;<b>apache</b>&lt;/httpd-user&gt;
&lt;ldap-cn&gt;<b>cn=Directory Manager</b>&lt;/ldap-cn&gt;
&lt;ldap-dn&gt;<b>dc=fullsack,dc=com</b>&lt;/ldap-dn&gt;
&lt;ldap-host&gt;<b>dir.fullsack.com</b>&lt;/ldap-host&gt;
&lt;ldap-port&gt;<b>389</b>&lt;/ldap-port&gt;
&lt;ldap-pword&gt;<b>fl0un9er</b>&lt;/ldap-pword&gt;
&lt;/node&gt;</p></PRE>
<div class="section-text">
I use, &quot;node&quot; for the root document tags, use whatever suits the device as long as it matches your stylesheet templates. The names of tags are what is meaningful for you to use and only need to match the tags used in your stylesheets, keep in mind they will be sorted by tag name and use it wisely. Save your XML data files to /usr/local/Templates/Type/Host or where it matches the templates directory value in the configsys/WEB-INF/web.xml file and the device name as in this example, a Host. ConfigSys will work on multiple object types, just add a, &quot;type&quot;, named directory in templates/Type and fill it with XML files and it will show up as an additional line on the main menu. Use the pattern &lt;the device's hostname&gt;.xml for the XML files names so it will be easy to apply your configurations in scripts later. Now copy that files contents to one named NEW.xml in the same directory then edit and remove any values truly unique and leave a set of defaults, ConfigSys will use this to create new configurations quickly through a web browser form, edit NEW and give it a new file name.
The multiple device named directories can be used to sort and group your devices into more manageable sets.
This document will not teach you beyond the basic in and out of XSL but will only show you only most basic form of the transformation, how to protect the text you want passed through and how to interpolate it with the text in between the tags of your XML document.
First create a stylesheet in /usr/local/templates or where you have the configsys/WEB-INF/web.xml pointing to. First develop a working configuration file we need, copy it to /usr/local/templates/orig for reference. Our example will be a simple ssl.conf to point to the right license file for this host and looks like:
<PRE><p style="font-size: 10pt;">LoadModule ssl_module modules/mod_ssl.so
Listen 443
AddType application/x-x509-ca-cert .crt
AddType application/x-pkcs7-crl    .crl
SSLPassPhraseDialog  builtin
SSLSessionCache        dc:UNIX:/var/cache/mod_ssl/distcache
SSLSessionCacheTimeout  300
SSLMutex default
SSLRandomSeed startup file:/dev/random  512
SSLRandomSeed connect file:/dev/random  512
SSLCryptoDevice ubsec
&lt;VirtualHost _default_:443&gt;
ErrorLog logs/ssl_error_log
TransferLog logs/ssl_access_log
LogLevel warn
SSLEngine on
SSLProtocol all -SSLv2
SSLCipherSuite ALL:!ADH:!EXPORT:!SSLv2:RC4+RSA:+HIGH:+MEDIUM:+LOW
SSLCertificateFile <b>/etc/pki/tls/certs/localhost</b>.crt
SSLCertificateKeyFile <b>/etc/pki/tls/private/localhost</b>.key
&lt;Files ~ "\.(cgi|shtml|phtml|php3?)$"&gt;
    SSLOptions +StdEnvVars
&lt;/Files&gt;
&lt;Directory "/var/www/cgi-bin"&gt;
    SSLOptions +StdEnvVars
&lt;/Directory&gt;
SetEnvIf User-Agent ".*MSIE.*" \
         nokeepalive ssl-unclean-shutdown \
         downgrade-1.0 force-response-1.0
CustomLog logs/ssl_request_log \
          "%t %h %{SSL_PROTOCOL}x %{SSL_CIPHER}x \"%r\" %b"
&lt;/VirtualHost&gt;
</p></PRE>
Some careful analysis reveals the only thing we need to change per host is the name and location of the keys in bold above. We store our keys in /etc/httpd/certs and named for the host. But first we must mark up the areas we want to preserve 
as character data by bracketing inside a tag of, &quot;&lt;![CDATA[&quot;, and, &quot;]]&gt;. Next replace the text we want to substitute with the XSL tag to select the value in the xml tag of the same name outside of the CDATA tag. We'll need to describe it as an xml file, and instruct the processor to output it as UTF-8 text with no &lt;?xml version?&gt; declarations. Template inside our stylesheet matches the document root tag and reads in the values we'll use and select on the field we need with a little boiler plate text added like so:
<PRE><p style="font-size: 10pt;"><b>&lt;?xml version="1.0"?&gt;
&lt;xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"&gt; 
&lt;xsl:output method="text" version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/&gt;
&lt;xsl:template match="node"&gt;&lt;![CDATA[</b>#
LoadModule ssl_module modules/mod_ssl.so
Listen 443
AddType application/x-x509-ca-cert .crt
AddType application/x-pkcs7-crl .crl
&lt;Files ~ "\.(cgi|shtml|phtml|php3?)$"&gt;
SSLOptions +StdEnvVars
&lt;/Files>
&lt;Directory "/var/www/cgi-bin"&gt;
SSLOptions +StdEnvVars
&lt;/Directory&gt;
NameVirtualHost *:443
&lt;VirtualHost *:443&gt;
SSLEngine on<b>]]&gt;</b>
SSLCertificateFile <i>/etc/httpd/certs/</i><b>&lt;xsl:value-of select="httpd-ssl-key-name"/&gt;</b><i>.crt</i>
SSLCertificateKeyFile <i>/etc/httpd/certs/</i><b>&lt;xsl:value-of select="httpd-ssl-key-name"/&gt;</b><i>.key</i>
<b>&lt;![CDATA[</b>ErrorLog logs/ssl_error_log 
TransferLog logs/ssl_access_log 
LogLevel warn
&lt;/VirtualHost&gt;
<b>]]&gt;</b>
&lt;/xsl:template&gt; 
&lt;/xsl:stylesheet&gt;</p></PRE>
<div class="section-title">
Chapter 3: The Installation And Usage
</div>
<div class="section-text">
Unzip the application configsys-#.#.zip into the webapps directory of your Java application server. If running ControlTier this might be ControlTier-3.6.0/pkgs/jetty-6.1.21/webapps/. 
Unzip the configsys-templates.zip into /usr/local. Chown -R both webapps/configsys and /usr/local/templates to your installation user.
Once unzipped restart your application service and configsys should be found at http://ControlTierServerName:8080/configsys. 
Edit the file webapps/configsys/WEB-INF/web.xml and change the URL's and directories in context parameters to match your installation.
Click edit and you should see your XML files, new and first host listed. Clicking on them loads them into a web form to edit and save, NEW will need to be named before saving. If you use an existing file's name it will be overwritten. You can open a file and then change the name field to make a copy.
Click view and you should see your XSL templates on the left and your XML files on the right. Select two and submit to see the transformation of them. This is the raw output of the apply servlet and how you will make use of your configuration data. As your install user on your target host you can 
use the following syntax of curl or wget to fetch, transform and write to disk with a command pattern of, <i>&quot;apply stylesheet document&quot;</i> as in:
<PRE><p style="font-size: 10pt;">
sudo curl -s "http://ControlTierServer:8080/configsys/apply?style=ssl-conf&amp;doc=`hostname`&amp;type=Host&amp;" -o /etc/httpd/conf.d/ssl.conf
or
sudo wget "http://ControlTierServer:8080/configsys/apply?style=ssl-conf&doc=`hostname`&type=Host&" &lt; /etc/httpd/conf.d/ssl.conf
</PRE>
or you can still do this without java or an application server by making a transformation locally with a perl script like so:
<PRE><p style="font-size: 10pt;">#!/usr/bin/perl
#   usage: 
#          apply.pl stylesheet xmlfile
# example:
#         ./apply.pl ssl.xsl Type/Host/mrsblapp00399.xml
#
# import required modules
use XML::DOM;
use XML::XSLT;

# define local variables
my $xslfile = $ARGV[0];
my $xmlfile = $ARGV[1];
print "applying $xslfile to $xmlfile", "\n";
# create an instance of XSL::XSLT processor
my $xslt = eval { XML::XSLT-&gt;new ($xslfile, warnings =&gt; 1, debug =&gt; 0) };

# some error handling here ...
if ($@) {
 die("Sorry, Could not create an instance of the XSL Processor using $xslfile.\n");
}

# transforms the XML file using the XSL style sheet
eval { $xslt-&gt;transform ($xmlfile) };

# ... and here
if ($@) {
 die("Sorry, Could not transform XML file, $xmlfile.\n");
}

# send to output
print $xslt-&gt;toString;

# free up some memory
$xslt-&gt;dispose(); 
</PRE>
In one intstance I used CGI programming to fill out a form and generate the XML document then apply the transformation to generate configurations with a local web server running as the install user. Uses are mostly limited by you for instance instead of configuration files it could be generating Makefiles or any thing you need reliable results on. I hope this has helped you in your mission and XML/XSLT will continue to be another usefull tool in your Systems Engineering toolbox.
</div>
<br/>
<p style="font-size: 11pt;"><b> H8 linus_AT_fullsack.com for this, last update 2/1/11.</b></p>
</body>
</html>
