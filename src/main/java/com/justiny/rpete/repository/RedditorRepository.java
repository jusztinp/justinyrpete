package com.justiny.rpete.repository;

import com.justiny.rpete.model.Redditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RedditorRepository extends JpaRepository<Redditor, Long> {
    Optional<Redditor> findByUsername(String username);
}