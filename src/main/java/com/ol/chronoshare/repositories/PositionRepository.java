package com.ol.chronoshare.repositories;

import com.ol.chronoshare.model.Position;
import com.ol.chronoshare.model.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository  extends JpaRepository<Position, Integer> {
}
