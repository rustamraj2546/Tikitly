package com.rkumar.tikitly.services;

import com.rkumar.tikitly.dto.TheaterDto;
import com.rkumar.tikitly.exceptions.ResourceNotFoundException;
import com.rkumar.tikitly.models.Theater;
import com.rkumar.tikitly.repositories.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterService {
    private final ModelMapper modelMapper;
    private final TheaterRepository theaterRepository;


    public TheaterDto createTheater(TheaterDto theaterDto) {
        Theater theater = mapToEntity(theaterDto);

        Theater savedTheater = theaterRepository.save(theater);
        return mapToTheaterDto(savedTheater);
    }

    public TheaterDto getTheaterById(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + id));

        return mapToTheaterDto(theater);
    }

    public List<TheaterDto> getAllTheaters() {
        List<Theater> theaters = theaterRepository.findAll();
        return theaters.stream()
                .map(this::mapToTheaterDto)
                .toList();

    }

    public List<TheaterDto> getAllTheaterByCity(String city) {
        List<Theater> theaters = theaterRepository.findByCity(city);
        return theaters.stream()
                .map(this::mapToTheaterDto)
                .toList();
    }

    public TheaterDto updateTheater(Long id, TheaterDto theaterDto) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + id));
        return null; // implement
    }

    public void deleteTheater(Long id) {
        // Implement
    }



    private Theater mapToEntity(TheaterDto theaterDto) {
        return modelMapper.map(theaterDto, Theater.class);
    }

    private TheaterDto mapToTheaterDto(Theater theater) {
        return modelMapper.map(theater, TheaterDto.class);
    }

}
