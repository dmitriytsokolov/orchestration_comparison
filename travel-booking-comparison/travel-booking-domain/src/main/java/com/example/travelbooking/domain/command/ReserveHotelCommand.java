package com.example.travelbooking.domain.command;

import java.util.UUID;

public record ReserveHotelCommand(UUID hotelId, UUID bookingId) {
}
