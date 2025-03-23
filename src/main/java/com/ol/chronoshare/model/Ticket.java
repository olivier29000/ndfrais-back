package com.ol.chronoshare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titre;
    private String notes;
    private LocalDateTime dateCreation;
    private LocalDateTime dateTicket;
    private Double montant;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Ticket(Image image, User user) {
        this.image = image;
        this.user = user;
        this.dateTicket = LocalDateTime.now();
        this.dateCreation = LocalDateTime.now();
    }
}
