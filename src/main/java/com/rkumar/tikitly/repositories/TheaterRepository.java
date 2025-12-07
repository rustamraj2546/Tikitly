package com.rkumar.tikitly.repositories;

import com.rkumar.tikitly.models.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    List<Theater> findByCity(String city);

    Optional<Theater> findByName(String name);
}
