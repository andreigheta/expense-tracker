package org.andreigheta.expensetracker.service;

import org.andreigheta.expensetracker.dto.WalletRequestDto;
import org.andreigheta.expensetracker.dto.WalletResponseDto;
import org.andreigheta.expensetracker.entity.User;
import org.andreigheta.expensetracker.entity.Wallet;
import org.andreigheta.expensetracker.repository.UserRepository;
import org.andreigheta.expensetracker.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletService {

	private final WalletRepository walletRepository;
	private final UserRepository userRepository;

	public WalletResponseDto createWallet(WalletRequestDto requestDto, String userEmail) {
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));

		Wallet wallet = new Wallet();
		wallet.setName(requestDto.getName());
		wallet.setBalance(requestDto.getBalance());
		wallet.setUser(user);

		Wallet savedWallet = walletRepository.save(wallet);

		return mapToDto(savedWallet);
	}

	public List<WalletResponseDto> getUserWallets(String userEmail) {
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));

		List<Wallet> wallets = walletRepository.findByUserId(user.getId());

		return wallets.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	private Wallet getWalletIfBelongsToUser(Long walletId, String userEmail) {
		Wallet wallet = walletRepository.findById(walletId)
				.orElseThrow(() -> new RuntimeException("Portofelul nu a fost găsit!"));

		if (!wallet.getUser().getEmail().equals(userEmail)) {
			throw new RuntimeException("Nu ai permisiunea să accesezi acest portofel!"); // 403 Forbidden în viitor
		}
		return wallet;
	}

	public WalletResponseDto getWalletById(Long id, String userEmail) {
		Wallet wallet = getWalletIfBelongsToUser(id, userEmail);
		return mapToDto(wallet);
	}

	public WalletResponseDto updateWallet(Long id, WalletRequestDto requestDto, String userEmail) {
		Wallet wallet = getWalletIfBelongsToUser(id, userEmail);

		wallet.setName(requestDto.getName());
		wallet.setBalance(requestDto.getBalance());

		Wallet updatedWallet = walletRepository.save(wallet);
		return mapToDto(updatedWallet);
	}

	public void deleteWallet(Long id, String userEmail) {
		Wallet wallet = getWalletIfBelongsToUser(id, userEmail);
		walletRepository.delete(wallet);
	}

	private WalletResponseDto mapToDto(Wallet wallet) {
		WalletResponseDto dto = new WalletResponseDto();
		dto.setId(wallet.getId());
		dto.setName(wallet.getName());
		dto.setBalance(wallet.getBalance());
		return dto;
	}
}