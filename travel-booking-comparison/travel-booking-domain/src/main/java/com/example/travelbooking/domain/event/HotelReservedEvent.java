package com.example.travelbooking.domain.event;

import java.util.UUID;

public record HotelReservedEvent(UUID hotelId, UUID bookingId) {
}
