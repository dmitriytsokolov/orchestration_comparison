package com.example.travelbooking.core.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.travelbooking.core.exception.BookingNotFoundException;
import com.example.travelbooking.core.repository.BookingRepository;
import com.example.travelbooking.core.repository.BookingStatusHistoryRepository;
import com.example.travelbooking.domain.dto.BookingResponse;
import com.example.travelbooking.domain.dto.BookingStatusResponse;
import com.example.travelbooking.domain.dto.CreateBookingRequest;
import com.example.travelbooking.domain.dto.ModifyBookingRequest;
import com.example.travelbooking.domain.entity.Booking;
import com.example.travelbooking.domain.entity.BookingStatus;
import com.example.travelbooking.domain.entity.BookingStatusHistory;

@Service
public class BookingService {
  private static final Logger log = LoggerFactory.getLogger(BookingService.class);
  private static final BigDecimal DEFAULT_TOTAL = new BigDecimal("300.00");

  private final BookingRepository bookingRepository;
  private final BookingStatusHistoryRepository historyRepository;
  private final IdempotencyService idempotencyService;
  private final TravelBookingMapper mapper;

  public BookingService(
      final BookingRepository bookingRepository,
      final BookingStatusHistoryRepository historyRepository,
      final IdempotencyService idempotencyService,
      final TravelBookingMapper mapper) {
    this.bookingRepository = bookingRepository;
    this.historyRepository = historyRepository;
    this.idempotencyService = idempotencyService;
    this.mapper = mapper;
  }

  @Transactional
  public Booking createPending(final CreateBookingRequest request) {
    final var existing = idempotencyService.findBooking(request.idempotencyKey());
    if (existing.isPresent()) {
      log.info("Returning existing booking for idempotency key bookingId={}", existing.get().getId());
      return existing.get();
    }
    final var now = Instant.now();
    final var booking = new Booking(
        UUID.randomUUID(),
        request.userId(),
        request.flightId(),
        request.hotelId(),
        request.carRentalId(),
        DEFAULT_TOTAL,
        request.idempotencyKey(),
        now);
    final var saved = bookingRepository.save(booking);
    recordStatus(saved.getId(), BookingStatus.PENDING, "Booking requested");
    log.info("Created pending booking bookingId={}", saved.getId());
    return saved;
  }

  @Transactional
  public void attachOrchestrationId(final UUID bookingId, final String orchestrationId) {
    final var booking = findBooking(bookingId);
    booking.setOrchestrationId(orchestrationId);
    log.info("Attached orchestration to booking bookingId={} orchestrationId={}", bookingId, orchestrationId);
  }

  @Transactional
  public void confirm(final UUID bookingId) {
    final var booking = findBooking(bookingId);
    booking.confirm();
    recordStatus(bookingId, BookingStatus.CONFIRMED, "Booking confirmed");
    log.info("Confirmed booking bookingId={}", bookingId);
  }

  @Transactional
  public void cancel(final UUID bookingId, final String reason) {
    final var booking = findBooking(bookingId);
    booking.cancel();
    recordStatus(bookingId, BookingStatus.CANCELLED, reason);
    log.info("Cancelled booking bookingId={} reason={}", bookingId, reason);
  }

  @Transactional
  public void modify(final UUID bookingId, final ModifyBookingRequest request) {
    final var booking = findBooking(bookingId);
    booking.markModificationPending();
    booking.modify(request.flightId(), request.hotelId(), request.carRentalId());
    recordStatus(bookingId, BookingStatus.CONFIRMED, "Booking modified");
    log.info("Modified booking bookingId={}", bookingId);
  }

  @Transactional(readOnly = true)
  public BookingStatusResponse getStatus(final UUID bookingId) {
    return mapper.toStatusResponse(findBooking(bookingId));
  }

  @Transactional(readOnly = true)
  public BookingResponse getBooking(final UUID bookingId) {
    return mapper.toResponse(findBooking(bookingId));
  }

  @Transactional(readOnly = true)
  public List<BookingResponse> getUserBookings(final UUID userId) {
    return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId)
        .stream()
        .map(mapper::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public Booking findBooking(final UUID bookingId) {
    return bookingRepository.findById(bookingId)
        .orElseThrow(() -> new BookingNotFoundException(bookingId));
  }

  private void recordStatus(
      final UUID bookingId,
      final BookingStatus status,
      final String reason) {
    historyRepository.save(new BookingStatusHistory(bookingId, status, Instant.now(), reason));
  }
}
