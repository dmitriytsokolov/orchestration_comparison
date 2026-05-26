package com.example.travelbooking.domain.dto;

import java.time.LocalDate;

public record SearchCarRentalsRequest(
    String pickupLocation,
    LocalDate pickupDate,
    LocalDate dropoffDate) {
}
