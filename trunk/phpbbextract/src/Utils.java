
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

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
 * classe contenant toutes les methodes utilis�es par les servlets
 *
 */
public class Utils {
	
	private static String page;
	private static ArrayList<Post> listePost = new ArrayList<Post>();
	
	

	public Utils() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * met a jour la page html courante � traiter
	 * @param page la page sous forme de chaine de caracteres
	 */
	public static void setPage(String page) {
		Utils.page = page;
		System.out.println("======nouvelle page:::::::::::::::::::::::::::"+page);
	}
	
	public static String getPage() {
		return page;
	}
	
	public static ArrayList<Post> getListePost() {
		return listePost;
	}

	public static void setListePost(ArrayList<Post> listePost) {
		Utils.listePost = listePost;
	}
	
	/**
	 * permet de recuperer un post en fonction de son id
	 * @param idSearch id du post � copier
	 * @throws ParserException
	 * @throws UnsupportedEncodingException 
	 */
	public static Post recupererPost(String idSearch) throws ParserException, UnsupportedEncodingException
	{
		Parser parser = new Parser(getPage());
		AndFilter postID = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", idSearch));
		AndFilter classInner = new AndFilter(new TagNameFilter("div"),new HasParentFilter(postID));
		AndFilter classPostBody = new AndFilter(new TagNameFilter("div"),new HasParentFilter(classInner));
		AndFilter titreH3 = new AndFilter(new TagNameFilter("H3"),new HasParentFilter(classPostBody));
		AndFilter auteurP = new AndFilter(new TagNameFilter("p"),new HasParentFilter(classPostBody));
		AndFilter content1 = new AndFilter(new TagNameFilter("div"),new HasParentFilter(classPostBody));
		AndFilter content2 = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class", "content"));
		NodeFilter[] postbodyContent = new NodeFilter[2];
		postbodyContent[0] = content1;
		postbodyContent[1] = content2;
		AndFilter content = new AndFilter(postbodyContent);
		
		//titre
		Node noeud = parser.parse(titreH3).elementAt(0);
		String titre = noeud.getChildren().elementAt(0).toPlainTextString();
		System.out.println("Titre :"+titre);
		
		parser.reset();
		
		//auteur et date
		noeud = parser.parse(auteurP).elementAt(0);
		String auteur =noeud.toPlainTextString().substring(3, noeud.toPlainTextString().indexOf("&raquo;"));
		String date =	noeud.toPlainTextString().substring(noeud.toPlainTextString().indexOf("&raquo;")+7, noeud.toPlainTextString().length());
		
		System.out.println("Auteur :"+auteur);
		System.out.println("Date :"+date);
		
		parser.reset();
		
		//message
		noeud = parser.parse(content).elementAt(0);
		String message = noeud.toPlainTextString();
		System.out.println("message"+message);
		
		//creation d'un objet post
		Post p = new Post(idSearch,titre,auteur,date,message);
		return p;
	}
	
	/**
	 * permet de recuperer tous les posts d'une page
	 * @throws ParserException
	 */
	public static String recupererPosts() throws ParserException
	{
		String result = "";
		String idP,titreP, auteurP, dateP, messageP;
		Post postCourant;

		Parser parser = new Parser(getPage());
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
		AndFilter titre = new AndFilter(new TagNameFilter("H3"),new HasParentFilter(classPostBody));
		AndFilter auteur = new AndFilter(new TagNameFilter("p"),new HasParentFilter(classPostBody));
		AndFilter divPostbody = new AndFilter(new TagNameFilter("div"),new HasParentFilter(classPostBody));
		AndFilter divContent = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class", "content"));
		NodeFilter[] postbodyContent = new NodeFilter[2];
		postbodyContent[0] = divPostbody;
		postbodyContent[1] = divContent;
		AndFilter content = new AndFilter(postbodyContent);
		
		//liste des posts de la page
		NodeList ListPost = parser.parse(postIDs);
		for(int i =0;i<ListPost.size();i++)
		{
			//on parse a nouveau sur les posts pour recuperer l'id
			parser.reset();
			ListPost = parser.parse(postIDs);
			TagNode t =(TagNode)ListPost.elementAt(i);
			idP = t.getAttribute("id");
		
			//titre
			parser.reset();
			ListPost = parser.parse(titre);
			Node noeud = ListPost.elementAt(i);
			
			titreP = noeud.getChildren().elementAt(0).toPlainTextString();
			result +=titreP;
			System.out.println("Titre :"+titreP);
			
			parser.reset();
			
			//auteur et date
			ListPost = parser.parse(auteur);
			noeud = ListPost.elementAt(i);
			auteurP =noeud.toPlainTextString().substring(3, noeud.toPlainTextString().indexOf("&raquo;"));
			dateP =noeud.toPlainTextString().substring(noeud.toPlainTextString().indexOf("&raquo;")+7, noeud.toPlainTextString().length());
			System.out.println("Auteur :"+auteurP);
			System.out.println("Date :"+dateP);
			
			parser.reset();
			
			//message
			ListPost = parser.parse(content);
			noeud = ListPost.elementAt(i);
			messageP = noeud.toPlainTextString();
			result+=messageP;
			System.out.println("message : "+messageP);
			System.out.println("==========================================");
			
			//creation du post et ajout dans la liste
			postCourant = new Post(idP,titreP,auteurP,dateP,messageP);
			getListePost().add(postCourant);
		}
		return result;
		
	}
	
