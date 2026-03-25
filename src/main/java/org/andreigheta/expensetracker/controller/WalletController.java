package org.andreigheta.expensetracker.controller;

import org.andreigheta.expensetracker.dto.WalletRequestDto;
import org.andreigheta.expensetracker.dto.WalletResponseDto;
import org.andreigheta.expensetracker.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

	private final WalletService walletService;

	@PostMapping
	public ResponseEntity<WalletResponseDto> createWallet(
			@Valid @RequestBody WalletRequestDto requestDto,
			Principal principal
	) {
		WalletResponseDto response = walletService.createWallet(requestDto, principal.getName());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<WalletResponseDto>> getUserWallets(Principal principal) {
		List<WalletResponseDto> wallets = walletService.getUserWallets(principal.getName());
		return ResponseEntity.ok(wallets);
	}

	@GetMapping("/{id}")
	public ResponseEntity<WalletResponseDto> getWalletById(
			@PathVariable Long id,
			Principal principal) {
		WalletResponseDto response = walletService.getWalletById(id, principal.getName());
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<WalletResponseDto> updateWallet(
			@PathVariable Long id,
			@Valid @RequestBody WalletRequestDto requestDto,
			Principal principal) {
		WalletResponseDto response = walletService.updateWallet(id, requestDto, principal.getName());
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWallet(
			@PathVariable Long id,
			Principal principal) {
		walletService.deleteWallet(id, principal.getName());
		return ResponseEntity.noContent().build();
	}
}