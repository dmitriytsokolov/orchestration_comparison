package com.example.travelbooking.axon.aggregate;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.travelbooking.domain.command.ReleaseCarRentalCommand;
import com.example.travelbooking.domain.command.ReserveCarRentalCommand;
import com.example.travelbooking.domain.event.CarReservedEvent;

@Aggregate
public class CarRentalAggregate {
  @AggregateIdentifier
  private UUID carRentalId;

  protected CarRentalAggregate() {
  }

  @CommandHandler
  public CarRentalAggregate(final ReserveCarRentalCommand command) {
    AggregateLifecycle.apply(new CarReservedEvent(command.carRentalId(), command.bookingId()));
  }

  @CommandHandler
  public void handle(final ReleaseCarRentalCommand command) {
    AggregateLifecycle.apply(new CarReservedEvent(command.carRentalId(), command.bookingId()));
  }

  @EventSourcingHandler
  public void on(final CarReservedEvent event) {
    carRentalId = event.carRentalId();
  }
}
