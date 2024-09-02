package com.ol.chronoshare.model;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String email;
    private String pseudo;
    private String password;
    private boolean enabled;
    private String tokenEnabled;
    private LocalDateTime dateCreation;
    private LocalDateTime lastConnection;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<Role> roleList;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.pseudo = email;
        this.enabled = false;
        this.dateCreation = LocalDateTime.now();
        this.roleList = new ArrayList<>();
        this.roleList.add(Role.ROLE_USER);
        Integer n = 10;
        StringBuilder stringBuilder = new StringBuilder(n);
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random RANDOM = new SecureRandom();
        for (int i = 0; i < n; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(index));
        }
        this.tokenEnabled = stringBuilder.toString();
    }
}
