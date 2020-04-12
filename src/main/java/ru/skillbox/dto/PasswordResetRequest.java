package ru.skillbox.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {

    private String code;
    private String password;
    private String captcha;
    private String captchaSecret;
}
