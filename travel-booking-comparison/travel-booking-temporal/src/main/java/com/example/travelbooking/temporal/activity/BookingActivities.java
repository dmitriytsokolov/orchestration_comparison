package com.example.travelbooking.temporal.activity;

import java.util.UUID;

import com.example.travelbooking.domain.dto.ModifyBookingRequest;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface BookingActivities {
  void confirmBooking(UUID bookingId);

  void cancelBooking(UUID bookingId, String reason);

  void modifyBooking(UUID bookingId, ModifyBookingRequest request);

  void releaseReservations(UUID bookingId);
}
