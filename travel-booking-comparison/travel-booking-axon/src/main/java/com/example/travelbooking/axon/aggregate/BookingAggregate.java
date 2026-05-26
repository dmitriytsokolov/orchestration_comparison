package com.example.travelbooking.axon.aggregate;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.travelbooking.domain.command.CancelBookingCommand;
import com.example.travelbooking.domain.command.ConfirmBookingCommand;
import com.example.travelbooking.domain.command.CreateBookingCommand;
import com.example.travelbooking.domain.command.ModifyBookingCommand;
import com.example.travelbooking.domain.entity.BookingStatus;
import com.example.travelbooking.domain.event.BookingCancellationInitiatedEvent;
import com.example.travelbooking.domain.event.BookingCancelledEvent;
import com.example.travelbooking.domain.event.BookingConfirmedEvent;
import com.example.travelbooking.domain.event.BookingCreatedEvent;

@Aggregate
public class BookingAggregate {
  @AggregateIdentifier
  private UUID bookingId;
  private BookingStatus status;

  protected BookingAggregate() {
  }

  @CommandHandler
  public BookingAggregate(final CreateBookingCommand command) {
    AggregateLifecycle.apply(new BookingCreatedEvent(
        command.bookingId(),
        command.userId(),
        command.flightId(),
        command.hotelId(),
        command.carRentalId()));
  }

  @CommandHandler
  public void handle(final ConfirmBookingCommand command) {
    AggregateLifecycle.apply(new BookingConfirmedEvent(command.bookingId()));
  }

  @CommandHandler
  public void handle(final CancelBookingCommand command) {
    AggregateLifecycle.apply(new BookingCancellationInitiatedEvent(command.bookingId(), command.reason()));
    AggregateLifecycle.apply(new BookingCancelledEvent(command.bookingId(), command.reason()));
  }

  @CommandHandler
  public void handle(final ModifyBookingCommand command) {
    status = BookingStatus.MODIFICATION_PENDING;
  }

  @EventSourcingHandler
  public void on(final BookingCreatedEvent event) {
    bookingId = event.bookingId();
    status = BookingStatus.PENDING;
  }

  @EventSourcingHandler
  public void on(final BookingConfirmedEvent event) {
    status = BookingStatus.CONFIRMED;
  }

  @EventSourcingHandler
  public void on(final BookingCancelledEvent event) {
    status = BookingStatus.CANCELLED;
  }
}
