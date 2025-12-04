package com.rkumar.tikitly.repository;

import com.rkumar.tikitly.model.Booking;
import com.rkumar.tikitly.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    List<Theater> findByCity(String city);

    Optional<Theater> findByName(String name);

    List<Theater> findByShowId(Long showId);
}
