package com.example.carrental.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalResponse {
    private Long id;
    private Long carId;
    private String carBrand;
    private String carModel;
    private String userEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
}
