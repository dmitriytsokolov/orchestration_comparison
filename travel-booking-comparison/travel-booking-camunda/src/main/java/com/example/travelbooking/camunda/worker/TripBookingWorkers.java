package com.example.travelbooking.camunda.worker;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.travelbooking.core.exception.InventoryUnavailableException;
import com.example.travelbooking.core.exception.PaymentFailedException;
import com.example.travelbooking.core.service.BookingService;
import com.example.travelbooking.core.service.InventoryService;
import com.example.travelbooking.core.service.NotificationService;
import com.example.travelbooking.core.service.PaymentService;
import com.example.travelbooking.domain.dto.ModifyBookingRequest;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class TripBookingWorkers {
  private static final String CURRENCY = "USD";
  private static final BigDecimal TOTAL_AMOUNT = new BigDecimal("300.00");

  private final InventoryService inventoryService;
  private final PaymentService paymentService;
  private final BookingService bookingService;
  private final NotificationService notificationService;

  public TripBookingWorkers(
      final InventoryService inventoryService,
      final PaymentService paymentService,
      final BookingService bookingService,
      final NotificationService notificationService) {
    this.inventoryService = inventoryService;
    this.paymentService = paymentService;
    this.bookingService = bookingService;
    this.notificationService = notificationService;
  }

  @JobWorker(type = "reserve-flight", autoComplete = false)
  public void reserveFlight(final ActivatedJob job, final JobClient client) {
    try {
      inventoryService.reserveFlight(uuid(job, "flightId"));
      complete(job, client);
    } catch (final InventoryUnavailableException ex) {
      throwError(job, client, "INVENTORY_UNAVAILABLE", ex.getMessage());
    }
  }

  @JobWorker(type = "release-flight", autoComplete = false)
  public void releaseFlight(final ActivatedJob job, final JobClient client) {
    inventoryService.releaseFlight(uuid(job, "flightId"));
    complete(job, client);
  }

  @JobWorker(type = "reserve-hotel", autoComplete = false)
  public void reserveHotel(final ActivatedJob job, final JobClient client) {
    try {
      inventoryService.reserveHotel(uuid(job, "hotelId"));
      complete(job, client);
    } catch (final InventoryUnavailableException ex) {
      throwError(job, client, "INVENTORY_UNAVAILABLE", ex.getMessage());
    }
  }

  @JobWorker(type = "release-hotel", autoComplete = false)
  public void releaseHotel(final ActivatedJob job, final JobClient client) {
    inventoryService.releaseHotel(uuid(job, "hotelId"));
    complete(job, client);
  }

  @JobWorker(type = "reserve-car", autoComplete = false)
  public void reserveCar(final ActivatedJob job, final JobClient client) {
    try {
      inventoryService.reserveCarRental(uuid(job, "carRentalId"));
      complete(job, client);
    } catch (final InventoryUnavailableException ex) {
      throwError(job, client, "INVENTORY_UNAVAILABLE", ex.getMessage());
    }
  }

  @JobWorker(type = "release-car", autoComplete = false)
  public void releaseCar(final ActivatedJob job, final JobClient client) {
    inventoryService.releaseCarRental(uuid(job, "carRentalId"));
    complete(job, client);
  }

  @JobWorker(type = "process-payment", autoComplete = false)
  public void processPayment(final ActivatedJob job, final JobClient client) {
    try {
      paymentService.capture(
          uuid(job, "bookingId"),
          TOTAL_AMOUNT,
          CURRENCY,
          string(job, "idempotencyKey") + "-payment");
      complete(job, client);
    } catch (final PaymentFailedException ex) {
      throwError(job, client, "PAYMENT_FAILED", ex.getMessage());
    }
  }

  @JobWorker(type = "confirm-booking", autoComplete = false)
  public void confirmBooking(final ActivatedJob job, final JobClient client) {
    bookingService.confirm(uuid(job, "bookingId"));
    complete(job, client);
  }

  @JobWorker(type = "send-confirmation", autoComplete = false)
  public void sendConfirmation(final ActivatedJob job, final JobClient client) {
    notificationService.sendConfirmation(uuid(job, "bookingId"));
    complete(job, client);
  }

  @JobWorker(type = "initiate-refund", autoComplete = false)
  public void initiateRefund(final ActivatedJob job, final JobClient client) {
    final var bookingId = uuid(job, "bookingId");
    paymentService.refund(bookingId, bookingId + "-refund");
    complete(job, client);
  }

  @JobWorker(type = "release-reservations-on-cancel", autoComplete = false)
  public void releaseReservationsOnCancel(final ActivatedJob job, final JobClient client) {
    final var booking = bookingService.findBooking(uuid(job, "bookingId"));
    inventoryService.releaseCarRental(booking.getCarRentalId());
    inventoryService.releaseHotel(booking.getHotelId());
    inventoryService.releaseFlight(booking.getFlightId());
    complete(job, client);
  }

  @JobWorker(type = "confirm-cancellation", autoComplete = false)
  public void confirmCancellation(final ActivatedJob job, final JobClient client) {
    bookingService.cancel(uuid(job, "bookingId"), "Customer requested cancellation");
    complete(job, client);
  }

  @JobWorker(type = "send-cancellation-email", autoComplete = false)
  public void sendCancellationEmail(final ActivatedJob job, final JobClient client) {
    notificationService.sendCancellationConfirmation(uuid(job, "bookingId"));
    complete(job, client);
  }

  @JobWorker(type = "modify-booking", autoComplete = false)
  public void modifyBooking(final ActivatedJob job, final JobClient client) {
    bookingService.modify(uuid(job, "bookingId"), new ModifyBookingRequest(
        uuid(job, "flightId"),
        uuid(job, "hotelId"),
        uuid(job, "carRentalId")));
    complete(job, client);
  }

  private UUID uuid(final ActivatedJob job, final String key) {
    return UUID.fromString(string(job, key));
  }

  private String string(final ActivatedJob job, final String key) {
    return String.valueOf(job.getVariablesAsMap().get(key));
  }

  private void complete(final ActivatedJob job, final JobClient client) {
    client.newCompleteCommand(job.getKey()).send().join();
  }

  private void throwError(
      final ActivatedJob job,
      final JobClient client,
      final String errorCode,
      final String message) {
    client.newThrowErrorCommand(job.getKey())
        .errorCode(errorCode)
        .errorMessage(message)
        .send()
        .join();
  }
}
