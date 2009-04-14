

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import sun.net.www.URLConnection;

/**
 * Servlet implementation class extractDiscussion
 */
public class extractDiscussion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public extractDiscussion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream is= new FileInputStream("d:/eclipse/save.xml");
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition","attachment;filename=save.xml");
        int count;
        byte buf[] = new byte[4096];
        while ((count = is.read(buf)) > -1)
        os.write(buf, 0, count);
        is.close();
        os.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Utils.setPage(request.getParameter("page"));
			int nbMsg = Integer.parseInt(request.getParameter("nbMsg"));
			Utils.recupererDiscussion(nbMsg);
			Utils.creerPostsXML(Utils.getListePost());
			
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
