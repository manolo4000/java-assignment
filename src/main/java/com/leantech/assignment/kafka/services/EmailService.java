package com.leantech.assignment.kafka.services;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.sun.mail.imap.protocol.FLAGS;


@Service
public class EmailService {
	
    private static final String EMAIL_TEXT_TEMPLATE_NAME = "text/email-text";
    private static final String EMAIL_SIMPLE_TEMPLATE_NAME = "html/email-simple";
    private static final String EMAIL_WITHATTACHMENT_TEMPLATE_NAME = "html/email-withattachment";
    private static final String EMAIL_CONTROLES = "html/email-controles.html";
    private static final String EMAIL_INDICADORES = "html/email-indicadores.html";
    private static final String EMAIL_CAIDA_INTERRUPCION= "html/email-down.html";

    private static final String ECORISK_LOGO_IMAGE = "mail/html/images/logo.png";

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


    /* 
     * Send plain TEXT mail 
     */
    public void sendTextMail(
        final String recipientName, final String recipientEmail, final Locale locale)
        throws MessagingException {

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Example plain TEXT email");
        message.setFrom("thymeleaf@example.com");
        message.setTo(recipientEmail);

        // Create the plain TEXT body using Thymeleaf
        final String textContent = this.textTemplateEngine.process(EMAIL_TEXT_TEMPLATE_NAME, ctx);
        message.setText(textContent);

        // Send email
        this.mailSender.send(mimeMessage);
    }


    /* 
     * Send HTML mail (simple) 
     */
    public void sendSimpleMail(
        final String recipientName, final String recipientEmail, final Locale locale)
        throws MessagingException {

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Example HTML email (simple)");
        message.setFrom("thymeleaf@example.com");
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.htmlTemplateEngine.process(EMAIL_SIMPLE_TEMPLATE_NAME, ctx);
        message.setText(htmlContent, true /* isHtml */);

        // Send email
        this.mailSender.send(mimeMessage);
    }


    /* 
     * Send HTML mail with attachment. 
     */
    public void sendMailWithAttachment(
        final String recipientName, final String recipientEmail, final String attachmentFileName,
        final byte[] attachmentBytes, final String attachmentContentType, final Locale locale)
        throws MessagingException {

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message
            = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject("Example HTML email with attachment");
        message.setFrom("thymeleaf@example.com");
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.htmlTemplateEngine.process(EMAIL_WITHATTACHMENT_TEMPLATE_NAME, ctx);
        message.setText(htmlContent, true /* isHtml */);

        // Add the attachment
        final InputStreamSource attachmentSource = new ByteArrayResource(attachmentBytes);
        message.addAttachment(
            attachmentFileName, attachmentSource, attachmentContentType);

        // Send mail
        this.mailSender.send(mimeMessage);
    }


    
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
    }
    
    //Store in send elements
    public void EmailsEnviados(MimeMessage mimeMessage) {
    	/*  Set the mail properties  */
    	
    	Properties properties = System.getProperties();
    	Session session = Session.getDefaultInstance(properties);
    	String host = "manolo4000.com";
    	String user = "ecorisk@manolo4000.com";
    	String pwd =  "nvt7$@&46lC3";
    	try {
			Store store = session.getStore("imap");
			store.connect(host, user, pwd);  
			Folder folder = (Folder) store.getFolder("INBOX.Sent");
	        folder.open(Folder.READ_WRITE);
	        System.out.println("guardando...");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    }

    /*
     * Custom Mail
     */
    public void sendMailDown(
             final String Para,final String Asunto, final Context ctx)
            throws MessagingException {


        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message
            = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject(Asunto);
        message.setFrom("ecorisk@ecopetrol.com.co");
        message.setTo(Para);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.htmlTemplateEngine.process(EMAIL_CAIDA_INTERRUPCION, ctx);
        message.setText(htmlContent, true /* isHtml */);
        
        message.addInline("guytexting", new ClassPathResource("mail/html/images/Guy_texting.png"), PNG_MIME);
        message.addInline("logo", new ClassPathResource(ECORISK_LOGO_IMAGE), PNG_MIME);
        
        // Send mail
        this.mailSender.send(mimeMessage);
        
        EmailsEnviados(mimeMessage);
    }

    
    /*
     * Custom Mail
     */
    public void sendMailControles(
             final String Para,final String Asunto, final Context ctx)
            throws MessagingException {


        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message
            = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject(Asunto);
        message.setFrom("ecorisk@ecopetrol.com.co");
        message.setTo(Para);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.htmlTemplateEngine.process(EMAIL_CONTROLES, ctx);
        message.setText(htmlContent, true /* isHtml */);
        
        message.addInline("logo", new ClassPathResource(ECORISK_LOGO_IMAGE), PNG_MIME);
        
        // Send mail
        this.mailSender.send(mimeMessage);
        EmailsEnviados(mimeMessage);
    }
    
    /*
     * Custom Mail
     */
    public void sendMailIndicadores(
             final String Para,final String Asunto, final Context ctx)
            throws MessagingException {


        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message
            = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject(Asunto);
        message.setFrom("ecorisk@ecopetrol.com.co");
        message.setTo(Para);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.htmlTemplateEngine.process(EMAIL_INDICADORES, ctx);
        message.setText(htmlContent, true /* isHtml */);
        
        message.addInline("logo", new ClassPathResource(ECORISK_LOGO_IMAGE), PNG_MIME);
        
        // Send mail
        this.mailSender.send(mimeMessage);
        EmailsEnviados(mimeMessage);
    }


}