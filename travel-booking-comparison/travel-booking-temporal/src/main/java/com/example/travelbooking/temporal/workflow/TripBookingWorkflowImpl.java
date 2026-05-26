package com.example.travelbooking.temporal.workflow;

import java.math.BigDecimal;
import java.time.Duration;

import com.example.travelbooking.core.exception.InventoryUnavailableException;
import com.example.travelbooking.domain.dto.CreateBookingRequest;
import com.example.travelbooking.domain.dto.ModifyBookingRequest;
import com.example.travelbooking.domain.entity.BookingStatus;
import com.example.travelbooking.temporal.activity.BookingActivities;
import com.example.travelbooking.temporal.activity.CarRentalActivities;
import com.example.travelbooking.temporal.activity.FlightActivities;
import com.example.travelbooking.temporal.activity.HotelActivities;
import com.example.travelbooking.temporal.activity.NotificationActivities;
import com.example.travelbooking.temporal.activity.PaymentActivities;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;

public class TripBookingWorkflowImpl implements TripBookingWorkflow {
  private static final String CURRENCY = "USD";
  private static final BigDecimal TOTAL_AMOUNT = new BigDecimal("300.00");

  private final ActivityOptions options = ActivityOptions.newBuilder()
      .setScheduleToCloseTimeout(Duration.ofMinutes(5))
      .setRetryOptions(RetryOptions.newBuilder()
          .setMaximumAttempts(3)
          .setInitialInterval(Duration.ofSeconds(2))
          .setBackoffCoefficient(2.0)
          .setDoNotRetry(InventoryUnavailableException.class.getName())
          .build())
      .build();
  private final FlightActivities flightActivities =
      Workflow.newActivityStub(FlightActivities.class, options);
  private final HotelActivities hotelActivities =
      Workflow.newActivityStub(HotelActivities.class, options);
  private final CarRentalActivities carRentalActivities =
      Workflow.newActivityStub(CarRentalActivities.class, options);
  private final PaymentActivities paymentActivities =
      Workflow.newActivityStub(PaymentActivities.class, options);
  private final BookingActivities bookingActivities =
      Workflow.newActivityStub(BookingActivities.class, options);
  private final NotificationActivities notificationActivities =
      Workflow.newActivityStub(NotificationActivities.class, options);

  private BookingStatus status = BookingStatus.PENDING;

  @Override
  public void bookTrip(final CreateBookingRequest request) {
    final var bookingId = Workflow.getInfo().getWorkflowId().replace("booking-", "");
    final var parsedBookingId = java.util.UUID.fromString(bookingId);
    final var saga = new Saga(new Saga.Options.Builder().setParallelCompensation(false).build());
    try {
      flightActivities.reserveFlight(request.flightId(), parsedBookingId);
      saga.addCompensation(() -> flightActivities.releaseFlight(request.flightId(), parsedBookingId));
      hotelActivities.reserveHotel(request.hotelId(), parsedBookingId);
      saga.addCompensation(() -> hotelActivities.releaseHotel(request.hotelId(), parsedBookingId));
      carRentalActivities.reserveCarRental(request.carRentalId(), parsedBookingId);
      saga.addCompensation(
          () -> carRentalActivities.releaseCarRental(request.carRentalId(), parsedBookingId));
      paymentActivities.capturePayment(
          parsedBookingId,
          TOTAL_AMOUNT,
          CURRENCY,
          request.idempotencyKey() + "-payment");
      saga.addCompensation(
          () -> paymentActivities.refundPayment(parsedBookingId, request.idempotencyKey() + "-refund"));
      bookingActivities.confirmBooking(parsedBookingId);
      notificationActivities.sendConfirmation(parsedBookingId);
      status = BookingStatus.CONFIRMED;
    } catch (final Exception ex) {
      saga.compensate();
      bookingActivities.cancelBooking(parsedBookingId, ex.getMessage());
      status = BookingStatus.CANCELLED;
      throw ex;
    }
  }

  @Override
  public void cancelTrip() {
    status = BookingStatus.CANCELLED;
  }

  @Override
  public void modifyTrip(final ModifyBookingRequest request) {
    status = BookingStatus.MODIFICATION_PENDING;
  }

  @Override
  public BookingStatus getStatus() {
    return status;
  }
}
