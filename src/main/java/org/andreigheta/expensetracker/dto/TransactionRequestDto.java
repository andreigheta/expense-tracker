package org.andreigheta.expensetracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransactionRequestDto {

	@NotNull(message = "Sum is mandatory")
	@Positive(message = "Sum must be positive")
	private Double amount;

	private String description;

	@NotNull(message = "You must specify the wallet (walletId)")
	private Long walletId;

	@NotNull(message = "You must specify the category (categoryId)")
	private Long categoryId;
}