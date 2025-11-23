package com.ClinicaOnline.TF_grupo6.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class CorreosService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String destinatario, String asunto, String cuerpo, String imagenRuta) {

        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            helper.setTo(destinatario);
            helper.setSubject(asunto);

            String html = "<html><body>"
                    + "<h3>" + cuerpo + "</h3>";

            if (imagenRuta != null) {
                html += "<img src='cid:imagen_embebida'>";
            }

            html += "</body></html>";

            helper.setText(html, true);

            if (imagenRuta != null) {
                FileSystemResource img = new FileSystemResource(new File(imagenRuta));
                if (img.exists()) {
                    helper.addInline("imagen_embebida", img);
                }
            }

            helper.setFrom(System.getenv("SPRING_MAIL_USERNAME"));

            mailSender.send(mensaje);

            System.out.println("Correo enviado correctamente a " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error enviando correo: " + e.getMessage());
        }
    }
}
