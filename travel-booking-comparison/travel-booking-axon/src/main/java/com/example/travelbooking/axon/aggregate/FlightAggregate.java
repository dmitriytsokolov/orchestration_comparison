package com.example.travelbooking.axon.aggregate;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.travelbooking.domain.command.ReleaseFlightCommand;
import com.example.travelbooking.domain.command.ReserveFlightCommand;
import com.example.travelbooking.domain.event.FlightReservedEvent;

@Aggregate
public class FlightAggregate {
  @AggregateIdentifier
  private UUID flightId;

  protected FlightAggregate() {
  }

  @CommandHandler
  public FlightAggregate(final ReserveFlightCommand command) {
    AggregateLifecycle.apply(new FlightReservedEvent(command.flightId(), command.bookingId()));
  }

  @CommandHandler
  public void handle(final ReleaseFlightCommand command) {
    AggregateLifecycle.apply(new FlightReservedEvent(command.flightId(), command.bookingId()));
  }

  @EventSourcingHandler
  public void on(final FlightReservedEvent event) {
    flightId = event.flightId();
  }
}
