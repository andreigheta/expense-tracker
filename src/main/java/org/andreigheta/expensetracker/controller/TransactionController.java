package org.andreigheta.expensetracker.controller;

import org.andreigheta.expensetracker.dto.TransactionRequestDto;
import org.andreigheta.expensetracker.dto.TransactionResponseDto;
import org.andreigheta.expensetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

	@PostMapping
	public ResponseEntity<TransactionResponseDto> createTransaction(
			@Valid @RequestBody TransactionRequestDto requestDto,
			Principal principal) {
		return new ResponseEntity<>(transactionService.createTransaction(requestDto, principal.getName()), HttpStatus.CREATED);
	}

	@GetMapping("/wallet/{walletId}")
	public ResponseEntity<List<TransactionResponseDto>> getTransactionsByWallet(
			@PathVariable Long walletId,
			Principal principal) {
		return ResponseEntity.ok(transactionService.getTransactionsByWallet(walletId, principal.getName()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TransactionResponseDto> updateTransaction(
			@PathVariable Long id,
			@Valid @RequestBody TransactionRequestDto requestDto,
			Principal principal) {
		return ResponseEntity.ok(transactionService.updateTransaction(id, requestDto, principal.getName()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTransaction(
			@PathVariable Long id,
			Principal principal) {
		transactionService.deleteTransaction(id, principal.getName());
		return ResponseEntity.noContent().build();
	}
}
