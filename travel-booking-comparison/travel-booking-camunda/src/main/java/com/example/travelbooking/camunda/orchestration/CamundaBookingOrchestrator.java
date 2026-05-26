package com.example.travelbooking.camunda.orchestration;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.travelbooking.core.service.BookingOrchestrator;
import com.example.travelbooking.domain.dto.CreateBookingRequest;
import com.example.travelbooking.domain.dto.ModifyBookingRequest;

import io.camunda.zeebe.client.ZeebeClient;

@Component
public class CamundaBookingOrchestrator implements BookingOrchestrator {
  private final ZeebeClient zeebeClient;

  public CamundaBookingOrchestrator(final ZeebeClient zeebeClient) {
    this.zeebeClient = zeebeClient;
  }

  @Override
  public String startBooking(final UUID bookingId, final CreateBookingRequest request) {
    final var variables = Map.of(
        "bookingId", bookingId.toString(),
        "flightId", request.flightId().toString(),
        "hotelId", request.hotelId().toString(),
        "carRentalId", request.carRentalId().toString(),
        "idempotencyKey", request.idempotencyKey());
    final var event = zeebeClient.newCreateInstanceCommand()
        .bpmnProcessId("trip-booking")
        .latestVersion()
        .variables(variables)
        .send()
        .join();
    return String.valueOf(event.getProcessInstanceKey());
  }

  @Override
  public String startCancellation(final UUID bookingId) {
    final var event = zeebeClient.newCreateInstanceCommand()
        .bpmnProcessId("booking-cancellation")
        .latestVersion()
        .variables(Map.of("bookingId", bookingId.toString()))
        .send()
        .join();
    return String.valueOf(event.getProcessInstanceKey());
  }

  @Override
  public String startModification(final UUID bookingId, final ModifyBookingRequest request) {
    final var variables = Map.of(
        "bookingId", bookingId.toString(),
        "flightId", request.flightId().toString(),
        "hotelId", request.hotelId().toString(),
        "carRentalId", request.carRentalId().toString());
    final var event = zeebeClient.newCreateInstanceCommand()
        .bpmnProcessId("booking-modification")
        .latestVersion()
        .variables(variables)
        .send()
        .join();
    return String.valueOf(event.getProcessInstanceKey());
  }
}
