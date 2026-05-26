package com.example.travelbooking.temporal.activity;

import java.util.UUID;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface NotificationActivities {
  void sendConfirmation(UUID bookingId);

  void sendCancellationConfirmation(UUID bookingId);
}
