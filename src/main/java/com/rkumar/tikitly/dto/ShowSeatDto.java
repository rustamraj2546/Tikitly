package com.rkumar.tikitly.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeatDto {
    private Long id;
    private SeatDto seat;
    private String status;
    private Double price;
}
