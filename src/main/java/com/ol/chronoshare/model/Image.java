package com.ol.chronoshare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private LocalDateTime dateUpload;
    private String path;
    private Long  size;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Qui a uploadé le fichier


    public Image(String nom, String path, Long size, User user) {
        this.nom = nom;
        this.path = path;
        this.size = size;
        this.user = user;
        this.dateUpload = LocalDateTime.now();
    }
}
