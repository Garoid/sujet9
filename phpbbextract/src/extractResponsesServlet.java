

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
		
		System.out.println("taille : "+ListPost.size());
		//System.out.println("nb : "+nb);
		for(int i =0;i<ListPost.size();i++)
		{
			//System.out.println(nb+"--"+i+"!!!!!"+ListPost.elementAt(i).toHtml());
			String blocContent = ListPost.elementAt(i).toHtml();
			//blocContent.substring(blocContent.indexOf("<blockquote"), blocContent.indexOf("</blockquote"))
			if(blocContent.contains("<blockquote")){
				String debBlocContent = blocContent.substring(0, blocContent.indexOf("<blockquote"));
				//System.out.println("debbbbbbbbbbbbbbb"+debBlocContent);
				String finBlocContent = blocContent;
				/*while(finBlocContent.contains("<blockquote")){
					finBlocContent = finBlocContent.substring(finBlocContent.lastIndexOf("<blockquote"),finBlocContent.length());
					System.out.println("1111"+finBlocContent);
				}*/
				while(finBlocContent.contains("</blockquote")){
					finBlocContent = finBlocContent.substring(finBlocContent.lastIndexOf("</blockquote>")+13,finBlocContent.length());
					//System.out.println("1111"+finBlocContent);
				}
				blocContent = debBlocContent+ " "+ finBlocContent;
				//System.out.println("bloccccccccccccccc : "+blocContent);
				//String finBlocContent = blocContent.substring(blocContent.indexOf("<blockquote",), blocContent.length());
				System.out.println(blocContent);
				verifMsg(blocContent, auteurRecherche, msgRecherche);
			}
		}
		nb = nb-1;
		if (nb>0) {
			System.out.println("nb la ou on est (recursif): "+ nb);
			parser.reset();
			getCiteRecursive(nb, divcite, parser, auteurRecherche, msgRecherche);
		}
	}
	
	public void verifMsg(String contenuMsg,String auteurRecherche, String msgRecherche  ) {
		StringBuffer sb = new StringBuffer(contenuMsg);
		System.out.println("sb :::::::::dans verif::::"+sb.toString());
		//System.out.println("1::::::::::::::::"+contenuMsg.contains("<"));

		//System.out.println("**************************************"+sb.toString());
		while(contenuMsg.contains("<")||contenuMsg.contains(">")){
			//System.out.println("je pass:::::::::::::::::::::::::");
			sb = sb.delete(contenuMsg.indexOf("<"), contenuMsg.indexOf(">")+1);
		}
		contenuMsg = sb.toString();
		
		System.out.println("contenuMsg"+contenuMsg);
		System.out.println(msgRecherche);
		if (msgRecherche.contains(contenuMsg)) {
			System.out.println(auteurRecherche + "okkkkkkkkk");
		} else {
			System.out.println("kooooooooo");
		}

	}
}
