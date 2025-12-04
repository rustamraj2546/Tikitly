package com.rkumar.tikitly.repository;

import com.rkumar.tikitly.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByLannguage(String language);

    List<Movie> findByGenre(String genre);

    List<Movie> findByReleaseDate(String releaseDate);

    Optional<Movie> findByTitleContainingIgnoreCase(String title);
}
