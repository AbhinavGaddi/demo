package com.example.demo.service;

import com.example.demo.dao.AccountRepo;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.model.Account;
import com.example.demo.schema.AccountRequestDto;
import com.example.demo.schema.AccountResponseDto;
import com.example.demo.schema.AccountUpdateDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountService(AccountRepo accountRepo, AccountMapper accountMapper) {
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
    }

    public AccountResponseDto createAccount(@Valid AccountRequestDto accountRequestDto) {
        Account account = accountMapper.toEntity(accountRequestDto);
        Account savedaccount = accountRepo.save(account);
        return accountMapper.toAccountResponseDto(savedaccount);


    }

    public List<AccountResponseDto> getAllAccounts() {
        List<Account> accounts = accountRepo.findAll();
        return accounts.stream().map(accountMapper::toAccountResponseDto).collect(Collectors.toList());
    }

    public AccountResponseDto updateAccount(@Valid AccountUpdateDto accountUpdateDto) {
        Optional<Account> optionalAccount = accountRepo.findByAccountNumber(accountUpdateDto.getAccountNumber());
        if (!optionalAccount.isPresent()) {
            throw new RuntimeException("Account not found");
        }
        Account account = optionalAccount.get();
        account.setAccountName(accountUpdateDto.getNewAccountName());
        Account savedaccount = accountRepo.save(account);
        return accountMapper.toAccountResponseDto(savedaccount);
    }
}
