

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.NotFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
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
		int nbProfondeur = Integer.parseInt(request.getParameter("profondeur"));
		String msg = request.getParameter("idPost");
		int nbPageCourante = Integer.parseInt(request.getParameter("pageCourante"));
		int nbLastPage = Integer.parseInt(request.getParameter("lastPage"));
		//System.out.println(nbPageCourante);
		//System.out.println(nbLastPage);
		try {
			recupBloc(3,msg, nbPageCourante,nbLastPage);
			//recupBloc(nbProfondeur,msg);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void recupBloc(int n, String idBegin, int pageBegin, int lastPage) throws ParserException, UnsupportedEncodingException
	{
		//on reinitialise a liste
		Utils.getListePost().clear();
		
		//System.out.println(Utils.getPage());
		Post p = Utils.recupererPost(idBegin);
		//System.out.println(p.getAuteur()+" dit: "+p.getMessage());
		
		//on recupere les pages suivantes a ce post jusqu'a la fin de la discussion
		String lienAdressCourante = Utils.getPage();
		recupPostCite(n,idBegin,p.getAuteur(),p.getMessage());
		String adresseBegin = "";
		String adresseCheck ="";int nbCourant = 0;
		int iFin = lienAdressCourante.lastIndexOf(".html");
		int tiret = lienAdressCourante.lastIndexOf("-");
		if(tiret>(iFin-5)){
			//on garde l'adresse sans l'extension
			adresseBegin = lienAdressCourante.substring(0,tiret);
		}
		else{
			//on garde l'adresse sans l'extension
			adresseBegin = lienAdressCourante.substring(0,iFin);
		}
		for(int i = pageBegin ; i < lastPage ; i++)
		{
			nbCourant = 15*(i);
			adresseCheck = adresseBegin+"-"+nbCourant+".html";
			//System.out.println(adresseCheck);
			Utils.setPage(adresseCheck);
			recupPostCite(n,idBegin,p.getAuteur(),p.getMessage());
			
		}	
	}
	
	public void recupPostCite(int n, String idBegin,String auteur, String msg) throws ParserException
	{
		Parser parser = new Parser(Utils.getPage());
		AndFilter divID = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id"));
		AndFilter divClass = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class"));
		AndFilter divClassS = new AndFilter(new TagNameFilter("div"), new NotFilter(new HasAttributeFilter("class","signature")));
		NodeFilter[] IdClass = new NodeFilter[3];
		IdClass[0] = divID;
		IdClass[1] = divClass;
		IdClass[2] = divClassS;
		AndFilter postIDs = new AndFilter(IdClass );

		AndFilter classInner = new AndFilter(new TagNameFilter("div"),new HasParentFilter(postIDs));
		AndFilter classPostBody = new AndFilter(new TagNameFilter("div"),new HasParentFilter(classInner));
		AndFilter divPostbody = new AndFilter(new TagNameFilter("div"),new HasParentFilter(classPostBody));
		AndFilter divContent = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class", "content"));
		NodeFilter[] postbodyContent = new NodeFilter[2];
		postbodyContent[0] = divPostbody;
		postbodyContent[1] = divContent;
		AndFilter content = new AndFilter(postbodyContent);
		getCiteRecursive(n, content, parser, auteur, msg);
	}
	
	public void getCiteRecursive(int nb, AndFilter filtreParent, Parser parser, String auteurRecherche, String msgRecherche) throws ParserException {
		AndFilter divcite = new AndFilter(new TagNameFilter("div"), new HasParentFilter(filtreParent));
		NodeList ListPost = parser.parse(divcite);
		
		for(int i =0;i<ListPost.size();i++)
		{
			String blocContent = ListPost.elementAt(i).toHtml();
			//System.out.println("parent"+ListPost.elementAt(i).getParent().getParent().getParent().getParent());
			
			TagNode parent = (TagNode)ListPost.elementAt(i).getParent().getParent().getParent().getParent();
			//System.out.println("parent : "+parent);
			System.out.println("id : "+parent.getAttribute("id"));
			if(blocContent.contains("<blockquote")){
				
				while(blocContent.contains("</blockquote")){
					blocContent = blocContent.substring(blocContent.lastIndexOf("</blockquote>")+13,blocContent.length());
				}
			}else{
				//on supprime l'auteur "a écrit :"
				if(blocContent.contains("crit:")){
					blocContent = blocContent.substring(blocContent.indexOf(":")+1, blocContent.length());
				}
			}
			verifMsg(blocContent, auteurRecherche, msgRecherche);
		}
		nb = nb-1;
		if (nb>0) {
			//System.out.println("nb la ou on est (recursif): "+ nb);
			parser.reset();
			getCiteRecursive(nb, divcite, parser, auteurRecherche, msgRecherche);
		}
	}
	
	public void verifMsg(String contenuMsg,String auteurRecherche, String msgRecherche  ) {
		
		contenuMsg = contenuMsg.replaceAll("<[^>]*>", "");

		
		if (msgRecherche.contains(contenuMsg) && contenuMsg.length()>2) {
			System.out.println("--------------------------------------------");
			System.out.println("message à tester : "+ contenuMsg);
			System.out.println("message test :"+msgRecherche);
			System.out.println("auteur test :"+auteurRecherche);
			
			System.out.println("okkkkkkkkk");
			System.out.println("--------------------------------------------");
		} else {
			//System.out.println("kooooooooo");
		}
		
	}
}
