

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import org.htmlparser.util.ParserException;

/**
 * Servlet implementation class extractPostServlet
 */
public class extractPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public extractPostServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//envoi le fichier à sauvegarder grace au window.open() chez le client
		System.out.println(this.getServletContext().getContextPath());
		System.out.println(this.getServletContext().getServerInfo());
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
			Post lePost = Utils.recupererPost(request.getParameter("id"));
			Utils.creerPostXML(lePost);
			
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
