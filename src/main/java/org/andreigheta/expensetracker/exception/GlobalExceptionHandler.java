package org.andreigheta.expensetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				errorMessage,
				LocalDateTime.now()
		);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex) {
		ErrorResponse error = new ErrorResponse(
				HttpStatus.CONFLICT.value(),
				ex.getMessage(),
				LocalDateTime.now()
		);
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalExceptions(Exception ex) {
		ErrorResponse error = new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal server error: " + ex.getMessage(),
				LocalDateTime.now()
		);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex) {
		ErrorResponse error = new ErrorResponse(
				HttpStatus.FORBIDDEN.value(),
				"Access forbidden! You do not have the role to access this page",
				LocalDateTime.now()
		);
		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}
}
