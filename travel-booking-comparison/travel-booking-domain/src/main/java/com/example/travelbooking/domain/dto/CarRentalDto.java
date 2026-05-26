package com.example.travelbooking.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CarRentalDto(
    UUID id,
    String company,
    String model,
    String pickupLocation,
    String dropoffLocation,
    LocalDate pickupDate,
    LocalDate dropoffDate,
    boolean available,
    BigDecimal pricePerDay,
    String currency) {
}
