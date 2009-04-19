

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class extractPageServlet
 */
public class extractPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public extractPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//envoi le fichier à sauvegarder grace au window.open() chez le client
		String directoryPath = this.getServletContext().getRealPath("/fichierXML");
		InputStream is= new FileInputStream(directoryPath+"\\saveFile.xml");
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition","attachment;filename=saveFile.xml");
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
		//stocke le contenu de la page courante dans la classe Utils
		Utils.setPage(request.getParameter("page"));
	}
}
