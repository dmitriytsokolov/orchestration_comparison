package com.example.travelbooking.domain.dto;

import java.time.LocalDate;

public record SearchHotelsRequest(String city, LocalDate checkIn, LocalDate checkOut, int rooms) {
}
