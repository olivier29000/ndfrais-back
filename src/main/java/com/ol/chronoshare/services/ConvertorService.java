package com.ol.chronoshare.services;

import com.ol.chronoshare.model.DTO.ImageDTO;
import com.ol.chronoshare.model.DTO.TicketDTO;
import com.ol.chronoshare.model.DTO.TrajetDTO;
import com.ol.chronoshare.model.Image;
import com.ol.chronoshare.model.Position;
import com.ol.chronoshare.model.Ticket;
import com.ol.chronoshare.model.Trajet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
@Service
public class ConvertorService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    public ImageDTO convertToImageDTO(Image image) throws IOException {
        Path imagePath = Paths.get(uploadDir + "/images").resolve(image.getPath());

        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException("Image not found: " + image.getNom());
        }

        // Lire l'image sous forme de tableau de bytes
        byte[] imageBytes = Files.readAllBytes(imagePath);

        // Convertir en base64
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        return new ImageDTO(image.getId(), image.getNom(), base64Image);
    }

    public TrajetDTO convertToTrajetDTO(Trajet trajet) {
        return new TrajetDTO(
                trajet.getId(),
                trajet.getTitre(),
                trajet.getNbkm(),
                trajet.getDateTrajet(),
                trajet.getDateCreation(),
                new Position(
                        trajet.getDisplayedDepart(),
                        trajet.getLatDepart(),
                        trajet.getLonDepart()
                ),
                new Position(
                        trajet.getDisplayedArrive(),
                        trajet.getLatArrive(),
                        trajet.getLonArrive()
                )
        );
    }

    public TicketDTO convertToTicketDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getTitre(),
                ticket.getNotes(),
                ticket.getDateCreation(),
                ticket.getDateTicket(),
                ticket.getMontant()
        );
    }
}
