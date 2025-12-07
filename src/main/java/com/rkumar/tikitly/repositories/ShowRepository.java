package com.rkumar.tikitly.repositories;

import com.rkumar.tikitly.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovieId(Long movieId);

    List<Show> findByScreenId(Long screenId);

    List<Show> findByStartDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Show> findByMovieIdAndScreen_Theater_City(Long movieId, String city);

    List<Show> findByMovie_IdAndScreen_Theater_City(Long movieId, String city);
}
