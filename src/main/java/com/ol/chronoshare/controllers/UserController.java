package com.ol.chronoshare.controllers;
import com.ol.chronoshare.model.DTO.ChangementMotDePasseDTO;
import com.ol.chronoshare.model.DTO.EmailSupportDTO;
import com.ol.chronoshare.model.DTO.UserConnectedDTO;
import com.ol.chronoshare.model.DTO.UserCreationDTO;
import com.ol.chronoshare.model.Ticket;
import com.ol.chronoshare.model.Trajet;
import com.ol.chronoshare.model.User;
import com.ol.chronoshare.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/back/user")
public class UserController {
    @Value("${redirect.url}")
    private String redirectUrl;

    @Autowired
    private UserService userService;
    @Autowired
    private TrajetService trajetService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private EmailService emailService;
    @Autowired
    JwtAuthentificationService jwtAuthentificationService;

    @GetMapping("/get-excel/{monthStr}")
    public ResponseEntity<Resource> getExcelRecap(@PathVariable String monthStr, HttpServletRequest request) throws Exception {
        User user = userService.getUserFromCookie(request);
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(monthStr, formatterDate);
        List<Ticket> ticketList = ticketService.getAllByUserAndYearMonth(user, yearMonth);
        List<Trajet> trajetList = trajetService.getAllByUserAndYearMonth(user, yearMonth);

        StringBuilder csvData = new StringBuilder("\uFEFF"); // Ajout du BOM UTF-8
        csvData.append(monthStr).append("\n").append("\n");
        csvData.append("Tickets").append("\n");
        csvData.append("date;titre;montant;note;").append("\n");
        for (Ticket ticket : ticketList) {
            csvData.append(ticket.getDateTicket())
                    .append(";").append(ticket.getTitre())
                    .append(";").append(ticket.getMontant())
                    .append(";").append(ticket.getNotes()).append("\n");

        }

        csvData.append("\n");
        csvData.append("Trajets").append("\n");
        csvData.append("date;titre;distance (en km);départ;arrivée").append("\n");
        for (Trajet trajet : trajetList) {
            csvData.append(trajet.getDateTrajet())
                    .append(";").append(trajet.getTitre())
                    .append(";").append(trajet.getNbkm())
                    .append(";").append(trajet.getDisplayedDepart())
                    .append(";").append(trajet.getDisplayedArrive()).append("\n");

        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.toString().getBytes(StandardCharsets.UTF_8));
        InputStreamResource resource = new InputStreamResource(inputStream);
        String fileName = "ndfrais.pro.csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,   "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(resource);
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

    @PostMapping("/email")
    public void emailSupport(@RequestBody EmailSupportDTO emailSupportDTO, HttpServletRequest request) throws Exception {
        UserConnectedDTO userConnected = userService.getUserConnectedDTO(request);
        emailService.sendEmailSupportToAdmin(userConnected.getEmail(), emailSupportDTO);
        emailService.sendEmailSupportToUser(userConnected.getEmail());
    }
}
