package com.ol.chronoshare.services;

import com.ol.chronoshare.model.DTO.UserConnectedDTO;
import com.ol.chronoshare.model.DTO.UserCreationDTO;
import com.ol.chronoshare.model.User;
import com.ol.chronoshare.model.exceptions.ChronoshareException;
import com.ol.chronoshare.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private JwtAuthentificationService jwtAuthentificationService;

    public ResponseEntity<UserConnectedDTO> login(UserCreationDTO userCreationDTO) throws Exception {
        // Charger les détails de l'utilisateur par le nom d'utilisateur
        UserDetails userDetails = userDetailsService.loadUserByUsername(userCreationDTO.getEmail());
        if(!userDetails.isEnabled()){
            throw new ChronoshareException(userCreationDTO.getEmail() + " n'a pas été validé via l\'email envoyé");
        }
        // Vérifier si le mot de passe est correct
        if (userDetails != null && bcrypt.matches(userCreationDTO.getPassword(), userDetails.getPassword())) {
            // Retourner la réponse avec le token
            ResponseCookie tokenCookie = jwtAuthentificationService.createAuthenticationToken(userDetails.getUsername(), userCreationDTO.getPassword());
            Optional<User>  optUser = userRepository.findByEmail(userDetails.getUsername());
            if(optUser.isPresent()){
                UserConnectedDTO userConnected = new UserConnectedDTO(optUser.get().getPseudo(), optUser.get().getEmail());
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenCookie.toString()).body(userConnected);
            }else {
                throw new ChronoshareException("Erreur anormale pour " + userCreationDTO.getEmail());
            }

        } else {
            // Retourner une réponse d'erreur en cas d'échec de l'authentification
            throw new ChronoshareException("Mauvais mot de passe pour " + userCreationDTO.getEmail());
        }
    }

    public void creationCompte(UserCreationDTO userCreationDTO) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(userCreationDTO.getEmail());
        if(optionalUser.isPresent()){
            if(optionalUser.get().isEnabled()){
                throw new ChronoshareException("L'adresse email " + userCreationDTO.getEmail() + " est déjà utilisée et a été confirmée");
            }else{
                throw new ChronoshareException("L'adresse email " + userCreationDTO.getEmail() + " doit être confirmée par l'email reçu, vérifiez vos spams");
            }
        }

        User user = userRepository.save(new User(userCreationDTO.getEmail(),bcrypt.encode(userCreationDTO.getPassword()))) ;

       // emailService.sendEmailCreationCompte(user.getEmail(),  "activation de votre compte", "www.pourdubeurre.bzh/back/users/confirm-email/" +  user.getTokenConfirmation());




    }
}
