package com.rkumar.tikitly.repository;

import com.rkumar.tikitly.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovieId(Long movieId);

    List<Show> findByScreenId(Long screenId);

    List<Show> findByStartDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Show> findByMovieIdAndScreen_Theater_City(Long movieId, String city);


}
