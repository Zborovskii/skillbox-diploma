package ru.skillbox.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestorePasswordRequest {

    @NotBlank
    private String email;
}
