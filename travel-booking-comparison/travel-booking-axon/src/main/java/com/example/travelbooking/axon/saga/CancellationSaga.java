package com.example.travelbooking.axon.saga;

import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.travelbooking.core.service.BookingService;
import com.example.travelbooking.core.service.InventoryService;
import com.example.travelbooking.core.service.NotificationService;
import com.example.travelbooking.core.service.PaymentService;
import com.example.travelbooking.domain.event.BookingCancellationInitiatedEvent;

@Saga
public class CancellationSaga {
  @Autowired
  private transient PaymentService paymentService;
  @Autowired
  private transient InventoryService inventoryService;
  @Autowired
  private transient BookingService bookingService;
  @Autowired
  private transient NotificationService notificationService;

  @StartSaga
  @EndSaga
  @SagaEventHandler(associationProperty = "bookingId")
  public void on(final BookingCancellationInitiatedEvent event) {
    paymentService.refund(event.bookingId(), event.bookingId() + "-axon-refund");
    final var booking = bookingService.findBooking(event.bookingId());
    inventoryService.releaseCarRental(booking.getCarRentalId());
    inventoryService.releaseHotel(booking.getHotelId());
    inventoryService.releaseFlight(booking.getFlightId());
    bookingService.cancel(event.bookingId(), event.reason());
    notificationService.sendCancellationConfirmation(event.bookingId());
  }
}
