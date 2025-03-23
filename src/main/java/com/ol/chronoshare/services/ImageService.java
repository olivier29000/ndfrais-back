package com.ol.chronoshare.services;

import com.ol.chronoshare.model.DTO.ImageDTO;
import com.ol.chronoshare.model.Image;
import com.ol.chronoshare.model.User;
import com.ol.chronoshare.model.exceptions.ChronoshareException;
import com.ol.chronoshare.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    ConvertorService convertorService;

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty() || Objects.isNull(file.getContentType())) {
            throw new IllegalArgumentException("File is empty or null!");
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed!");
        }
        // Créer le répertoire s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir + "/images");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // Génère un nom unique pour le fichier
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Enregistre le fichier sur le serveur
        Files.copy(file.getInputStream(), filePath);

        return fileName; // Retourne le nom du fichier pour le stocker dans la base
    }

    public List<ImageDTO> getAllByUser(User user) throws IOException {
        List<Image> images = imageRepository.findAllByUser(user);
        List<ImageDTO> imageDTOList = new ArrayList<>();
        for (Image image : images) {
            imageDTOList.add(convertorService.convertToImageDTO(image));
        }
        return imageDTOList;
    }

    public List<ImageDTO> deleteImageById(Integer imageId, User user) throws Exception {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new Exception("Image not found"));
        if (!image.getUser().equals(user)) {
            throw new Exception("Unauthorized access");
        }
        imageRepository.delete(image);
        return getAllByUser(user);
    }
    public Image findById(Integer imageId) {
        Optional<Image> optImage = imageRepository.findById(imageId);
        if(optImage.isEmpty()) {
            throw new ChronoshareException("audio-not-found");
        }
        return optImage.get();

    }

    public ImageDTO getById(Integer imageId) throws IOException {

        return convertorService.convertToImageDTO(findById(imageId));

    }
}
