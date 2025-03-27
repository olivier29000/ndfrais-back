package com.ol.chronoshare.services;

import com.ol.chronoshare.model.TokenModifMotDePasse;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailService {

    @Value("${redirect.url}")
    private String URL;
    @Value("${spring.mail.username}")
    private String emailAdmin;
    @Value("${path.resource}")
    private String pathResource;
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

    public void sendEmailCreationCompte(String to, String subject, String urlConfirmation) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(emailAdmin);

        // Charger le template HTML
        String htmlContent = new String(Files.readAllBytes(Paths.get(pathResource + "resources/email-inscription.html")));
        // Remplacer le placeholder par l'URL de confirmation
        htmlContent = htmlContent.replace("${urlConfirmation}", urlConfirmation);
        helper.setText(htmlContent, true);

        // Ajouter une image inline
        FileSystemResource logoImage = new FileSystemResource(pathResource + "resources/images/logo.png");
        helper.addInline("logoImage", logoImage);

        javaMailSender.send(message);
    }

    public void sendEmail(
            String to,
            String subject,
            String title,
            String content
    ) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(emailAdmin);

        // Charger le template HTML
        String htmlContent = new String(Files.readAllBytes(Paths.get(pathResource + "resources/email-empty.html")));
        // Remplacer le placeholder par l'URL de confirmation
        htmlContent = htmlContent.replace("${title}", title);
        htmlContent = htmlContent.replace("${content}", content);

        helper.setText(htmlContent, true);

        // Ajouter une image inline
        FileSystemResource logoImage = new FileSystemResource(pathResource + "resources/images/logo.png");
        helper.addInline("logoImage", logoImage);

        javaMailSender.send(message);
    }
}
