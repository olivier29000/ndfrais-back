package com.ol.chronoshare.services;

import com.ol.chronoshare.model.TokenModifMotDePasse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailService {

    @Value("${redirect.url}")
    private String URL;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailOubliMotDePasse(TokenModifMotDePasse tokenModifMotDePasse) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(tokenModifMotDePasse.getUser().getEmail());
        helper.setSubject("Vous avez oublié votre mot de passe ?");
        helper.setFrom("contact@narratrice.fr");

        // Charger le template HTML
        String htmlContent = new String(Files.readAllBytes(Paths.get("/app/resources/email-changement-mot-de-passe.html")));
        String urlChangementMotDePasse = URL + "changement-mot-de-passe/" + tokenModifMotDePasse.getToken();
        // Remplacer le placeholder par l'URL de confirmation
        htmlContent = htmlContent.replace("${urlChangementMotDePasse}", urlChangementMotDePasse);
        helper.setText(htmlContent, true);

        // Ajouter une image inline
        FileSystemResource logoImage = new FileSystemResource("/app/resources/images/logo.png");
        helper.addInline("logoImage", logoImage);

        javaMailSender.send(message);
    }
}
