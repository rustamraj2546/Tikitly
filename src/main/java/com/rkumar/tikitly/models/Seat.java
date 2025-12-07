package com.rkumar.tikitly.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private String seatType;  // Regular, Premium, Balcony

    @Column(nullable = false)
    private Double basePrice;

    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;


}
