package org.andreigheta.expensetracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDto {

	@NotBlank(message = "First name is mandatory")
	private String firstName;

	@NotBlank(message = "Last name is mandatory")
	private String lastName;

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email format is invalid")
	private String email;

	@NotBlank(message = "Password is mandatory")
	@Size(min = 6, message = "Password must contain minimum 6 characters")
	private String password;
}