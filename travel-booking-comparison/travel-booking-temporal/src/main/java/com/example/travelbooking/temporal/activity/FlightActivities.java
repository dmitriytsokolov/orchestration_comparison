package com.example.travelbooking.temporal.activity;

import java.util.UUID;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface FlightActivities {
  void reserveFlight(UUID flightId, UUID bookingId);

  void releaseFlight(UUID flightId, UUID bookingId);
}
