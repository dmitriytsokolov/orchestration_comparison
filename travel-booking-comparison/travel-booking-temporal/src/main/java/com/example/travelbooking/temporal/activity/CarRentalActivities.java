package com.example.travelbooking.temporal.activity;

import java.util.UUID;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface CarRentalActivities {
  void reserveCarRental(UUID carRentalId, UUID bookingId);

  void releaseCarRental(UUID carRentalId, UUID bookingId);
}
