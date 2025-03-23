package com.ol.chronoshare.model.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {

    private int id;
    private String titre;
    private String notes;
    private LocalDateTime dateCreation;
    private LocalDateTime dateTicket;
    private Double montant;
}
