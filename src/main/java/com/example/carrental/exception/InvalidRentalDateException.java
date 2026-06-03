package com.example.carrental.exception;

import org.springframework.http.HttpStatus;

public class InvalidRentalDateException extends CustomException {
    public InvalidRentalDateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
