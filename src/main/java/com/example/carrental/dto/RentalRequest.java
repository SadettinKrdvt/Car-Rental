package com.example.carrental.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalRequest {

    @NotNull(message = "Araç seçimi zorunludur.")
    private Long carId;

    @NotNull(message = "Başlangıç tarihi zorunludur.")
    private LocalDate startDate;

    @NotNull(message = "Bitiş tarihi zorunludur.")
    private LocalDate endDate;
}
