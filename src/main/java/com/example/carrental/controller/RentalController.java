package com.example.carrental.controller;

import com.example.carrental.dto.RentalRequest;
import com.example.carrental.dto.RentalResponse;
import com.example.carrental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<RentalResponse> createRental(
            @Valid @RequestBody RentalRequest request,
            Principal principal
    ) {
        return new ResponseEntity<>(rentalService.createRental(request, principal.getName()), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RentalResponse>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<List<RentalResponse>> getMyRentals(Principal principal) {
        return ResponseEntity.ok(rentalService.getMyRentals(principal.getName()));
    }
}
