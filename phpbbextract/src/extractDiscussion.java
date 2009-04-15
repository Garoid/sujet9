

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import org.htmlparser.util.ParserException;

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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Utils.setPage(request.getParameter("page"));
			int nbMsg = Integer.parseInt(request.getParameter("nbMsg"));
			String direct = request.getParameter("direct");
			Utils.recupererDiscussion(nbMsg, direct);
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
