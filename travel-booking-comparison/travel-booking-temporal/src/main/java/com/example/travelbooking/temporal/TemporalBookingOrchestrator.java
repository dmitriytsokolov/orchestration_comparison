package com.example.travelbooking.temporal;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.travelbooking.core.service.BookingOrchestrator;
import com.example.travelbooking.domain.dto.CreateBookingRequest;
import com.example.travelbooking.domain.dto.ModifyBookingRequest;
import com.example.travelbooking.temporal.workflow.CancellationWorkflow;
import com.example.travelbooking.temporal.workflow.TripBookingWorkflow;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;

@Component
public class TemporalBookingOrchestrator implements BookingOrchestrator {
  private static final String TASK_QUEUE = "travel-booking";

  private final WorkflowClient workflowClient;

  public TemporalBookingOrchestrator(final WorkflowClient workflowClient) {
    this.workflowClient = workflowClient;
  }

  @Override
  public String startBooking(final UUID bookingId, final CreateBookingRequest request) {
    final var workflowId = "booking-" + bookingId;
    final var workflow = workflowClient.newWorkflowStub(
        TripBookingWorkflow.class,
        WorkflowOptions.newBuilder()
            .setWorkflowId(workflowId)
            .setTaskQueue(TASK_QUEUE)
            .build());
    WorkflowClient.start(workflow::bookTrip, request);
    return workflowId;
  }

  @Override
  public String startCancellation(final UUID bookingId) {
    final var workflowId = "cancel-" + bookingId;
    final var workflow = workflowClient.newWorkflowStub(
        CancellationWorkflow.class,
        WorkflowOptions.newBuilder()
            .setWorkflowId(workflowId)
            .setTaskQueue(TASK_QUEUE)
            .build());
    WorkflowClient.start(workflow::cancel, bookingId);
    return workflowId;
  }

  @Override
  public String startModification(final UUID bookingId, final ModifyBookingRequest request) {
    final var workflowId = "modify-" + bookingId;
    final var workflow = workflowClient.newWorkflowStub(
        TripBookingWorkflow.class,
        WorkflowOptions.newBuilder()
            .setWorkflowId(workflowId)
            .setTaskQueue(TASK_QUEUE)
            .build());
    workflow.modifyTrip(request);
    return workflowId;
  }
}
