package com.example.travelbooking.temporal.activity;

import java.util.UUID;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface HotelActivities {
  void reserveHotel(UUID hotelId, UUID bookingId);

  void releaseHotel(UUID hotelId, UUID bookingId);
}
