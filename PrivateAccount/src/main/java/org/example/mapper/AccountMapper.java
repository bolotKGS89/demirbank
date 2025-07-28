package org.example.mapper;

import org.example.dtos.AccountDto;
import org.example.model.Account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    AccountDto toDto(Account account);

    Account toEntity(AccountDto dto);
}
