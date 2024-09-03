package com.ol.chronoshare.controllers;
import com.ol.chronoshare.model.DTO.UserConnectedDTO;
import com.ol.chronoshare.model.DTO.UserCreationDTO;
import com.ol.chronoshare.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/back/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() throws Exception {

        return "hello";
    }

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
}
