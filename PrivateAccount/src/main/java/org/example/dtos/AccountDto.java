package org.example.dtos;

import lombok.Data;

@Data
public class AccountDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
