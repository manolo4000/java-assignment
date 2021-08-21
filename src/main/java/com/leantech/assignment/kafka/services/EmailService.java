package com.leantech.assignment.kafka.services;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.sun.mail.imap.protocol.FLAGS;


@Service
public class EmailService {
	
    
    private static final String EMAIL_CONFIRMATION = "html/email-confirmation.html";
    
    private static final String EMAIL_ERRORS_LIST = "html/email-errors-list.html";

    private static final String LEANTECH_LOGO = "mail/html/images/logo.png";

    private static final String PNG_MIME = "image/png";
    

    @Autowired
    private JavaMailSender mailSender;

    @Qualifier("email")
    @Autowired
    private TemplateEngine htmlTemplateEngine;

    @Qualifier("email")
    @Autowired
    private TemplateEngine textTemplateEngine;

    @Qualifier("email")
    @Autowired
    private TemplateEngine stringTemplateEngine;

    
    //Get all email folders name
    public void GetFoldersNames(Store store){
    	javax.mail.Folder[] folders;
		try {
			folders = store.getDefaultFolder().list("*");
			for (javax.mail.Folder folder : folders) {
		        if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
		            System.out.println(folder.getFullName() + ": " + folder.getMessageCount());
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
    }
    
    //Store in send elements
    public void SentEmails(MimeMessage mimeMessage) {
    	/*  Set the mail properties  */
    	
    	Properties properties = System.getProperties();
    	Session session = Session.getDefaultInstance(properties);
    	String host = "manolo4000.com";
    	String user = "leantech@manolo4000.com";
    	String pwd =  "qhTP7jJge4bC9pVe";
    	try {
			Store store = session.getStore("imap");
			store.connect(host, user, pwd);  
			Folder folder = (Folder) store.getFolder("INBOX.Sent");
	        folder.open(Folder.READ_WRITE);
	        try {

	            folder.appendMessages(new Message[]{mimeMessage});
	            mimeMessage.setFlag(FLAGS.Flag.RECENT, true);
	            mimeMessage.setFlag(Flag.SEEN, true);

	        } catch (Exception ignore) {
	            System.out.println("error processing message " + ignore.getMessage());
	        } finally {
	            store.close();
	           // folder.close(false);
	        }
	        
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
    	 
    }

    /*
     * Custom Mail
     */
    public void sendMailConfirmation(
             final String To,final String subject, final Context ctx)
            throws MessagingException {


        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message
            = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject(subject);
        message.setFrom("leantech@manolo4000.com");
        message.setTo(To);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.htmlTemplateEngine.process(EMAIL_CONFIRMATION , ctx);
        message.setText(htmlContent, true /* isHtml */);
        
        message.addInline("guytexting", new ClassPathResource("mail/html/images/Guy_texting.png"), PNG_MIME);
        message.addInline("logo", new ClassPathResource(LEANTECH_LOGO), PNG_MIME);
        
        // Send mail
        mailSender.send(mimeMessage);
        SentEmails(mimeMessage);
    }
    
    /*
     * Custom Mail
     */
    public void sendMailErrosList(
             final String To,final String subject, final Context ctx)
            throws MessagingException {


        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message
            = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject(subject);
        message.setFrom("leantech@manolo4000.com");
        message.setTo(To);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.htmlTemplateEngine.process(EMAIL_ERRORS_LIST , ctx);
        message.setText(htmlContent, true /* isHtml */);
        
        message.addInline("roboerror", new ClassPathResource("mail/html/images/Roboerror.png"), PNG_MIME);
        message.addInline("logo", new ClassPathResource(LEANTECH_LOGO), PNG_MIME);
        
        // Send mail
        mailSender.send(mimeMessage);
        SentEmails(mimeMessage);
    }
    
    public boolean isEmail(String email) {
    	if(email == null) {
    		email = "";
    	}
    	if(!email.equals("")) {
    		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher mat = pattern.matcher(email);
            if(mat.matches()){
            	return true;
            }
    	}
    	return false;
    }

}