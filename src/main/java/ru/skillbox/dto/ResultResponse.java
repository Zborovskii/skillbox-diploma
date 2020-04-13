package ru.skillbox.dto;

import java.util.Map;
import lombok.Data;

@Data
public class ResultResponse {

    private boolean result;
    private Map<String, Object> errors;

}
