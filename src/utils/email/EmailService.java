package utils.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import entities.Persona;

import java.util.Properties;

public class EmailService {
	
    private String host = "smtp.gmail.com";
    private int port = 587;
    private String username = "ingresoegresoapprosario@gmail.com";
    private String password = "rhoictioskmdfpzo";

    public EmailService() {}

    public void sendEmail(Persona persona, String body, String subject) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(persona.getEmail()));
                message.setSubject(subject);
                message.setText(body);
                Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
