package com.rkumar.tikitly.services;

import com.rkumar.tikitly.dto.*;
import com.rkumar.tikitly.exceptions.ResourceNotFoundException;
import com.rkumar.tikitly.exceptions.SeatUnavailableException;
import com.rkumar.tikitly.models.*;
import com.rkumar.tikitly.repositories.BookingRepository;
import com.rkumar.tikitly.repositories.ShowRepository;
import com.rkumar.tikitly.repositories.ShowSeatRepository;
import com.rkumar.tikitly.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public BookingDto createBooking(BookingRequestDto bookingRequestDto) {
        User user = userRepository.findById(bookingRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + bookingRequestDto.getUserId()));

        Show show = showRepository.findById(bookingRequestDto.getShowId())
                .orElseThrow(()-> new ResourceNotFoundException("Show not found with ID: " + bookingRequestDto.getShowId()));

        List<ShowSeat> selectedSeats = showSeatRepository.findAllById(bookingRequestDto.getSeatIds());

        for(ShowSeat seat : selectedSeats) {
            if (!"AVAILABLE".equalsIgnoreCase(seat.getStatus())) {
                throw new SeatUnavailableException("Seat " + seat.getSeat().getSeatNumber() + " is not available");
            }

            seat.setStatus("LOCKED");
        }
            showSeatRepository.saveAll(selectedSeats);

            Double totalAmount = selectedSeats.stream()
                    .mapToDouble(ShowSeat::getPrice)
                    .sum();

            Payment payment = new Payment();
            payment.setAmount(totalAmount);
            payment.setPaymentDateTime(LocalDateTime.now());
            payment.setPaymentMethod(bookingRequestDto.getPaymentMethod());
            payment.setStatus("SUCCESS");   // Payment Gateway
            payment.setTransactionId(UUID.randomUUID().toString());

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setShow(show);
            booking.setBookingDateTime(LocalDateTime.now());
            booking.setStatus("CONFIRM");
            booking.setBookingNumber(UUID.randomUUID().toString());
            booking.setTotalAmount(totalAmount);
            booking.setPayment(payment);

            Booking savedBooking = bookingRepository.save(booking);

            selectedSeats.forEach(seat -> {
                seat.setStatus("BOOKED");
                seat.setBooking(savedBooking);
            });

            showSeatRepository.saveAll(selectedSeats);
            return  mapToBookingDto(savedBooking, selectedSeats);
    }

    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat->seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking, seats);
    }

    public BookingDto getBookingsByNumber(String bookingNumber) {
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat->seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking, seats);
    }

    public List<BookingDto> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return bookings.stream()
                .map(booking -> {
                    List<ShowSeat> seats = showSeatRepository.findAll()
                            .stream()
                            .filter(seat->seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                            .collect(Collectors.toList());

                    return mapToBookingDto(booking, seats);

                })
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingDto cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found"));

        booking.setStatus("CANCCELED");

        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat->seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        seats.forEach(seat->{
            seat.setStatus("AVAILABLE");
            seat.setBooking(null);
        });

        if(booking.getPayment() != null) {
            booking.getPayment().setStatus("REFUNDED");
        }

        Booking updateBooking = bookingRepository.save(booking);
        showSeatRepository.saveAll(seats);

        return mapToBookingDto(updateBooking, seats);
    }


    private BookingDto mapToBookingDto(Booking booking, List<ShowSeat> showSeats) {
        // Booking
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookingNumber(booking.getBookingNumber());
        bookingDto.setBookingDateTime(booking.getBookingDateTime());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setTotalAmount(booking.getTotalAmount());

        // User
        UserDto userDto = new UserDto();
        userDto.setId(booking.getUser().getId());
        userDto.setName(booking.getUser().getName());
        userDto.setEmail(booking.getUser().getEmail());
        userDto.setPhoneNumber(booking.getUser().getPhoneNumber());
        bookingDto.setUser(userDto);

        // Show
        ShowDto showDto = new ShowDto();
        showDto.setId(booking.getShow().getId());
        showDto.setStartDateTime(booking.getShow().getStartDateTime());
        showDto.setEndDateTime(booking.getShow().getEndDateTime());
//        showDto.setMovie(mapToMovieDto(booking.getShow().getMovie()));

        // Movie
        MovieDto movieDto = new MovieDto();
        movieDto.setId(booking.getShow().getMovie().getId());
        movieDto.setTitle(booking.getShow().getMovie().getTitle());
        movieDto.setDescription(booking.getShow().getMovie().getDescription());
        movieDto.setLanguage(booking.getShow().getMovie().getLanguage());
        movieDto.setGenre(booking.getShow().getMovie().getGenre());
        movieDto.setDuration(booking.getShow().getMovie().getDurationMins());
        movieDto.setReleaseDate(booking.getShow().getMovie().getReleaseDate());
        movieDto.setPosterUrl(booking.getShow().getMovie().getPosterUrl());
        showDto.setMovie(movieDto);

        // Screen
        ScreenDto screenDto = new ScreenDto();
        screenDto.setId(booking.getShow().getScreen().getId());
        screenDto.setName(booking.getShow().getScreen().getName());
        screenDto.setTotalSeats(booking.getShow().getScreen().getTotalSeats());

        // Theater
        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(booking.getShow().getScreen().getTheater().getId());
        theaterDto.setName(booking.getShow().getScreen().getTheater().getName());
        theaterDto.setAddress(booking.getShow().getScreen().getTheater().getAddress());
        theaterDto.setCity(booking.getShow().getScreen().getTheater().getCity());
        theaterDto.setTotalScreens(booking.getShow().getScreen().getTheater().getTotalScreens());


        screenDto.setTheater(theaterDto);
        showDto.setScreen(screenDto);
        bookingDto.setShow(showDto);

        List<ShowSeatDto> showSeatDtos = showSeats.stream()
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
                }).collect(Collectors.toList());

        bookingDto.setSeats(showSeatDtos);

        if(booking.getPayment() != null) {
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setId(booking.getPayment().getId());
            paymentDto.setAmount(booking.getPayment().getAmount());
            paymentDto.setPaymentDateTime(booking.getPayment().getPaymentDateTime());
            paymentDto.setPaymentMethod(booking.getPayment().getPaymentMethod());
            paymentDto.setStatus(booking.getPayment().getStatus());
            paymentDto.setTransactionId(booking.getPayment().getTransactionId());

            bookingDto.setPayment(paymentDto);
        }


        return bookingDto;
    }
}
