package ru.skillbox.dto;

import lombok.Data;

@Data
public class ResultResponse {

    private boolean result;
    private Error errors;

}
