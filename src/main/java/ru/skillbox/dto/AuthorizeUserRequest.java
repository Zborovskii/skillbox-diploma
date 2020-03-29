package ru.skillbox.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthorizeUserRequest {
    @JsonProperty(value = "e_mail")
    private String email;
    private String password;
}
