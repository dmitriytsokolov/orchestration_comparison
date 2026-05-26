package com.example.travelbooking.axon.aggregate;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.travelbooking.domain.command.ReleaseHotelCommand;
import com.example.travelbooking.domain.command.ReserveHotelCommand;
import com.example.travelbooking.domain.event.HotelReservedEvent;

@Aggregate
public class HotelAggregate {
  @AggregateIdentifier
  private UUID hotelId;

  protected HotelAggregate() {
  }

  @CommandHandler
  public HotelAggregate(final ReserveHotelCommand command) {
    AggregateLifecycle.apply(new HotelReservedEvent(command.hotelId(), command.bookingId()));
  }

  @CommandHandler
  public void handle(final ReleaseHotelCommand command) {
    AggregateLifecycle.apply(new HotelReservedEvent(command.hotelId(), command.bookingId()));
  }

  @EventSourcingHandler
  public void on(final HotelReservedEvent event) {
    hotelId = event.hotelId();
  }
}
