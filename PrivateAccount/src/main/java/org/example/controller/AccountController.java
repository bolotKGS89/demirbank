package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dtos.AccountDto;
import org.example.dtos.RequestDto;
import org.example.mapper.AccountMapper;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing bank accounts")
public class AccountController {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID")
    @ApiResponse(responseCode = "200", description = "Account found")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @Cacheable(value = "account", key = "#id")
    public AccountDto getById(@PathVariable("id") Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageFormat
                        .format("Could not find by id {0}", id)));
        return accountMapper.toDto(account);
    }

    @GetMapping("/all")
    @Cacheable(value = "accounts", key = "{#requestDto.page, #requestDto.size}")
    @Operation(summary = "Get all accounts (paginated)")
    @ApiResponse(responseCode = "200", description = "Page of accounts returned")
    public Page<AccountDto> getAll(@RequestBody RequestDto requestDto) {
        Page<Account> accounts = accountRepository.findAll(
                PageRequest.of(requestDto.getPage(), requestDto.getSize()));
        return accounts.map(accountMapper::toDto);
    }

    @CacheEvict(value = {"account", "accounts"}, allEntries = true)
    @Operation(summary = "Clear all account caches")
    @PostMapping("/clear-cache")
    public ResponseEntity<String> clearCache() {
        return ResponseEntity.ok("All account caches cleared");
    }
}
