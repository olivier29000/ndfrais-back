package com.ol.chronoshare.services;

import com.ol.chronoshare.model.TokenModifMotDePasse;
import com.ol.chronoshare.repositories.TokenModifMotDePasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenModifMotDePasseService {

    @Autowired
    TokenModifMotDePasseRepository tokenModifMotDePasseRepository;

    public TokenModifMotDePasse save(TokenModifMotDePasse tokenModifMotDePasse){
        return tokenModifMotDePasseRepository.save(tokenModifMotDePasse);
    }

    public Optional<TokenModifMotDePasse> findByToken(String token){
        return tokenModifMotDePasseRepository.findByToken(token);
    }
}
