package com.example.travelbooking.domain.command;

import java.util.UUID;

public record ReleaseHotelCommand(UUID hotelId, UUID bookingId) {
}
