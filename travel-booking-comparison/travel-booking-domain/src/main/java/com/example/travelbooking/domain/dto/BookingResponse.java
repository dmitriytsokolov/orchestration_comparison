package com.example.travelbooking.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.travelbooking.domain.entity.BookingStatus;

public record BookingResponse(
    UUID id,
    UUID userId,
    BookingStatus status,
    UUID flightId,
    UUID hotelId,
    UUID carRentalId,
    BigDecimal totalAmount,
    String orchestrationId) {
}
