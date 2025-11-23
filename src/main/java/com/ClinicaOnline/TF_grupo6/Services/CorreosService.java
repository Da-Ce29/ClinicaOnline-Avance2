package com.ClinicaOnline.TF_grupo6.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CorreosService {

    @Value("${postmark.api.key}")
    private String apiKey;

    @Value("${postmark.from.email}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviarCorreo(String destinatario, String asunto, String cuerpo, String imagenRuta) {
        try {
            String url = "https://api.postmarkapp.com/email";

            Map<String, Object> payload = new HashMap<>();
            payload.put("From", fromEmail);
            payload.put("To", destinatario);
            payload.put("Subject", asunto);
            payload.put("HtmlBody", cuerpo);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Postmark-Server-Token", apiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            restTemplate.postForEntity(url, request, String.class);

            System.out.println("Correo enviado a " + destinatario);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error enviando correo: " + e.getMessage());
        }
    }
}
