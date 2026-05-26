package com.example.travelbooking.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(PaymentGatewayProperties.class)
public class PaymentGatewayConfig {
  @Bean
  RestClient paymentGatewayRestClient(final PaymentGatewayProperties properties) {
    return RestClient.builder()
        .baseUrl(properties.baseUrl())
        .build();
  }
}
