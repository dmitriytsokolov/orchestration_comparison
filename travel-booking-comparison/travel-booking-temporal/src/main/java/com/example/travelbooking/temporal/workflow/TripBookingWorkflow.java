package com.example.travelbooking.temporal.workflow;

import com.example.travelbooking.domain.dto.CreateBookingRequest;
import com.example.travelbooking.domain.dto.ModifyBookingRequest;
import com.example.travelbooking.domain.entity.BookingStatus;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface TripBookingWorkflow {
  @WorkflowMethod
  void bookTrip(CreateBookingRequest request);

  @SignalMethod
  void cancelTrip();

  @SignalMethod
  void modifyTrip(ModifyBookingRequest request);

  @QueryMethod
  BookingStatus getStatus();
}
