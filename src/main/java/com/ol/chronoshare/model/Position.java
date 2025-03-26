package com.ol.chronoshare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String displayed;
    private Double lat;
    private Double lon;

    public Position(String displayed, Double lat, Double lon) {
        this.displayed = displayed;
        this.lat = lat;
        this.lon = lon;
    }
}
