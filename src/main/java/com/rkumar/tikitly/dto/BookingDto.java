package com.rkumar.tikitly.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private String bookingNumber;
    private LocalDateTime bookingDateTime;
    private UserDto user;
    private ShowDto show;
    private String status;
    private double totalAmount;
    private List<ShowSeatDto> seats;
    private PaymentDto payment;

}
