package com.rkumar.tikitly.repositories;

import com.rkumar.tikitly.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByLanguage(String language);

    List<Movie> findByGenre(String genre);

    List<Movie> findByReleaseDate(LocalDate releaseDate);

    Optional<Movie> findByTitleContainingIgnoreCase(String title);
}
