package com.example.carrental.service;

import com.example.carrental.dto.RentalRequest;
import com.example.carrental.dto.RentalResponse;
import com.example.carrental.entity.Car;
import com.example.carrental.entity.Rental;
import com.example.carrental.entity.User;
import com.example.carrental.exception.CarNotAvailableException;
import com.example.carrental.exception.InvalidRentalDateException;
import com.example.carrental.exception.ResourceNotFoundException;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.RentalRepository;
import com.example.carrental.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public RentalService(
            RentalRepository rentalRepository,
            CarRepository carRepository,
            UserRepository userRepository
    ) {
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RentalResponse createRental(RentalRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı."));

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Kiralanmak istenen araç bulunamadı."));

        if (!car.isAvailable()) {
            throw new CarNotAvailableException("Seçilen araç şu anda kiralama dışıdır veya hasarlıdır.");
        }

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidRentalDateException("Kiralama başlangıç tarihi geçmiş bir tarih olamaz.");
        }

        if (endDate.isBefore(startDate)) {
            throw new InvalidRentalDateException("Kiralama bitiş tarihi başlangıç tarihinden önce olamaz.");
        }

        boolean isOverlapping = rentalRepository.existsByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                car, endDate, startDate
        );

        if (isOverlapping) {
            throw new CarNotAvailableException("Seçilen araç belirtilen tarih aralığında zaten kiralanmış durumdadır.");
        }

        long rentalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        BigDecimal totalPrice = car.getDailyPrice().multiply(BigDecimal.valueOf(rentalDays));

        Rental rental = Rental.builder()
                .user(user)
                .car(car)
                .startDate(startDate)
                .endDate(endDate)
                .totalPrice(totalPrice)
                .build();

        if (startDate.equals(LocalDate.now()) || startDate.isBefore(LocalDate.now().plusDays(1))) {
            car.setAvailable(false);
            carRepository.save(car);
        }

        Rental savedRental = rentalRepository.save(rental);
        return mapToResponse(savedRental);
    }

    public List<RentalResponse> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RentalResponse> getMyRentals(String userEmail) {
        return rentalRepository.findByUserEmail(userEmail).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RentalResponse mapToResponse(Rental rental) {
        return RentalResponse.builder()
                .id(rental.getId())
                .carId(rental.getCar().getId())
                .carBrand(rental.getCar().getBrand())
                .carModel(rental.getCar().getModel())
                .userEmail(rental.getUser().getEmail())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .totalPrice(rental.getTotalPrice())
                .build();
    }
}
