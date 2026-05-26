package com.example.travelbooking.domain.event;

import java.util.UUID;

public record HotelReservationFailedEvent(UUID hotelId, UUID bookingId, String reason) {
}
