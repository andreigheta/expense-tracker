package org.andreigheta.expensetracker.controller;

import org.andreigheta.expensetracker.dto.CategoryRequestDto;
import org.andreigheta.expensetracker.dto.CategoryResponseDto;
import org.andreigheta.expensetracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@PostMapping
	public ResponseEntity<CategoryResponseDto> createCategory(
			@Valid @RequestBody CategoryRequestDto requestDto,
			Principal principal) {
		return new ResponseEntity<>(categoryService.createCategory(requestDto, principal.getName()), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<CategoryResponseDto>> getUserCategories(Principal principal) {
		return ResponseEntity.ok(categoryService.getUserCategories(principal.getName()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id, Principal principal) {
		return ResponseEntity.ok(categoryService.getCategoryById(id, principal.getName()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryResponseDto> updateCategory(
			@PathVariable Long id,
			@Valid @RequestBody CategoryRequestDto requestDto,
			Principal principal) {
		return ResponseEntity.ok(categoryService.updateCategory(id, requestDto, principal.getName()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id, Principal principal) {
		categoryService.deleteCategory(id, principal.getName());
		return ResponseEntity.noContent().build();
	}
}