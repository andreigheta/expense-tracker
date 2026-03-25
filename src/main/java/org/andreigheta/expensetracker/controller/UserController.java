package org.andreigheta.expensetracker.controller;

import org.andreigheta.expensetracker.dto.UserRegisterDto;
import org.andreigheta.expensetracker.dto.UserResponseDto;
import org.andreigheta.expensetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRegisterDto registerDto) {
		UserResponseDto response = userService.registerUser(registerDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
