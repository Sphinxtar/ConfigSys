#!/usr/bin/python
try:
  from lxml import etree
  print("running with lxml.etree")
except ImportError:
  print("import lxml.etree failed")
data = open('d1.xsl')
xslt_content = data.read()
xslt_root = etree.XML(xslt_content)
dom = etree.parse('d1.xml')
transform = etree.XSLT(xslt_root)
result = transform(dom)
print(str(result))
