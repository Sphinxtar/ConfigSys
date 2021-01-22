import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
//--
// import ExtensionFilter;

/*
 * ConfigSys - Configuration System
 *		 by Linus Sphinx 
 *  (c)Copyright Linus Sphinx 2010
 */
public class configsys extends HttpServlet
{ 
	public static final long serialVersionUID = 1L;
	public String Docroot; // location of doc root in initArgs from zone.properties
	public String Typedir; // directory added to XDocRoot where XDocName is
	public String Title = "ConfigSys"; // title of html pages
	// html chunks
	String topofpage = new String("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<link rel=\"stylesheet\" href=\"/configsys/configsys.css\"/>\n <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>ConfigSys</title><LINK REL=\"SHORTCUT ICON\" HREF=\"/configsys/favicon.ico\"/></head><body>\n" );
	String logo = new String( "<div class=\"top\"><table border=\"0\" cellspacing=\"2\" cellpadding=\"1\" width=\"400px\"><tr><th class=\"logoth\" align=\"center\" valign=\"bottom\" rowspan=\"3\">ConfigSys</th><tr><td class=\"logotd\" align=\"center\" valign=\"bottom\">configuration system</td></tr><tr><td align=\"center\" class=\"logotd\" valign=\"top\">SphinxWare Version 1.0</td></tr></table></div>\n" );
	String topoftable = new String( "<div class=\"middle\"><table id=\"menu\"><tr><th align=\"center\">%s</th><th colspan=\"3\" align=\"center\">Actions</th></tr>\n" );
	String tableitem = new String( "<tr><td align=\"center\" style=\"font-weight: bold\";>%s</td><td align=\"center\"><a href=\"/configsys/editxml?type=%s&amp;act=Edit&amp;\">Edit</a></td><td align=\"center\"><a href=\"/configsys/editxml?type=%s&amp;act=View&amp;\">View</a></td><td align=\"center\"><a href=\"/configsys/editxml?type=%s&amp;act=kill&amp;\">Delete</a></td></tr>\n" );
	String footer = new String( "</table></div></body></html>\n" );

/**
 * build page
 */
	public void buildPage (HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		Docroot = getServletContext().getInitParameter("docroot");
		Typedir = getServletContext().getInitParameter("typedir");
		File docsPath = new File( Docroot + '/' + Typedir );
		// String[] docDirs = docsPath.list();
		File[] docDirs = docsPath.listFiles();
		PrintWriter Out = response.getWriter();

		response.setContentType("text/html");
		Out.println( topofpage ); 
		Out.println( logo ); 
		Out.printf( topoftable, Typedir ); 
		for( int i=0; i < docDirs.length; i++ )
		{
			if ( docDirs[ i ].isDirectory() )
			{
				String fname = docDirs[ i ].getName();
				File F = new File( Docroot + '/' + Typedir + '/' + fname + '/' + "NEW.xml" );
				if ( F.exists() )
				{
					Out.printf( tableitem, fname, fname, fname, fname ); 
				}
			}
		}
		Out.println( footer ); 
		Out.close();
	} // eo buildPage

/**
 * answer Get
 */
	public void doGet (HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		buildPage( request, response );
	} // eo doGet

/**
 * answer Post
 */
	public void doPost (HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		buildPage( request, response );
	} // eo doPost


} // eoclass
