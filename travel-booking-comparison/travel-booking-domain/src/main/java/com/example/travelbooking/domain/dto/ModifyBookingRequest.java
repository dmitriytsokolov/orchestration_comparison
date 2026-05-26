package com.example.travelbooking.domain.dto;

import java.util.UUID;

public record ModifyBookingRequest(UUID flightId, UUID hotelId, UUID carRentalId) {
}
