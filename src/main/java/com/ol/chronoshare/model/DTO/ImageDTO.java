package com.ol.chronoshare.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {

    private Integer id;
    private String nom;
    private String base64;

    public ImageDTO(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
    }
}
