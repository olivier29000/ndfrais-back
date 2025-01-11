package com.ol.chronoshare.repositories;

import com.ol.chronoshare.model.TokenModifMotDePasse;
import com.ol.chronoshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenModifMotDePasseRepository extends JpaRepository<TokenModifMotDePasse, Integer> {
    Optional<TokenModifMotDePasse> findByToken(String token);
}
