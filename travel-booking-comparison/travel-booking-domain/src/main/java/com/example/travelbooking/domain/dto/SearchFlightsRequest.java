package com.example.travelbooking.domain.dto;

import java.time.LocalDate;

public record SearchFlightsRequest(
    String origin,
    String destination,
    LocalDate date,
    int passengers) {
}
