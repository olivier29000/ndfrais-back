package com.ol.chronoshare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class TokenModifMotDePasse {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    private String token;
    private LocalDateTime localDateTime;

    public TokenModifMotDePasse(User user, String token, LocalDateTime localDateTime) {
        this.user = user;
        this.token = token;
        this.localDateTime = localDateTime;
    }
}
