package com.ol.chronoshare.repositories;

import com.ol.chronoshare.model.Image;
import com.ol.chronoshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findAllByUser(User user);
}
