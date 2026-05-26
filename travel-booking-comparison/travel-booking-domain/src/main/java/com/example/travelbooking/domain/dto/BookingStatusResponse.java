package com.example.travelbooking.domain.dto;

import java.util.UUID;

import com.example.travelbooking.domain.entity.BookingStatus;

public record BookingStatusResponse(UUID id, BookingStatus status, String orchestrationId) {
}
