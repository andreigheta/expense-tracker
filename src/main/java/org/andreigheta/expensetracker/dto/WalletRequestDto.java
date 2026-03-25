package org.andreigheta.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WalletRequestDto {

	@NotBlank(message = "Wallet name is mandatory (ex: Cash, Revolut)")
	private String name;

	@NotNull(message = "Initial balance is mandatory")
	private Double balance;
}