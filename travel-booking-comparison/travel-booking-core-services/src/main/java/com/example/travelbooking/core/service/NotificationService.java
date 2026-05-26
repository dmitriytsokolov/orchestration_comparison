package com.example.travelbooking.core.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
  private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

  private final JavaMailSender mailSender;
  private final BookingService bookingService;
  private final UserService userService;

  public NotificationService(
      final JavaMailSender mailSender,
      final BookingService bookingService,
      final UserService userService) {
    this.mailSender = mailSender;
    this.bookingService = bookingService;
    this.userService = userService;
  }

  public void sendConfirmation(final UUID bookingId) {
    final var booking = bookingService.findBooking(bookingId);
    final var user = userService.findById(booking.getUserId());
    send(user.getEmail(), "Booking confirmed", "Your booking " + bookingId + " is confirmed.");
    log.info("Sent booking confirmation bookingId={}", bookingId);
  }

  public void sendCancellationConfirmation(final UUID bookingId) {
    final var booking = bookingService.findBooking(bookingId);
    final var user = userService.findById(booking.getUserId());
    send(user.getEmail(), "Booking cancelled", "Your booking " + bookingId + " is cancelled.");
    log.info("Sent cancellation confirmation bookingId={}", bookingId);
  }

  private void send(final String to, final String subject, final String text) {
    final var message = new SimpleMailMessage();
    message.setFrom("bookings@example.com");
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    mailSender.send(message);
  }
}
