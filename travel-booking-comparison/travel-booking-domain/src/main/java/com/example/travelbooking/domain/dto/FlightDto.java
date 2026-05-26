package com.example.travelbooking.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FlightDto(
    UUID id,
    String flightNumber,
    String origin,
    String destination,
    LocalDateTime departureTime,
    LocalDateTime arrivalTime,
    int availableSeats,
    BigDecimal pricePerSeat,
    String currency) {
}
