package org.andreigheta.expensetracker.service;

import org.andreigheta.expensetracker.dto.TransactionRequestDto;
import org.andreigheta.expensetracker.dto.TransactionResponseDto;
import org.andreigheta.expensetracker.entity.Category;
import org.andreigheta.expensetracker.entity.Transaction;
import org.andreigheta.expensetracker.entity.User;
import org.andreigheta.expensetracker.entity.Wallet;
import org.andreigheta.expensetracker.repository.CategoryRepository;
import org.andreigheta.expensetracker.repository.TransactionRepository;
import org.andreigheta.expensetracker.repository.UserRepository;
import org.andreigheta.expensetracker.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final WalletRepository walletRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;

	@Transactional
	public TransactionResponseDto createTransaction(TransactionRequestDto requestDto, String userEmail) {
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));

		Wallet wallet = walletRepository.findById(requestDto.getWalletId())
				.orElseThrow(() -> new RuntimeException("Wallet not found"));
		if (!wallet.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("You do not have the permission to access this wallet!");
		}

		Category category = categoryRepository.findById(requestDto.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found"));
		if (!category.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("You do not have the permission to access this category!");
		}

		if (category.getType().equalsIgnoreCase("INCOME")) {
			wallet.setBalance(wallet.getBalance() + requestDto.getAmount());
		} else if (category.getType().equalsIgnoreCase("EXPENSE")) {
			wallet.setBalance(wallet.getBalance() - requestDto.getAmount());
		}
		walletRepository.save(wallet);

		Transaction transaction = new Transaction();
		transaction.setAmount(requestDto.getAmount());
		transaction.setDescription(requestDto.getDescription());
		transaction.setDate(LocalDateTime.now());
		transaction.setWallet(wallet);
		transaction.setCategory(category);

		Transaction savedTransaction = transactionRepository.save(transaction);
		return mapToDto(savedTransaction);
	}

	public List<TransactionResponseDto> getTransactionsByWallet(Long walletId, String userEmail) {
		Wallet wallet = walletRepository.findById(walletId)
				.orElseThrow(() -> new RuntimeException("Wallet not found"));

		if (!wallet.getUser().getEmail().equals(userEmail)) {
			throw new RuntimeException("Forbidden access for this wallet!");
		}

		return transactionRepository.findByWalletId(walletId)
				.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public TransactionResponseDto updateTransaction(Long id, TransactionRequestDto requestDto, String userEmail) {
		Transaction transaction = transactionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Transaction not found!"));

		if (!transaction.getWallet().getUser().getEmail().equals(userEmail)) {
			throw new RuntimeException("Forbidden access for this wallet.");
		}

		Wallet oldWallet = transaction.getWallet();
		if (transaction.getCategory().getType().equalsIgnoreCase("INCOME")) {
			oldWallet.setBalance(oldWallet.getBalance() - transaction.getAmount());
		} else {
			oldWallet.setBalance(oldWallet.getBalance() + transaction.getAmount());
		}
		walletRepository.save(oldWallet);

		Wallet newWallet = walletRepository.findById(requestDto.getWalletId())
				.orElseThrow(() -> new RuntimeException("New wallet not found!"));
		if (!newWallet.getUser().getEmail().equals(userEmail)) throw new RuntimeException("Forbidden access for this wallet!");

		Category newCategory = categoryRepository.findById(requestDto.getCategoryId())
				.orElseThrow(() -> new RuntimeException("New category not found!"));
		if (!newCategory.getUser().getEmail().equals(userEmail)) throw new RuntimeException("Forbidden access for this category!");

		if (newCategory.getType().equalsIgnoreCase("INCOME")) {
			newWallet.setBalance(newWallet.getBalance() + requestDto.getAmount());
		} else {
			newWallet.setBalance(newWallet.getBalance() - requestDto.getAmount());
		}
		walletRepository.save(newWallet);

		transaction.setAmount(requestDto.getAmount());
		transaction.setDescription(requestDto.getDescription());
		transaction.setWallet(newWallet);
		transaction.setCategory(newCategory);

		return mapToDto(transactionRepository.save(transaction));
	}

	@Transactional
	public void deleteTransaction(Long id, String userEmail) {
		Transaction transaction = transactionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Transaction not found!"));

		if (!transaction.getWallet().getUser().getEmail().equals(userEmail)) {
			throw new RuntimeException("Forbidden access for this wallet!");
		}

		Wallet wallet = transaction.getWallet();

		if (transaction.getCategory().getType().equalsIgnoreCase("INCOME")) {
			wallet.setBalance(wallet.getBalance() - transaction.getAmount());
		} else {
			wallet.setBalance(wallet.getBalance() + transaction.getAmount());
		}

		walletRepository.save(wallet);
		transactionRepository.delete(transaction);
	}

	private TransactionResponseDto mapToDto(Transaction transaction) {
		TransactionResponseDto dto = new TransactionResponseDto();
		dto.setId(transaction.getId());
		dto.setAmount(transaction.getAmount());
		dto.setDescription(transaction.getDescription());
		dto.setDate(transaction.getDate());
		dto.setWalletName(transaction.getWallet().getName());
		dto.setCategoryName(transaction.getCategory().getName());
		dto.setCategoryType(transaction.getCategory().getType());
		return dto;
	}
}
