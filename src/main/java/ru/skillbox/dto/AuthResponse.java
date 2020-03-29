package ru.skillbox.dto;

import lombok.Data;

@Data
public class AuthResponse extends ResultResponse {
    private UserAdditionalInfoDto user;
}
