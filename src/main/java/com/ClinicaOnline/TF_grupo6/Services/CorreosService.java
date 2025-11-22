package com.ClinicaOnline.TF_grupo6.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Service
public class CorreosService {

    @Value("${spring.mail.username}")
    private String remitente;

    @Value("${spring.mail.password}")
    private String contrasena;

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

            String html = "<html><body>"
                    + "<h3>" + cuerpo + "</h3>"
                    + "<img src='cid:imagen_embebida' />"
                    + "</body></html>";

            MimeBodyPart htmlParte = new MimeBodyPart();
            htmlParte.setContent(html, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlParte);

            if (imagenRuta != null) {
                File imagen = new File(imagenRuta);
                if (imagen.exists()) {
                    MimeBodyPart imagenParte = new MimeBodyPart();
                    imagenParte.attachFile(imagen);
                    imagenParte.setContentID("<imagen_embebida>");
                    imagenParte.setDisposition(MimeBodyPart.INLINE);
                    multipart.addBodyPart(imagenParte);
                }
            }

            message.setContent(multipart);
            Transport.send(message);

            System.out.println("Correo enviado a " + destinatario);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            System.out.println("Error enviando correo: " + e.getMessage());
        }
    }
}
