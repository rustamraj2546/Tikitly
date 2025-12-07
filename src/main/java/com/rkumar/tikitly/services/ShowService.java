package com.rkumar.tikitly.services;

import com.rkumar.tikitly.dto.*;
import com.rkumar.tikitly.exceptions.ResourceNotFoundException;
import com.rkumar.tikitly.models.Movie;
import com.rkumar.tikitly.models.Screen;
import com.rkumar.tikitly.models.Show;
import com.rkumar.tikitly.models.ShowSeat;
import com.rkumar.tikitly.repositories.MovieRepository;
import com.rkumar.tikitly.repositories.ScreenRepository;
import com.rkumar.tikitly.repositories.ShowRepository;
import com.rkumar.tikitly.repositories.ShowSeatRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowService {
    private final ModelMapper modelMapper;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;

    public ShowDto createShow(ShowDto showDto) {
        Show show = new Show();
        Movie movie = movieRepository.findById(showDto.getMovie().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Screen screen = screenRepository.findById(showDto.getScreen().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        show.setMovie(movie);
        show.setScreen(screen);

        show.setStartDateTime(showDto.getStartDateTime());
        show.setEndDateTime(showDto.getEndDateTime());

        Show savedShow = showRepository.save(show);

        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(savedShow.getId(), "AVAILABLE");

        return mapToDto(savedShow, availableSeats);
    }

    public ShowDto getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");

        return mapToDto(show, availableSeats);
    }

    public List<ShowDto> getAllShows() {
        List<Show> shows = showRepository.findAll();
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, availableSeats);
                }).toList();
    }

    public List<ShowDto> getShowsByMovie(Long movieId) {
        List<Show> shows = showRepository.findByMovieId(movieId);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, availableSeats);
                }).toList();
    }

    public List<ShowDto> getShowsByMovieAndCity(Long movieId, String city) {
        List<Show> shows = showRepository.findByMovie_IdAndScreen_Theater_City(movieId, city);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, availableSeats);
                }).toList();
    }

    public List<ShowDto> getShowsByDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Show> shows = showRepository.findByStartDateTimeBetween(fromDate, toDate);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, availableSeats);
                }).toList();
    }

    private ShowDto mapToDto(Show show, List<ShowSeat> availableSeats) {
        ShowDto showDto = modelMapper.map(show, ShowDto.class);
        MovieDto movieDto = modelMapper.map(show.getMovie(), MovieDto.class);
        TheaterDto theaterDto = modelMapper.map(show.getScreen().getTheater(), TheaterDto.class);
        ScreenDto screenDto = modelMapper.map(show.getScreen(), ScreenDto.class);

        screenDto.setTheater(theaterDto);
        showDto.setMovie(movieDto);
        showDto.setScreen(screenDto);

        List<ShowSeatDto> seatDtos = availableSeats.stream()
                .map(seat->{
                    ShowSeatDto showSeatDto = new ShowSeatDto();
                    showSeatDto.setId(seat.getId());
                    showSeatDto.setStatus(seat.getStatus());
                    showSeatDto.setPrice(seat.getPrice());

                    SeatDto seatDto = new SeatDto();
                    seatDto.setId(seat.getSeat().getId());
                    seatDto.setSeatNumber(seat.getSeat().getSeatNumber());
                    seatDto.setSeatType(seat.getSeat().getSeatType());
                    seatDto.setBasePrice(seat.getSeat().getBasePrice());

                    showSeatDto.setSeat(seatDto);
                    return showSeatDto;
                }).toList();

        showDto.setAvailableSeats(seatDtos);

        return showDto;
    }
}
