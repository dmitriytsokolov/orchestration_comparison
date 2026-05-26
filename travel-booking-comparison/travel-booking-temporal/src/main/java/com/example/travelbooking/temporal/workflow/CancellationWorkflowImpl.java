package com.example.travelbooking.temporal.workflow;

import java.time.Duration;
import java.util.UUID;

import com.example.travelbooking.temporal.activity.BookingActivities;
import com.example.travelbooking.temporal.activity.NotificationActivities;
import com.example.travelbooking.temporal.activity.PaymentActivities;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

public class CancellationWorkflowImpl implements CancellationWorkflow {
  private final ActivityOptions options = ActivityOptions.newBuilder()
      .setScheduleToCloseTimeout(Duration.ofMinutes(5))
      .build();
  private final PaymentActivities paymentActivities =
      Workflow.newActivityStub(PaymentActivities.class, options);
  private final BookingActivities bookingActivities =
      Workflow.newActivityStub(BookingActivities.class, options);
  private final NotificationActivities notificationActivities =
      Workflow.newActivityStub(NotificationActivities.class, options);

  @Override
  public void cancel(final UUID bookingId) {
    paymentActivities.refundPayment(bookingId, bookingId + "-cancel-refund");
    bookingActivities.releaseReservations(bookingId);
    bookingActivities.cancelBooking(bookingId, "Customer requested cancellation");
    notificationActivities.sendCancellationConfirmation(bookingId);
  }
}
