package com.rkumar.tikitly.advices;

import com.rkumar.tikitly.exceptions.ResourceAlreadyExistsException;
import com.rkumar.tikitly.exceptions.ResourceNotFoundException;
import com.rkumar.tikitly.exceptions.SeatUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundExceptionHandler(ResourceNotFoundException ex, WebRequest request) {
        ApiErrorResponse errorDetails = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();

        return buildExceptionResponseEntity(errorDetails);
    }

    @ExceptionHandler(SeatUnavailableException.class)
    public ResponseEntity<?> seatUnavailableExceptionHandler(SeatUnavailableException ex, WebRequest request) {
        ApiErrorResponse errorDetails = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();

        return buildExceptionResponseEntity(errorDetails);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> resourceAlreadyExistsExceptionHandler(ResourceAlreadyExistsException ex, WebRequest request) {
        ApiErrorResponse errorDetails = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT)
                .error("Conflict")
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();

        return buildExceptionResponseEntity(errorDetails);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        ApiErrorResponse errorDetails = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .error("Server Error")
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();

        return buildExceptionResponseEntity(errorDetails);
    }



    private ResponseEntity<ApiResponse<?>> buildExceptionResponseEntity(ApiErrorResponse error) {
        return new ResponseEntity<>(new ApiResponse<>(error), error.getStatus());
    }
}
