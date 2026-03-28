package org.andreigheta.expensetracker.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
	private Long id;
	private Double amount;
	private String description;
	private LocalDateTime date;

	private String walletName;
	private String categoryName;
	private String categoryType;
}
