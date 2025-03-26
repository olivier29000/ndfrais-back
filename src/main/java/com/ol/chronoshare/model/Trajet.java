package com.ol.chronoshare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "trajets")
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titre;
    private Double nbkm;
    private LocalDateTime dateTrajet;
    private LocalDateTime dateCreation;
    private String displayedDepart;
    private Double latDepart;
    private Double lonDepart;
    private String displayedArrive;
    private Double latArrive;
    private Double lonArrive;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;


    public Trajet(String titre, Double nbkm, LocalDateTime dateTrajet, String displayedDepart, Double latDepart, Double lonDepart, String displayedArrive, Double latArrive, Double lonArrive, User user) {
        this.titre = titre;
        this.nbkm = nbkm;
        this.dateTrajet = dateTrajet;
        this.displayedDepart = displayedDepart;
        this.latDepart = latDepart;
        this.lonDepart = lonDepart;
        this.displayedArrive = displayedArrive;
        this.latArrive = latArrive;
        this.lonArrive = lonArrive;
        this.user = user;
        this.dateCreation = LocalDateTime.now();
    }
}
