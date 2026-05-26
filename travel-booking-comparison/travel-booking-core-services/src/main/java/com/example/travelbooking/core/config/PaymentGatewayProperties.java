package com.example.travelbooking.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "travel-booking.payment-gateway")
public record PaymentGatewayProperties(String baseUrl) {
}
