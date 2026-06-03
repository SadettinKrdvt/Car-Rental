package com.example.carrental.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarResponse {
    private Long id;
    private String brand;
    private String model;
    private String plateNumber;
    private BigDecimal dailyPrice;
    private boolean available;
}
