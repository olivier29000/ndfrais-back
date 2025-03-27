package com.ol.chronoshare.controllers;

import com.ol.chronoshare.model.DTO.ImageDTO;
import com.ol.chronoshare.model.DTO.TicketDTO;
import com.ol.chronoshare.model.Image;
import com.ol.chronoshare.model.Ticket;
import com.ol.chronoshare.model.User;
import com.ol.chronoshare.model.exceptions.ChronoshareException;
import com.ol.chronoshare.repositories.ImageRepository;
import com.ol.chronoshare.services.ImageService;
import com.ol.chronoshare.services.TicketService;
import com.ol.chronoshare.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
@RestController
@RequestMapping("/back/ticket")
public class TicketController {

    @Autowired
    UserService userService;
    @Autowired
    ImageService imageService;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    private TicketService ticketService;


    @GetMapping("/delete-by-id/{ticketId}/{monthStr}")
    public List<TicketDTO> deleteTicketById(@PathVariable Integer ticketId,
                                           @PathVariable String monthStr,HttpServletRequest request) throws Exception {
        User user = userService.getUserFromCookie(request);
        ticketService.deleteById(ticketId, user);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(monthStr, formatter);
        return ticketService.getAllDTOByUserAndYearMonth(user, yearMonth);

    }
    @PostMapping("/upload-image")
    public TicketDTO uploadImage(@RequestParam("file") MultipartFile file,
                                       HttpServletRequest request) throws Exception {
        User user = userService.getUserFromCookie(request);
        // Sauvegarde le fichier sur le serveur
        String fileName = imageService.saveFile(file);
        // Sauvegarde les métadonnées du fichier dans la base de données
        Image image = new Image(
                file.getOriginalFilename(),
                fileName,
                file.getSize(),
                user);
        Image savedImage = imageRepository.save(image);
        return ticketService.createTicket(savedImage, user);
    }

    @GetMapping("/get-all-by-year-month/{monthStr}")
    public List<TicketDTO> getAllByYearMonth(@PathVariable String monthStr, HttpServletRequest request) throws Exception {
        User user = userService.getUserFromCookie(request);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(monthStr, formatter);
        return ticketService.getAllDTOByUserAndYearMonth(user, yearMonth);
    }

    @PostMapping("/update-ticket/{monthStr}")
    public List<TicketDTO> updateTicket(@PathVariable String monthStr,
                                  @RequestBody TicketDTO ticketDTO,
                                 HttpServletRequest request) throws Exception {
        User user = userService.getUserFromCookie(request);
        ticketService.updateTicket(ticketDTO, user);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(monthStr, formatter);
        return ticketService.getAllDTOByUserAndYearMonth(user, yearMonth);
    }

    @GetMapping("/get-image-by-ticket-id/{ticketId}")
    public ImageDTO updateTicket(@PathVariable Integer ticketId,
                                        HttpServletRequest request) throws Exception {
        User user = userService.getUserFromCookie(request);
        Ticket ticket = ticketService.findById(ticketId);
        if(!ticket.getUser().getId().equals(user.getId())){
            throw new ChronoshareException("Vous n'avez pas les droits");
        }
        return imageService.getById(ticket.getImage().getId());
    }

    @GetMapping("/get-excel-by-year-month/{monthStr}")
    public ResponseEntity<Resource> getExcelRecap(@PathVariable String monthStr, HttpServletRequest request) throws Exception {
        User user = userService.getUserFromCookie(request);
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(monthStr, formatterDate);
        List<Ticket> ticketList = ticketService.getAllByUserAndYearMonth(user , yearMonth);

        StringBuilder csvData = new StringBuilder("\uFEFF"); // Ajout du BOM UTF-8
        csvData.append(monthStr).append("\n").append("\n");
        csvData.append("date;titre;notes;montant;").append("\n");; // En-tête CSV
        for (Ticket ticket : ticketList) {
            csvData.append(ticket.getDateTicket())
                    .append(";").append(ticket.getTitre())
                    .append(";").append(ticket.getNotes())
                    .append(";").append(ticket.getMontant());
            csvData.append("\n");// En-tête CSV
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.toString().getBytes(StandardCharsets.UTF_8));
        InputStreamResource resource = new InputStreamResource(inputStream);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = now.format(formatter);
        String fileName = String.format("%s_%s_planifique.pro.csv", formattedDateTime, user.getEmail());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,   "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(resource);
    }
}
