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
public class PaymentDto {
    private Long id;
    private String transactionId;
    private Double amount;
    private LocalDateTime paymentDateTime;
    private String paymentMethod;
    private String status;
}
