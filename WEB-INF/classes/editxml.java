import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.channels.ClosedChannelException;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import org.apache.xalan.xslt.*;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
//--

/*
 * Edit XML - load an XML file into a form and write it back out
 */
public class editxml extends HttpServlet
{ 
	public static final long serialVersionUID = 1L;
	public String Title = "ConfigSys Editor"; // title of html pages
	public String Yoorl = "/configsys/editxml"; // full URL to this for /posting subsequent passes
	public String Action; // action of editor - edit, view, del
	// -- these four / delimited make up the full path to working doc
	public String Docroot; // location of doc root in initArgs from zone.properties
	public String Home; // home page for back link
	public String Typedir; // Type directory
	public String Typename; // directory added to Docroot where Docname is
	public String Docname; // full path/name of xml file to work on is Docroot/Typedir/Typename/Docname
/*
 * Collect tbe necessaries
 */
	void getReqParams( HttpServletRequest req ) 
	{
		ServletContext context = this.getServletContext();
		Docroot = context.getInitParameter( "docroot" );
		Home = context.getInitParameter( "home" );
		Typedir = context.getInitParameter( "typedir" );
		Typename = req.getParameter( "type" );
		Docname = req.getParameter( "doc" );
		Action = req.getParameter( "act" );
	}
/**
 * create the top of a form
 */
	void topOfForm( PrintWriter Out )
	{
		Out.println("<html><head><title>" + Title + "</title>");
		Out.println("<script>");
		Out.println("function trim(stringToTrim)\n{\n return stringToTrim.replace(/^\\s+|\\s+$/g,\"\");\n}");
		Out.println("function valid_required(field)\n{\n if(trim(field)==\"\")\n{\n  return false;\n}\n return true;\n}");
		Out.println("function valForm()\n{\nif(!valid_required(document.xform.fname.value))\n{\nalert(\"Filename is a required field!\");\nreturn false;\n}\nreturn true;\n}");
		Out.println("</script>");
		Out.println("</head>");
		Out.println("<body bgcolor=\"#FFFFFF\"><center><p/>");
		Out.println("<form name=\"xform\" method=\"Post\" action=\"" + Yoorl + "\" onSubmit=\"return valForm()\">");
		Out.println("<table border=\"0\" cellspacing=\"2\" cellpadding=\"2\">");
	} // eo topOfForm


/*
 *  end of a form
 */
	void endOfForm( PrintWriter Out )
	{
		Out.println( "</table></form><a href=\"" + Home + "\">Home</a>" );
		Out.println( "</center>" );
		Out.println( "</body></html>" );
	} // eo endOfForm


/*
 * whack a file
 */
	void delete( )
	{
		String fullname = new String( Docroot + File.separator  + Typedir + File.separator + Typename + File.separator + Docname + ".xml" );
		try 
		{
			File target = new File( fullname );

			if ( target.exists() ) 
			{
				target.delete();
			}
		} 
		catch ( SecurityException e )
		{
			;
		}
	}


/*
 * view form
 */
	void viewForm( PrintWriter Out )
	{
		File xmlPath = new File( Docroot + '/' + Typedir + '/' + Typename );
		ExtensionFilter xf = new ExtensionFilter( "xml" );
		String[] xmlDocs = xmlPath.list( xf );
		File xslPath = new File( Docroot );
		ExtensionFilter xs = new ExtensionFilter( "xsl" );
		String[] xslDocs = xslPath.list( xs );

		Out.println("<html><head><title>" + Title + "</title>");
		Out.println("</head>");
		Out.println("<body bgcolor=\"#FFFFFF\"><center><p/>");
		Out.println("<form name=\"vform\" method=\"Post\" action=\"apply\">");
		Out.println("<table border=\"0\">");
		Out.println("<tr><td colspan=\"2\">Select a combination to view:</td></tr>");
		Out.println("<tr><th>Template (xslt)</th><th>" + Typename + " (xml)</th></tr>");
		Out.println("<tr><td><select name=\"style\" size=\"24\">");
		for ( int i = 0; i < xslDocs.length; i++ )
		{
			Out.print("<option>" + xslDocs[ i ].substring( 0, xslDocs[ i ].length() - 4 ) + "</option>");
		}
		Out.println( "</select></td><td><select name=\"doc\" size=\"24\">");
		for ( int i = 0; i < xmlDocs.length; i++ )
		{
			if ( xmlDocs[ i ].equals( "NEW.xml" ))
			{
				continue;
			}
			else
			{
				Out.print("<option>" + xmlDocs[ i ].substring( 0, xmlDocs[ i ].length() - 4 ) + "</option>");
			}
		}
		Out.print("</select></td></tr>");

		setHiddenValues( Action, Out );
		Out.println("<tr><td colspan=\"2\"><table border=\"1\" width=\"100%\"><tr><th align-\"center\"><input type=\"submit\" value=\"" + Action + "\" /></th></tr></table></td></tr>"); 
		endOfForm( Out );
	} // eo viewForm


/*
 *  doc param is empty so present a form to fill it
 */
	void selectDoc( PrintWriter Out )
	{
		File docPath = new File( Docroot + '/' + Typedir + '/' + Typename );
		ExtensionFilter xf = new ExtensionFilter( "xml" );
		String[] xmlDocs = docPath.list( xf );

		topOfForm( Out );
		Out.println("<tr><td align=\"left\">");
		Out.println( "Select " + Typename + ":</td></tr>" );
		Out.println( "<tr><td align=\"left\"><select name=\"doc\" size=\"24\">" );

		Out.print( "<option>NEW</option>" );
		for ( int i = 0; i < xmlDocs.length; i++ )
		{
			if ( xmlDocs[ i ].equals( "NEW.xml" ))
			{
				continue;
			}
			else
			{
				Out.print("<option>" + xmlDocs[ i ].substring( 0, xmlDocs[ i ].length() - 4 ) + "</option>");
			}
		}

		Out.println( "</select></td></tr>" );
		Out.println("<tr><td><table border=\"1\" width=\"100%\"><tr><th align-\"center\"><input type=\"submit\" value=\"" + Action + "\" /></th></tr></table></td></tr>"); 
		setHiddenValues( Action, Out );
		endOfForm( Out );
	} // eo selectDoc


/*
 *  delete a doc form
 */
	void delForm( PrintWriter Out )
	{
		File docPath = new File( Docroot + '/' + Typedir + '/' + Typename );
		ExtensionFilter xf = new ExtensionFilter( "xml" );
		String[] xmlDocs = docPath.list( xf );
		Action = new String("Delete");
		Out.println("<html><head><title>" + Title + "</title>");
		Out.println("<script>");
		Out.println("function valForm()\n{\n if(confirm(\"Are you sure? This file will be permanently deleted.\"))\n {\n  return true;\n }\n else\n {\n  return false;\n }\n }\n");
		Out.println("</script>");
		Out.println("</head>");
		Out.println("<body bgcolor=\"#FFFFFF\"><center><p/>");
		Out.println("<form name=\"xform\" method=\"Post\" action=\"" + Yoorl + "\" onSubmit=\"return valForm()\">");

		Out.println("<table border=\"0\" cellspacing=\"2\" cellpadding=\"2\">");
		Out.println("<tr><td align=\"left\">");
		Out.println( "Select " + Typename + ":</td></tr>" );
		Out.println( "<tr><td align=\"left\"><select name=\"doc\" size=\"24\">" );

		for ( int i = 0; i < xmlDocs.length; i++ )
		{
			if ( xmlDocs[ i ].equals( "NEW.xml" ))
			{
				continue;
			}
			else
			{
				Out.print("<option>" + xmlDocs[ i ].substring( 0, xmlDocs[ i ].length() - 4 ) + "</option>");
			}
		}

		Out.println( "</select></td></tr>" );
		Out.println("<tr><td><table border=\"1\" width=\"100%\"><tr><th align-\"center\"><input type=\"submit\" value=\"" + Action + "\" /></th></tr></table></td></tr>"); 
		setHiddenValues( Action, Out );
		endOfForm( Out );
	}


/*
 * setHiddenValues - adds hidden fields to form
 */
	void setHiddenValues( String Act, PrintWriter Out )
	{
		if ( Typename != null )
		{
			Out.println("<tr><td><input type=\"hidden\" name=\"type\" value=\"" + Typename + "\" /></td></tr>");
		}
		if ( Docname != null )
		{
			Out.println("<tr><td><input type=\"hidden\" name=\"doc\" value=\"" + Docname + "\" /></td></tr>");
		}
		if ( Action != null )
		{
			Out.println("<tr><td><input type=\"hidden\" name=\"act\" value=\"" + Act + "\" /></td></tr>");
		}
	} // eo setHiddenValues


/*
 * walk nodes and build form
 */
    public void editForm( org.w3c.dom.NodeList nodeListi, PrintWriter Out )
                throws java.io.IOException,
                       java.net.MalformedURLException,
                       org.xml.sax.SAXException
    {
        int type;
        int x;
        String name;
        String val;
        org.w3c.dom.Node nodei;
        org.w3c.dom.Node nodej;
		
		if ( Docname.equals( "NEW" ))
		{
			Docname = new String( "" );
		}

		topOfForm( Out );
        for (int i=0; i < nodeListi.getLength(); i++ ) // for every xitem we are looking for
        {
            nodei = nodeListi.item(i);
            type = nodei.getNodeType();
            name = nodei.getNodeName();
			if ( i == 0 ) 
			{
				Out.println("<tr><td align=\"right\">Filename</td><td><input type=\"text\" name=\"fname\" value=\"" + Docname + "\"></th></tr>");
				Out.println("<tr><th align=\"center\">Variables</th><th><input type=\"hidden\" name=\"hidetop\" value=\"" + name + "\"></th></tr>");
			}
			else
			{
				org.w3c.dom.Node kid = nodei.getFirstChild();
				if ( kid != null )
				{
					Out.println("<tr><td align=\"right\">" + name + "</td><td><input type=\"text\" size=\"78\" name=\"" + name + "\" value=\"" + kid.getNodeValue() + "\"></td></tr>");
				}
				else
				{
					Out.println("<tr><td align=\"right\">" + name + "</td><td><input type=\"text\" size=\"78\" name=\"" + name + "\" value=\"\"></td></tr>");
				}
			}
		}
		Out.println("<tr><td colspan=\"2\" align=\"center\"><table border=\"1\" width=\"50%\"><tr><th align-\"center\"><input type=\"submit\" value=\"Save\" /></th></tr></table></td></tr>"); 
		setHiddenValues( "Save", Out );
		endOfForm( Out );
	} // eo editForm	


/*
 * Check for empty params and build/display the appropriate form
 */
	void buildForm( PrintWriter Out )
	{
		Document doc;

		if ( Docname == null ) 
		{
			selectDoc( Out );
			return;
		}
		else
		{
			try 
			{
				String pat = new String( "*" );
				DOMParser parser = new DOMParser();
				parser.parse(Docroot + '/' + Typedir + '/' + Typename + '/' + Docname + ".xml" );
				doc = parser.getDocument();
				doc.getDocumentElement().normalize();
				org.w3c.dom.NodeList nodeList = doc.getElementsByTagName( pat );
				editForm( nodeList, Out );
			} 
			catch (SAXParseException spe) 
			{
				Out.println("\n--Parse error" + ", line " + spe.getLineNumber () + ", uri " + spe.getSystemId ());
				Out.println("   " + spe.getMessage() );
				Exception  x = spe;
				if (spe.getException() != null)
					 x = spe.getException();
				x.printStackTrace();
				return;
			} 
			catch (SAXException sxe) 
			{
				Exception  x = sxe;
				if (sxe.getException() != null)
					x = sxe.getException();
				x.printStackTrace();
				return;
			} 
			catch (IOException ioe) 
			{
			   ioe.printStackTrace();
			   return;
			}
		}
	} // eo buildForm


/*
 * Setup our basic page
 */
	public void buildPage (HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		String toptag[] = null;
		String nametag[] = null;
		ArrayList<String> tags = new ArrayList<String>();
		PrintWriter Out = response.getWriter();
		getReqParams( request ); 
		response.setContentType("text/html");
		if ( Action.equals( "Save" ))
		{
			for ( Enumeration<String> tagz = request.getParameterNames(); tagz.hasMoreElements() ;)
			{
				String tag = tagz.nextElement();

				if ( tag.equals( "hidetop" ))
				{
					toptag = request.getParameterValues(tag);
				}
				else if ( tag.equals( "fname" ))
				{
					nametag = request.getParameterValues(tag);
				}
				else if ( tag.equals( "doc" ))
				{
					continue;
				}
				else if ( tag.equals( "act" ))
				{
					continue;
				}
				else if ( tag.equals( "type" ))
				{
					continue;
				}
				else
				{
					tags.add( tag );
				}
			}
			Out.println( "<html><head><title>ConfigSys Saved</title></head><body><PRE>");
			Collections.sort( tags );
			String fullname = new String( Docroot + File.separator  + Typedir + File.separator + Typename + File.separator + nametag[0] + ".xml" );
			File file = new File(fullname);
			FileChannel channel = new RandomAccessFile( file, "rw" ).getChannel();
			FileLock lock;
			try
			{
				lock = channel.tryLock();
				Out.println( "Successfully locked " + fullname + "<br/>");
			}
			catch( OverlappingFileLockException e )
			{ 
				Out.println( "Failed to lock " + fullname + "<br/>");
				Out.println( "</PRE></body></html>" );
				return;
			}
			if ( lock == null )
			{
				Out.println( "Failed locking " + fullname + "<br/>");
				channel.close();
				Out.println( "</PRE></body></html>" );
				return; 
			}
			OutputStream os = Channels.newOutputStream( channel );
			BufferedWriter fileOut = new BufferedWriter( new OutputStreamWriter( os ));
			Out.println( "&lt;" + toptag[0] + "&gt;" );
			fileOut.write( "<?xml version=\"1.0\"?>\n" );
			fileOut.write( "<" + toptag[0] + ">\n" );

			for ( Iterator<String> iter = tags.iterator(); iter.hasNext();  ) 
			{
				String tagname = new String( iter.next() );
				Out.print( "&lt;" + tagname + "&gt;" );
				fileOut.write( "<" + tagname + ">" );
				String val[] = request.getParameterValues(tagname);
				for ( int i = 0; i < val.length; i++ )
				{
					Out.print( val[ i ] );
					fileOut.write( val[ i ] );
				}
				Out.print( "&lt;/" + tagname + "&gt;\n" );
				fileOut.write( "</" + tagname + ">\n" );
			}
			Out.println( "&lt;/" + toptag[0] + "&gt;" );
			fileOut.write( "</" + toptag[0] + ">\n" );

			Out.println( "\nSaved.\n</PRE>" );
			Out.println( "<a href=\"" + Home + "\">Home</a></body></html>" );

			fileOut.flush();
			fileOut.close();
			try
			{
				lock.release();
				channel.close();
			}
			catch( ClosedChannelException e )
			{
			}
		}
		else if( Action.equals( "View" ))
		{
			viewForm( Out );
		}
		else if ( Action.equals( "Delete" ))
		{
			delete();
			delForm( Out );
		}
		else if ( Action.equals( "kill" ))
		{
			delForm( Out );
		}
		else
		{
			buildForm( Out );
		}
		Out.close();
	} // eo buildPage


/*
 * answer Get and build a page
 */
	public void doGet (HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		buildPage( request, response );
	} // eo doGet


/*
 * answer Post and build a page
 */
	public void doPost (HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		buildPage( request, response );
	} // eo doPost

} // eoclass
