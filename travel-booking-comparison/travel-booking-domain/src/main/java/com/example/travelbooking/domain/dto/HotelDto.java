package com.example.travelbooking.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record HotelDto(
    UUID id,
    String name,
    String city,
    String address,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    int availableRooms,
    BigDecimal pricePerNight,
    String currency) {
}
