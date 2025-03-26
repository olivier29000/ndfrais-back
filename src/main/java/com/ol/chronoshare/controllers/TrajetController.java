package com.ol.chronoshare.controllers;

import com.ol.chronoshare.model.DTO.ImageDTO;
import com.ol.chronoshare.model.DTO.TicketDTO;
import com.ol.chronoshare.model.DTO.TrajetDTO;
import com.ol.chronoshare.model.User;
import com.ol.chronoshare.services.TrajetService;
import com.ol.chronoshare.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/back/trajet")
public class TrajetController {

    @Autowired
    UserService userService;
    @Autowired
    TrajetService trajetService;
    @GetMapping("/get-all-by-year-month/{monthStr}")
    public List<TrajetDTO> getAllByYearMonth(@PathVariable String monthStr, HttpServletRequest request) throws Exception {
        User user = userService.getUserFromCookie(request);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(monthStr, formatter);
        return trajetService.getAllDTOByUserAndYearMonth(user, yearMonth);
    }

    @PostMapping("/update-trajet/{monthStr}")
    public List<TrajetDTO> updateTrajet(@PathVariable String monthStr,
                                        @RequestBody TrajetDTO trajetDTO,
                                        HttpServletRequest request) throws Exception {
        User user = userService.getUserFromCookie(request);
        trajetService.updateTrajet( user,trajetDTO);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(monthStr, formatter);
        return trajetService.getAllDTOByUserAndYearMonth(user, yearMonth);
    }

    @PostMapping("/create-trajet/{monthStr}")
    public List<TrajetDTO> createTrajet(@PathVariable String monthStr,
                                        @RequestBody TrajetDTO trajetDTO,
                                        HttpServletRequest request) throws Exception {
        User user = userService.getUserFromCookie(request);
        trajetService.createTrajet( user,trajetDTO);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(monthStr, formatter);
        return trajetService.getAllDTOByUserAndYearMonth(user, yearMonth);
    }

    @GetMapping("/delete-by-id/{trajetId}/{monthStr}")
    public List<TrajetDTO> deleteAudioById(@PathVariable Integer trajetId,
                                           @PathVariable String monthStr,
                                           HttpServletRequest request) throws Exception {
        User user = userService.getUserFromCookie(request);
        trajetService.deleteById(trajetId, user);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(monthStr, formatter);
        return trajetService.getAllDTOByUserAndYearMonth(user, yearMonth);
    }
}
