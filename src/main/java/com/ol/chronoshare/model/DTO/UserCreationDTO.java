package com.ol.chronoshare.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreationDTO {
    private String email;
    private String password;
}
