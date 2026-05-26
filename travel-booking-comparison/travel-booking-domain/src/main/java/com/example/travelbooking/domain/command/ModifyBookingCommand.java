package com.example.travelbooking.domain.command;

import java.util.UUID;

public record ModifyBookingCommand(
    UUID bookingId,
    UUID flightId,
    UUID hotelId,
    UUID carRentalId) {
}
