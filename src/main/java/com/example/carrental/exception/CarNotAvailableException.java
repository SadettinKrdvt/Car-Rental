package com.example.carrental.exception;

import org.springframework.http.HttpStatus;

public class CarNotAvailableException extends CustomException {
    public CarNotAvailableException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
