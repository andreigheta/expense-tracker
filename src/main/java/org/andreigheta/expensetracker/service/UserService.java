package org.andreigheta.expensetracker.service;

import org.andreigheta.expensetracker.dto.UserRegisterDto;
import org.andreigheta.expensetracker.dto.UserResponseDto;
import org.andreigheta.expensetracker.entity.User;
import org.andreigheta.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final JavaMailSender mailSender;

	public UserResponseDto registerUser(UserRegisterDto registerDto) {
		if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
			throw new RuntimeException("Email already used!");
		}

		User user = new User();
		user.setFirstName(registerDto.getFirstName());
		user.setLastName(registerDto.getLastName());
		user.setEmail(registerDto.getEmail());
		user.setPassword(registerDto.getPassword());

		User savedUser = userRepository.save(user);

		sendWelcomeEmail(savedUser.getEmail(), savedUser.getFirstName());

		UserResponseDto responseDto = new UserResponseDto();
		responseDto.setId(savedUser.getId());
		responseDto.setFirstName(savedUser.getFirstName());
		responseDto.setLastName(savedUser.getLastName());
		responseDto.setEmail(savedUser.getEmail());

		return responseDto;
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
