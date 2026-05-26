package com.example.travelbooking.axon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.travelbooking")
@EntityScan("com.example.travelbooking.domain.entity")
@EnableJpaRepositories("com.example.travelbooking.core.repository")
public class TravelBookingAxonApplication {
  public static void main(final String[] args) {
    SpringApplication.run(TravelBookingAxonApplication.class, args);
  }
}
