package com.ol.chronoshare.model.DTO;

import com.ol.chronoshare.model.Position;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrajetDTO {

    private Integer id;
    private String titre;
    private Double nbkm;
    private LocalDateTime dateTrajet;
    private LocalDateTime dateCreation;
    private Position depart;
    private Position arrive;
}
