package com.ClinicaOnline.TF_grupo6.Services;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Service
public class CorreosService {

    private final String remitente = System.getenv("SPRING_MAIL_USERNAME");
    private final String contrasena = System.getenv("SPRING_MAIL_PASSWORD");

    public void enviarCorreo(String destinatario, String asunto, String cuerpo, String imagenRuta) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, contrasena);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            MimeBodyPart htmlParte = new MimeBodyPart();
            htmlParte.setContent("<h3>" + cuerpo + "</h3>", "text/html");

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlParte);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Correo enviado correctamente!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error enviando correo: " + e.getMessage());
        }
    }
}
