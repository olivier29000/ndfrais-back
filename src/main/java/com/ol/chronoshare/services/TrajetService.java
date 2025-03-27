package com.ol.chronoshare.services;

import com.ol.chronoshare.model.DTO.TrajetDTO;
import com.ol.chronoshare.model.Position;
import com.ol.chronoshare.model.Trajet;
import com.ol.chronoshare.model.User;
import com.ol.chronoshare.model.exceptions.ChronoshareException;
import com.ol.chronoshare.repositories.PositionRepository;
import com.ol.chronoshare.repositories.TicketRepository;
import com.ol.chronoshare.repositories.TrajetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class TrajetService {
    @Autowired
    TrajetRepository trajetRepository;
    @Autowired
    PositionRepository positionRepository;
    @Autowired
    ConvertorService convertorService;
    public List<TrajetDTO> getAllDTOByUserAndYearMonth(User user, YearMonth yearMonth) {
        return getAllByUserAndYearMonth(user, yearMonth)
                .stream().map(convertorService::convertToTrajetDTO)
                .toList();
    }
    public List<Trajet> getAllByUserAndYearMonth(User user, YearMonth yearMonth) {
        return trajetRepository.findAllByUserIdAndYearMonth(user.getId(), yearMonth.getYear(), yearMonth.getMonthValue());
    }
    public void createTrajet(User user,TrajetDTO trajetDTO) {
        trajetRepository.save(
                new Trajet(
                        trajetDTO.getTitre(),
                        trajetDTO.getNbkm(),
                        trajetDTO.getDateTrajet(),
                        trajetDTO.getDepart().getDisplayed(),
                        trajetDTO.getDepart().getLat(),
                        trajetDTO.getDepart().getLon(),
                        trajetDTO.getArrive().getDisplayed(),
                        trajetDTO.getArrive().getLat(),
                        trajetDTO.getArrive().getLon(),
                        user
                )
        );
    }
    public void updateTrajet(User user,TrajetDTO trajetDTO) {
        Trajet trajet = getTrajetById(trajetDTO.getId());
        if(!trajet.getUser().getId().equals(user.getId())){
            throw new ChronoshareException("Vous n'avez pas les droits");
        }
        trajet.setDateTrajet(trajetDTO.getDateTrajet());
        trajet.setTitre(trajetDTO.getTitre());
        trajet.setNbkm(trajetDTO.getNbkm());
        trajet.setDisplayedArrive(trajetDTO.getArrive().getDisplayed());
        trajet.setLatArrive(trajetDTO.getArrive().getLat());
        trajet.setLonArrive(trajetDTO.getArrive().getLon());
        trajet.setDisplayedDepart(trajetDTO.getDepart().getDisplayed());
        trajet.setLatDepart(trajetDTO.getDepart().getLat());
        trajet.setLonDepart(trajetDTO.getDepart().getLon());
        trajetRepository.save(trajet);
    }

    public void deleteById(Integer id, User user){
        Trajet trajet = getTrajetById(id);
        if(!trajet.getUser().getId().equals(user.getId())){
            throw new ChronoshareException("Vous n'avez pas les droits");
        }
        trajet.setUser(null);
        trajetRepository.delete(trajet);
    }

    public Trajet getTrajetById(Integer id){
        Optional<Trajet>  optTrajet = trajetRepository.findById(id);
       if(optTrajet.isEmpty()){
           throw new ChronoshareException("Aucun trajet avec cet id");
       }
       return optTrajet.get();
    }
}
