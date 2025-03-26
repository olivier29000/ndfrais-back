package com.ol.chronoshare.repositories;

import com.ol.chronoshare.model.Ticket;
import com.ol.chronoshare.model.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrajetRepository  extends JpaRepository<Trajet, Integer> {
    @Query(value = "SELECT * FROM trajets WHERE user_id = :userid AND YEAR(date_trajet) = :year AND MONTH(date_trajet) = :month ORDER BY date_trajet ASC", nativeQuery = true)
    List<Trajet> findAllByUserIdAndYearMonth(Integer userid, Integer year, Integer month );

}
