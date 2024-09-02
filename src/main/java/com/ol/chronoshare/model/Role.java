package com.ol.chronoshare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


public enum Role {

    ROLE_USER, ROLE_MANAGER, ROLE_COMPTABLE, ROLE_ADMIN;

}
