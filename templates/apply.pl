#!/usr/bin/perl

# import required modules
use XML::DOM;
use XML::XSLT;

# define local variables
my $xmlfile = $ARGV[0];
my $xslfile = $ARGV[1];

# create an instance of XSL::XSLT processor
my $xslt = XML::XSLT->new($xslfile);

# transform XML file 
$xslt->transform($xmlfile);

# send to output
my $str = $xslt->toString;
print $str;

# free up some memory
$xslt->dispose(); 

