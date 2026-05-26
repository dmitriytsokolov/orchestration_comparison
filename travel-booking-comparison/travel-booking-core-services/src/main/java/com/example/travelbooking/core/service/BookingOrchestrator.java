package com.example.travelbooking.core.service;

import java.util.UUID;

import com.example.travelbooking.domain.dto.CreateBookingRequest;
import com.example.travelbooking.domain.dto.ModifyBookingRequest;

public interface BookingOrchestrator {
  String startBooking(UUID bookingId, CreateBookingRequest request);

  String startCancellation(UUID bookingId);

  String startModification(UUID bookingId, ModifyBookingRequest request);
}
