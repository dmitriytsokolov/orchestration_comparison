package com.example.travelbooking.temporal.activity;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.travelbooking.core.service.NotificationService;

@Component
public class NotificationActivitiesImpl implements NotificationActivities {
  private final NotificationService notificationService;

  public NotificationActivitiesImpl(final NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @Override
  public void sendConfirmation(final UUID bookingId) {
    notificationService.sendConfirmation(bookingId);
  }

  @Override
  public void sendCancellationConfirmation(final UUID bookingId) {
    notificationService.sendCancellationConfirmation(bookingId);
  }
}
