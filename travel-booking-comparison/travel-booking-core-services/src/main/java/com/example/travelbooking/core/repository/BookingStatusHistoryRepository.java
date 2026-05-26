package com.example.travelbooking.core.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelbooking.domain.entity.BookingStatusHistory;

public interface BookingStatusHistoryRepository extends JpaRepository<BookingStatusHistory, UUID> {
}
