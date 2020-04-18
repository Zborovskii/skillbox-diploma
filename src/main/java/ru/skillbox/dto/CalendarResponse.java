package ru.skillbox.dto;

import java.util.Map;
import java.util.Set;
import lombok.Data;

@Data
public class CalendarResponse {

    private Set<Integer> years;
    private Map<String, Long> posts;

}
