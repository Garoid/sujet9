

import java.io.IOException;

import javax.servlet.RequestDispatcher;
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
		
        String from = request.getParameter("from");
    	String to = request.getParameter("to");
    	String subject = request.getParameter("subject");
    	String message = request.getParameter("message");
    	String login = request.getParameter("login");
    	String password = request.getParameter("password");
    	System.out.println(from+ to+subject+message+login+password);
		envoyerMailSMTP(from, to,subject,message,login,password);
		//processRequest(request, response);

	}
	
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
