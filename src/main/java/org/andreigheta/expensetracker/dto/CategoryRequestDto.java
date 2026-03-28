package org.andreigheta.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDto {

	@NotBlank(message = "Name is mandatory (ex: Food, Salary)")
	private String name;

	@NotBlank(message = "Type is mandatory (ex: INCOME or EXPENSE)")
	private String type;
}