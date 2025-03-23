package com.ol.chronoshare.repositories;

import com.ol.chronoshare.model.Image;
import com.ol.chronoshare.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query(value = "SELECT * FROM tickets WHERE user_id = :userid AND YEAR(date_ticket) = :year AND MONTH(date_ticket) = :month ORDER BY date_ticket ASC", nativeQuery = true)
    List<Ticket> findAllByUserIdAndYearMonth(Integer userid, Integer year, Integer month );
}