	/**permet de parcourir la discussion par page, en recr�ant les url et r�cup�re les posts de chaque page 
	 * 
	 * @param nb le nombre de messages de la discussion
	 * @param direct false si le lien est une page de discussion, true si c'est une page d'index
	 * @throws ParserException
	 */
	public static void recupererDiscussion(int nb, String direct) throws ParserException
	{
		//on reinitialise la liste de posts si elle est pas vide
		Utils.getListePost().clear();
		
		String lienAdressCourante = "";
		String adresseBegin = "";
		
		if(direct.equals("false"))
		{
			//je recupere la premiere page
			lienAdressCourante =Utils.getPage();
			//on trouve la position de l'extension de la page .html et du tiret si c'est pas lka premi�re page de la discussion
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
		}	
		else{
			Parser parser = new Parser(Utils.getPage());
			AndFilter divHref = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "page-body"));
			AndFilter h2 = new AndFilter(new TagNameFilter("h2"), new HasParentFilter(divHref));
			AndFilter lien = new AndFilter(new TagNameFilter("a"), new HasParentFilter(h2));
			
			Node post = parser.parse(lien).elementAt(0);
			TagNode tg = (TagNode)post;
			
			//recuperation des posts de toutes les pages de la discussion
			lienAdressCourante = tg.getAttribute("href");
			Utils.setPage(lienAdressCourante);
			//on trouve la position de l'extension de la page .html 
			int iFin = lienAdressCourante.lastIndexOf(".html");
			//on garde l'adresse sans l'extension
			adresseBegin = lienAdressCourante.substring(0,iFin);
		}
		//calcul du nombre de pages
		int nbPages = (int)Math.ceil((float)(nb+1)/15);;
		int nbCourant =0;
			
		//recuperation premiere page
		Utils.recupererPosts();
	
		//pour chaque page, on construit l'url et on recupere les posts de la page
		String adresseCheck ="";
		for(int z =0;z<nbPages-1;z++)
		{
			nbCourant = nbCourant+15;
			adresseCheck = adresseBegin+"-"+nbCourant+".html";
			System.out.println(adresseCheck);
			Utils.setPage(adresseCheck);
			Utils.recupererPosts();
		}
	}
	
	/**
	 * creer le fichier XML contenant le post � envoyer au client
	 * @param postSave le post a convertir en xml et ecrire dans le fichier 
	 * @throws XMLStreamException
	 * @throws IOException 
	 */
	public static void creerPostXML(Post postSave) throws XMLStreamException, IOException{
		XMLOutputFactory out = XMLOutputFactory.newInstance();
		FileOutputStream output = new FileOutputStream("save.xml");
		XMLStreamWriter xmlsw = out.createXMLStreamWriter(output,"UTF-8");
		xmlsw.writeStartDocument();
			xmlsw.writeStartElement("POSTS");
				xmlsw.writeStartElement("POST");
				xmlsw.writeAttribute("id", postSave.getId());
					xmlsw.writeStartElement("Titre");
					xmlsw.writeCharacters(postSave.getTitre());
					xmlsw.writeEndElement();
					xmlsw.writeStartElement("Date");
					xmlsw.writeCharacters(postSave.getDate());
					xmlsw.writeEndElement();
					xmlsw.writeStartElement("Auteur");
					xmlsw.writeCharacters(postSave.getAuteur());
					xmlsw.writeEndElement();
					xmlsw.writeStartElement("Message");
					xmlsw.writeCharacters(postSave.getMessage());
					xmlsw.writeEndElement();
				xmlsw.writeEndElement();
			xmlsw.writeEndElement();
		xmlsw.writeEndDocument();
		xmlsw.flush();
		xmlsw.close();
		output.close();
	}
	
	/**
	 * creer le fichier XML contenant une discussion � envoyer au client
	 * @param liste la liste de post a enregistrer
	 * @throws XMLStreamException
	 * @throws IOException 
	 */
	public static void creerPostsXML(ArrayList<Post> liste) throws XMLStreamException, IOException{
		XMLOutputFactory out = XMLOutputFactory.newInstance();
		FileOutputStream output = new FileOutputStream("save.xml");
		XMLStreamWriter xmlsw = out.createXMLStreamWriter(output, "UTF-8");
		xmlsw.writeStartDocument();
		xmlsw.writeStartElement("POSTS");
		for(int i=0;i<liste.size();i++){
			Post p = liste.get(i);
			xmlsw.writeStartElement("POST");
			xmlsw.writeAttribute("id", p.getId());
				xmlsw.writeStartElement("Titre");
				xmlsw.writeCharacters(p.getTitre());
				xmlsw.writeEndElement();
				xmlsw.writeStartElement("Date");
				xmlsw.writeCharacters(p.getDate());
				xmlsw.writeEndElement();
				xmlsw.writeStartElement("Auteur");
				xmlsw.writeCharacters(p.getAuteur());
				xmlsw.writeEndElement();
				xmlsw.writeStartElement("Message");
				xmlsw.writeCharacters(p.getMessage());
				xmlsw.writeEndElement();
			xmlsw.writeEndElement();
		}
		xmlsw.writeEndElement();
	xmlsw.writeEndDocument();
		xmlsw.flush();
		xmlsw.close();
		output.close();

		
	}
	
	/**
	 * methode qui permet d'envoyer un mail contenant le fichierjoint via le protocole SMTP de gmail
	 * @param from l'expediteur
	 * @param to le destinataire
	 * @param subject le sujet du mail
	 * @param message le message contenu du mail
	 * @param login le login de l'expediteur
	 * @param password le mot de passe de l'expediteur
	 */
 	public static void envoyerMailSMTP(String from,String to,String subject,String message,String login,String password) {   
    	try {
    		Properties props = new Properties();
    		props.setProperty("mail.host", "smtp.gmail.com");
    		props.setProperty("mail.smtp.port", "587");
    		props.setProperty("mail.smtp.auth", "true");
    		props.setProperty("mail.smtp.starttls.enable", "true");
    		String filename = "d:/eclipse/save.xml";
    		
    		Authenticator auth = new SMTPAuthenticator(login, password);
    		Session session = Session.getInstance(props, auth);
    		MimeMessage msg = new MimeMessage(session);
    		
    		// Create the message part
    		Multipart mp = new MimeMultipart();         		
    		msg.setText(message);
    		msg.setSubject(subject);
    		msg.setFrom(new InternetAddress(from));
    		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    		msg.setContent(mp);
    		MimeBodyPart mbp2 = new MimeBodyPart();			
    		DataSource ds = new FileDataSource(filename);
     		mbp2.setDataHandler(new DataHandler(ds));		   
     		mbp2.setFileName("save.xml");			   
	     	mp.addBodyPart(mbp2);
    		Transport.send(msg);

    	} catch (AuthenticationFailedException ex) {
    		ex.printStackTrace();
    	} catch (AddressException ex) {
    		ex.printStackTrace();
    	} catch (MessagingException ex) {
    		ex.printStackTrace();
    	}
 	}
        
      /**
       * classe interne pour l'autentification au SMTP
       *
       */
    private static class SMTPAuthenticator extends Authenticator {

        private PasswordAuthentication authentication;

        public SMTPAuthenticator(String login, String password) {
            authentication = new PasswordAuthentication(login, password);
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }
	
}
