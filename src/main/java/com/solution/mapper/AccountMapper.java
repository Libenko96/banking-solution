package com.solution.mapper;

import com.solution.dto.AccountDTO;
import com.solution.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDTO toDTO(Account account);

    Account toEntity(AccountDTO accountDTO);

    List<AccountDTO> toDTOs(List<Account> accounts);
}
