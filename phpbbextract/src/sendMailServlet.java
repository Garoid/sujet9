

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


/**
 * Servlet implementation class sendMailServlet
 */
public class sendMailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public sendMailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//sendMail("marine.canessa@gmail.com","coucou","D:/eclipse/save.xml");
		System.out.println("coucou");
		envoyerMailSMTP("localhost", true);
	}
	
	public static boolean envoyerMailSMTP(String serveur, boolean debug) {   
        boolean result = false;   
        try {   
                 Properties prop = System.getProperties();   
                 prop.put("mail.smtp.host", serveur);   
                 Session session = Session.getDefaultInstance(prop,null);   
                 Message message = new MimeMessage(session);   
                 message.setFrom(new InternetAddress("marine.canessa@gmail.com"));   
                 InternetAddress[] internetAddresses = new InternetAddress[1];   
                 internetAddresses[0] = new InternetAddress("marine.canessa@gmail.com");   
                 message.setRecipients(Message.RecipientType.TO,internetAddresses);   
                 message.setSubject("Test");   
                 message.setText("test mail");   
                 message.setHeader("X-Mailer", "Java");   
                 message.setSentDate(new Date());   
                 session.setDebug(debug);   
                 Transport.send(message);   
                 result = true;   
        } catch (AddressException e) {   
                 e.printStackTrace();   
        } catch (MessagingException e) {   
                 e.printStackTrace();   
        }   
        return result;   
  }   

		/*public static void sendMail( String to, String subject, String file )
		{
			try
			{
				// Create some properties and get the default Session;
				Properties props = System.getProperties();
				props.put("mail.smtp.host", "localhost.server");
				Session session = Session.getDefaultInstance(props, null);
				String from = "marine.canessa@gmail.com";
				
//				Crée le message
				Message message = new MimeMessage(session);
				
//				On met les attributs d'entête ( sujet, adresse, expéditeur, destinataire)
				message.setSubject("Embedded Image");
				message.setFrom(new InternetAddress(from));
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(to));
				
//				Crée la partie message pour le contenu
				BodyPart messageBodyPart = new MimeBodyPart();
				String htmlText = "<H1>Hello</H1>" +
				"<img src=\"cid:image\">";
				
//				Type et sous-type du message
				messageBodyPart.setContent( htmlText, "text/html" );
				
//				Crée l'objet Multipart qui contiendra toutes les parties du message
//				on doit passer en paramètre "related", puisque les deux parties entres elles
//				sont reliées
				MimeMultipart multipart = new MimeMultipart( "related" );
				multipart.addBodyPart( messageBodyPart );
				
//				Crée l'autre partie du message qui contient l'image
				messageBodyPart = new MimeBodyPart();
				
//				Place l'image dans la partie
				DataSource fds = new FileDataSource( file );
				messageBodyPart.setDataHandler( new DataHandler(fds) );
				
//				Attribue un nom à l'entête de l'image ( pour faire le lien)
				messageBodyPart.setHeader("Content-ID", "<image>");
				
//				Ajoute la partie à l'objet Multipart
				multipart.addBodyPart( messageBodyPart  );
				
//				Ajoute l'objet Multipart au message
				message.setContent(multipart);
				
				Transport.send(message);
			}
			catch (Exception ex) 
			{
				ex.printStackTrace();
			}
		}*/


}
