package com.example.demo.mapper;

import com.example.demo.model.Account;
import com.example.demo.schema.AccountRequestDto;
import com.example.demo.schema.AccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
    @Mapping(target="accountNumber",expression = "java(generateAccountNumber())")
    @Mapping(target = "balance",constant = "0")
    Account toEntity(AccountRequestDto accountRequestDto);
    AccountResponseDto toAccountResponseDto(Account account);
    default String generateAccountNumber() {
        return java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
