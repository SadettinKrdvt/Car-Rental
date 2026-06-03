package com.example.carrental.repository;

import com.example.carrental.entity.Car;
import com.example.carrental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserEmail(String email);
    boolean existsByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Car car, LocalDate endDate, LocalDate startDate);
}
