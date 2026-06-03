package com.example.carrental.service;

import com.example.carrental.dto.CarRequest;
import com.example.carrental.dto.CarResponse;
import com.example.carrental.entity.Car;
import com.example.carrental.exception.DuplicateResourceException;
import com.example.carrental.exception.ResourceNotFoundException;
import com.example.carrental.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarResponse addCar(CarRequest request) {
        if (carRepository.existsByPlateNumber(request.getPlateNumber())) {
            throw new DuplicateResourceException("Bu plaka numarası ile kayıtlı bir araç zaten mevcut.");
        }

        Car car = Car.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .plateNumber(request.getPlateNumber())
                .dailyPrice(request.getDailyPrice())
                .available(true)
                .build();

        Car savedCar = carRepository.save(car);
        return mapToResponse(savedCar);
    }

    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CarResponse> getAvailableCars() {
        return carRepository.findByAvailableTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CarResponse> getCarsByBrand(String brand) {
        return carRepository.findByBrandIgnoreCase(brand).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CarResponse getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Araç bulunamadı."));
        return mapToResponse(car);
    }

    public CarResponse updateCar(Long id, CarRequest request) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Araç bulunamadı."));

        if (!car.getPlateNumber().equals(request.getPlateNumber()) &&
                carRepository.existsByPlateNumber(request.getPlateNumber())) {
            throw new DuplicateResourceException("Bu plaka numarası ile kayıtlı başka bir araç zaten mevcut.");
        }

        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setPlateNumber(request.getPlateNumber());
        car.setDailyPrice(request.getDailyPrice());

        Car updatedCar = carRepository.save(car);
        return mapToResponse(updatedCar);
    }

    public void deleteCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Araç bulunamadı."));
        carRepository.delete(car);
    }

    private CarResponse mapToResponse(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .plateNumber(car.getPlateNumber())
                .dailyPrice(car.getDailyPrice())
                .available(car.isAvailable())
                .build();
    }
}
