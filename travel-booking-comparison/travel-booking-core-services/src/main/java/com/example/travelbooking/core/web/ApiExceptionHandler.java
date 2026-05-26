package com.example.travelbooking.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.travelbooking.core.exception.BookingNotFoundException;
import com.example.travelbooking.core.exception.InventoryUnavailableException;
import com.example.travelbooking.core.exception.PaymentFailedException;
import com.example.travelbooking.core.exception.UserNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler({
      BookingNotFoundException.class,
      UserNotFoundException.class
  })
  ProblemDetail notFound(final RuntimeException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler({
      InventoryUnavailableException.class,
      PaymentFailedException.class,
      IllegalArgumentException.class
  })
  ProblemDetail badRequest(final RuntimeException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
  }
}
