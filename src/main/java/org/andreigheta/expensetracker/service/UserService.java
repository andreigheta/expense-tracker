package org.andreigheta.expensetracker.service;

import org.andreigheta.expensetracker.dto.UserLoginDto;
import org.andreigheta.expensetracker.dto.UserRegisterDto;
import org.andreigheta.expensetracker.dto.UserResponseDto;
import org.andreigheta.expensetracker.entity.Role;
import org.andreigheta.expensetracker.entity.User;
import org.andreigheta.expensetracker.exception.DuplicateResourceException;
import org.andreigheta.expensetracker.repository.RoleRepository;
import org.andreigheta.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.andreigheta.expensetracker.security.JwtService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final JavaMailSender mailSender;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public UserResponseDto registerUser(UserRegisterDto registerDto) {
		if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
			throw new DuplicateResourceException("Email already used!");
		}

		User user = new User();
		user.setFirstName(registerDto.getFirstName());
		user.setLastName(registerDto.getLastName());
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		Role userRole = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new RuntimeException("Default role not found in DB"));
		user.getRoles().add(userRole);

		User savedUser = userRepository.save(user);

		sendWelcomeEmail(savedUser.getEmail(), savedUser.getFirstName());

		UserResponseDto responseDto = new UserResponseDto();
		responseDto.setId(savedUser.getId());
		responseDto.setFirstName(savedUser.getFirstName());
		responseDto.setLastName(savedUser.getLastName());
		responseDto.setEmail(savedUser.getEmail());

		return responseDto;
	}

	public String loginUser(UserLoginDto loginDto) {
		authenticationManager.authenticate(
				new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
						loginDto.getEmail(),
						loginDto.getPassword()
				)
		);

		User user = userRepository.findByEmail(loginDto.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		return jwtService.generateToken(user);
	}

	private void sendWelcomeEmail(String to, String name) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("no-reply@expensetracker.com");
		message.setTo(to);
		message.setSubject("Welcome to Expense Tracker!");
		message.setText("Hello, " + name + "!\n\nYour account has been successfully created!");

		mailSender.send(message);
	}
}
