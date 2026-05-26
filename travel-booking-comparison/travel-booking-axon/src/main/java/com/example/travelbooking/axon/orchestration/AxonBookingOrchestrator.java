package com.example.travelbooking.axon.orchestration;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;

import com.example.travelbooking.core.service.BookingOrchestrator;
import com.example.travelbooking.domain.command.CancelBookingCommand;
import com.example.travelbooking.domain.command.CreateBookingCommand;
import com.example.travelbooking.domain.command.ModifyBookingCommand;
import com.example.travelbooking.domain.dto.CreateBookingRequest;
import com.example.travelbooking.domain.dto.ModifyBookingRequest;

@Component
public class AxonBookingOrchestrator implements BookingOrchestrator {
  private final CommandGateway commandGateway;

  public AxonBookingOrchestrator(final CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }

  @Override
  public String startBooking(final UUID bookingId, final CreateBookingRequest request) {
    commandGateway.send(new CreateBookingCommand(
        bookingId,
        request.userId(),
        request.flightId(),
        request.hotelId(),
        request.carRentalId(),
        request.idempotencyKey()));
    return bookingId.toString();
  }

  @Override
  public String startCancellation(final UUID bookingId) {
    commandGateway.send(new CancelBookingCommand(bookingId, "Customer requested cancellation"));
    return bookingId.toString();
  }

  @Override
  public String startModification(final UUID bookingId, final ModifyBookingRequest request) {
    commandGateway.send(new ModifyBookingCommand(
        bookingId,
        request.flightId(),
        request.hotelId(),
        request.carRentalId()));
    return bookingId.toString();
  }
}
