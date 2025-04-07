package com.ol.chronoshare.repositories;

import com.ol.chronoshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer> {

    public Optional<User> findByEmail(String email);
    public Optional<User> findUserByTokenEnabled(String tokenEnabled);
}
