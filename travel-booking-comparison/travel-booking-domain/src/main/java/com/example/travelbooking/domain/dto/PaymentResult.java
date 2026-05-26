package com.example.travelbooking.domain.dto;

public record PaymentResult(boolean successful, String gatewayRef, String failureReason) {
}
