package com.example.travelbooking.axon.saga;

import java.math.BigDecimal;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.travelbooking.core.service.BookingService;
import com.example.travelbooking.core.service.InventoryService;
import com.example.travelbooking.core.service.NotificationService;
import com.example.travelbooking.core.service.PaymentService;
import com.example.travelbooking.domain.command.CancelBookingCommand;
import com.example.travelbooking.domain.command.ConfirmBookingCommand;
import com.example.travelbooking.domain.command.ReserveFlightCommand;
import com.example.travelbooking.domain.event.BookingConfirmedEvent;
import com.example.travelbooking.domain.event.BookingCreatedEvent;

@Saga
public class TripBookingSaga {
  private static final String CURRENCY = "USD";
  private static final BigDecimal TOTAL_AMOUNT = new BigDecimal("300.00");

  @Autowired
  private transient CommandGateway commandGateway;
  @Autowired
  private transient InventoryService inventoryService;
  @Autowired
  private transient PaymentService paymentService;
  @Autowired
  private transient BookingService bookingService;
  @Autowired
  private transient NotificationService notificationService;

  private UUID bookingId;
  private UUID flightId;
  private UUID hotelId;
  private UUID carRentalId;

  @StartSaga
  @SagaEventHandler(associationProperty = "bookingId")
  public void on(final BookingCreatedEvent event) {
    bookingId = event.bookingId();
    flightId = event.flightId();
    hotelId = event.hotelId();
    carRentalId = event.carRentalId();
    SagaLifecycle.associateWith("flightId", flightId.toString());
    commandGateway.send(new ReserveFlightCommand(flightId, bookingId));
    try {
      inventoryService.reserveFlight(flightId);
      inventoryService.reserveHotel(hotelId);
      inventoryService.reserveCarRental(carRentalId);
      paymentService.capture(bookingId, TOTAL_AMOUNT, CURRENCY, bookingId + "-axon-payment");
      bookingService.confirm(bookingId);
      notificationService.sendConfirmation(bookingId);
      commandGateway.send(new ConfirmBookingCommand(bookingId));
    } catch (final Exception ex) {
      compensate();
      bookingService.cancel(bookingId, ex.getMessage());
      commandGateway.send(new CancelBookingCommand(bookingId, ex.getMessage()));
      SagaLifecycle.end();
    }
  }

  @EndSaga
  @SagaEventHandler(associationProperty = "bookingId")
  public void on(final BookingConfirmedEvent event) {
    SagaLifecycle.end();
  }

  private void compensate() {
    if (carRentalId != null) {
      inventoryService.releaseCarRental(carRentalId);
    }
    if (hotelId != null) {
      inventoryService.releaseHotel(hotelId);
    }
    if (flightId != null) {
      inventoryService.releaseFlight(flightId);
    }
  }
}
