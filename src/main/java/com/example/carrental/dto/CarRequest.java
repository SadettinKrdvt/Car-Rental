package com.example.carrental.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRequest {

    @NotBlank(message = "Marka alanı boş bırakılamaz.")
    private String brand;

    @NotBlank(message = "Model alanı boş bırakılamaz.")
    private String model;

    @NotBlank(message = "Plaka alanı boş bırakılamaz.")
    private String plateNumber;

    @NotNull(message = "Günlük kiralama ücreti boş bırakılamaz.")
    @DecimalMin(value = "0.01", message = "Günlük kiralama ücreti sıfırdan büyük olmalıdır.")
    private BigDecimal dailyPrice;
}
