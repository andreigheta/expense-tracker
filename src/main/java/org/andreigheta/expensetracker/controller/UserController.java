package org.andreigheta.expensetracker.controller;

import org.andreigheta.expensetracker.dto.UserLoginDto;
import org.andreigheta.expensetracker.dto.UserRegisterDto;
import org.andreigheta.expensetracker.dto.UserResponseDto;
import org.andreigheta.expensetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

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

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserLoginDto loginDto) {
		String jwtToken = userService.loginUser(loginDto);
		return ResponseEntity.ok(jwtToken);
	}

	@GetMapping("/admin-only")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<String> adminPing() {
		return ResponseEntity.ok("If you're seeing this that means you are an administrator.");
	}
}
