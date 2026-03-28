package org.andreigheta.expensetracker.dto;

import lombok.Data;

@Data
public class CategoryResponseDto {
	private Long id;
	private String name;
	private String type;
}