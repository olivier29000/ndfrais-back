package com.ol.chronoshare.services;

import com.ol.chronoshare.model.DTO.TicketDTO;
import com.ol.chronoshare.model.Image;
import com.ol.chronoshare.model.Ticket;
import com.ol.chronoshare.model.User;
import com.ol.chronoshare.model.exceptions.ChronoshareException;
import com.ol.chronoshare.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    ConvertorService convertorService;

    public TicketDTO createTicket(Image image, User user) {
        Ticket ticket = ticketRepository.save(new Ticket(
                image,
                user
        ));
        return convertorService.convertToTicketDTO(ticket);
    }

    public List<TicketDTO> getAllDTOByUserAndYearMonth(User user, YearMonth yearMonth){
        return getAllByUserAndYearMonth(user, yearMonth)
                .stream().map(t -> convertorService.convertToTicketDTO(t))
                .toList();
    }

    public List<Ticket> getAllByUserAndYearMonth(User user, YearMonth yearMonth){
        return ticketRepository.findAllByUserIdAndYearMonth(user.getId(), yearMonth.getYear(), yearMonth.getMonthValue());
    }


    public TicketDTO updateTicket(TicketDTO ticketDTO,User user) {
        Ticket ticket = findById(ticketDTO.getId());
        if(!ticket.getUser().getId().equals(user.getId())){
            throw new ChronoshareException("Vous  n'avez pas les droits");
        }
        ticket.setDateTicket(ticketDTO.getDateTicket());
        ticket.setNotes(ticketDTO.getNotes());
        ticket.setMontant(ticketDTO.getMontant());
        ticket.setTitre(ticketDTO.getTitre());
        return convertorService.convertToTicketDTO(ticketRepository.save(ticket));
    }

    public Ticket findById(Integer id) {
       Optional<Ticket> optTicket = ticketRepository.findById(id);
        if(optTicket.isEmpty()){
            throw new ChronoshareException("Aucun ticket en BDD avec cet id");
        }
        return optTicket.get();

    }
}
