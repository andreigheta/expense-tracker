package org.andreigheta.expensetracker.service;

import org.andreigheta.expensetracker.dto.CategoryRequestDto;
import org.andreigheta.expensetracker.dto.CategoryResponseDto;
import org.andreigheta.expensetracker.entity.Category;
import org.andreigheta.expensetracker.entity.User;
import org.andreigheta.expensetracker.repository.CategoryRepository;
import org.andreigheta.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;

	public CategoryResponseDto createCategory(CategoryRequestDto requestDto, String userEmail) {
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));

		Category category = new Category();
		category.setName(requestDto.getName());
		category.setType(requestDto.getType().toUpperCase());
		category.setUser(user);

		Category savedCategory = categoryRepository.save(category);
		return mapToDto(savedCategory);
	}

	public List<CategoryResponseDto> getUserCategories(String userEmail) {
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return categoryRepository.findByUserId(user.getId())
				.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	private Category getCategoryIfBelongsToUser(Long categoryId, String userEmail) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category not found!"));

		if (!category.getUser().getEmail().equals(userEmail)) {
			throw new RuntimeException("You don't have permission to access this category!");
		}
		return category;
	}

	public CategoryResponseDto getCategoryById(Long id, String userEmail) {
		return mapToDto(getCategoryIfBelongsToUser(id, userEmail));
	}

	public CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto, String userEmail) {
		Category category = getCategoryIfBelongsToUser(id, userEmail);

		category.setName(requestDto.getName());
		category.setType(requestDto.getType().toUpperCase());

		return mapToDto(categoryRepository.save(category));
	}

	public void deleteCategory(Long id, String userEmail) {
		Category category = getCategoryIfBelongsToUser(id, userEmail);
		categoryRepository.delete(category);
	}

	private CategoryResponseDto mapToDto(Category category) {
		CategoryResponseDto dto = new CategoryResponseDto();
		dto.setId(category.getId());
		dto.setName(category.getName());
		dto.setType(category.getType());
		return dto;
	}
}