package ru.skillbox.dto;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class CalendarResponse {

    private List<Integer> years;
    private Map<String, Long> posts;

}
