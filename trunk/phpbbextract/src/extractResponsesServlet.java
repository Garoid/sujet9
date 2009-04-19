

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import org.htmlparser.util.ParserException;

/**
 * Servlet implementation class extractResponsesServlet
 */
public class extractResponsesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	/**
     * @see HttpServlet#HttpServlet()
     */
    public extractResponsesServlet() {
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
		String directoryPath = this.getServletContext().getRealPath("/fichierXML");
		Utils.setDirectoryPath(directoryPath);
		int nbProfondeur = Integer.parseInt(request.getParameter("profondeur"));
		Utils.setProfondeur(nbProfondeur);
		//Utils.setProfondeur(2);
		String msg = request.getParameter("idPost");
		int nbPageCourante = Integer.parseInt(request.getParameter("pageCourante"));
		int nbLastPage = Integer.parseInt(request.getParameter("lastPage"));
		try {
			Utils.traiterPages(msg, nbPageCourante,nbLastPage);
			Utils.creerPostsXML(Utils.getListePost());
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
