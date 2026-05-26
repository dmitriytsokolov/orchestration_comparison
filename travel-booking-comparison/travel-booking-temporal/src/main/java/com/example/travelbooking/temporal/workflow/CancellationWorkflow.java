package com.example.travelbooking.temporal.workflow;

import java.util.UUID;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CancellationWorkflow {
  @WorkflowMethod
  void cancel(UUID bookingId);
}
