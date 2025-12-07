package com.rkumar.tikitly.services;

import com.rkumar.tikitly.dto.MovieDto;
import com.rkumar.tikitly.exceptions.ResourceNotFoundException;
import com.rkumar.tikitly.models.Movie;
import com.rkumar.tikitly.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final ModelMapper modelMapper;
    private final MovieRepository movieRepository;


    public MovieDto createMovie(MovieDto movieDto) {
        Movie movie = mapToEntity(movieDto);
        Movie savedMovie = movieRepository.save(movie);
        return mapToMovieDto(savedMovie);
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        return mapToMovieDto(movie);
    }

    public List<MovieDto> getMovieByLanguage(String language) {
        List<Movie> movies = movieRepository.findByLanguage(language);

        return movies.stream()
                .map(this::mapToMovieDto)
                .toList();
    }

    public List<MovieDto> getMovieByGenre(String genre) {
        List<Movie> movies = movieRepository.findByGenre(genre);

        return movies.stream()
                .map(this::mapToMovieDto)
                .toList();
    }

    public MovieDto getMovieByTitle(String title) {
        Movie movie = movieRepository.findByTitleContainingIgnoreCase(title)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with title: " + title));

        return mapToMovieDto(movie);
    }

    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(this::mapToMovieDto)
                .toList();
    }


    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMins(movieDto.getDuration());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());

        Movie updatedMovie = movieRepository.save(movie);
        return mapToMovieDto(updatedMovie);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        movieRepository.delete(movie);
    }

    public MovieDto mapToMovieDto(Movie movie) {
        return modelMapper.map(movie, MovieDto.class);
    }

    public Movie mapToEntity(MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMins(movieDto.getDuration());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());

        return movie;
    }
}
