package org.andreigheta.expensetracker.dto;

import lombok.Data;

@Data
public class WalletResponseDto {
	private Long id;
	private String name;
	private Double balance;
}
