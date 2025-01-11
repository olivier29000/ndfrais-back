package com.ol.chronoshare.controllers;
import com.ol.chronoshare.model.DTO.ChangementMotDePasseDTO;
import com.ol.chronoshare.model.DTO.UserConnectedDTO;
import com.ol.chronoshare.model.DTO.UserCreationDTO;
import com.ol.chronoshare.model.User;
import com.ol.chronoshare.services.JwtAuthentificationService;
import com.ol.chronoshare.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
@RequestMapping("/back/user")
public class UserController {
    @Value("${redirect.url}")
    private String redirectUrl;

    @Autowired
    private UserService userService;
    @Autowired
    JwtAuthentificationService jwtAuthentificationService;


    @GetMapping("/verifAuthenticate")
    public ResponseEntity<UserConnectedDTO> verifAuthenticate(HttpServletRequest request) throws Exception {
        UserConnectedDTO userConnected = userService.getUserConnectedDTO(request);
        return ResponseEntity.ok(userConnected);
    }

    @PostMapping("/creation-compte")
    public ResponseEntity<?> creationCompte(@RequestBody UserCreationDTO userCreationDTO) throws Exception {

        userService.creationCompte(userCreationDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCreationDTO userCreationDTO) throws Exception {

        return userService.login(userCreationDTO);
    }

    @GetMapping("/oubli-mot-de-passe/{email}")
    public ResponseEntity<?> oubliMotDePasse(@PathVariable String email) throws Exception {

        return userService.mailOubliMotDePasse(email);
    }

    @PostMapping("/changement-mot-de-passe")
    public ResponseEntity<?> changementMotDePasse(@RequestBody ChangementMotDePasseDTO changementMotDePasseDTO) throws Exception {

        return userService.changementMotDePasse(changementMotDePasseDTO);
    }

    @GetMapping("/confirm-email/{tokenConfirmation}")
    public RedirectView confirmEmail(@PathVariable String tokenConfirmation, HttpServletResponse response) throws Exception {
        Optional<User> optionalUsers =  userService.findUserByTokenEnabled(tokenConfirmation);
        if(optionalUsers.isPresent()) {
            User user = optionalUsers.get();
            user = userService.enableTokenConfirmation(user);

            ResponseCookie responseCookie =  jwtAuthentificationService.getAuthenticationToken(user);
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
            return new RedirectView(redirectUrl);
        }else{
            return new RedirectView(redirectUrl);
        }
    }
}
