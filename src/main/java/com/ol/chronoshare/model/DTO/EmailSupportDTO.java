package com.ol.chronoshare.model.DTO;

import com.ol.chronoshare.model.ContactPreferenceSupport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailSupportDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer echelle;
    private String imageBase64;
    private ContactPreferenceSupport contactPreference;
    private String contact;
}
