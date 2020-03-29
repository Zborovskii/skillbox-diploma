package ru.skillbox.dto;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class PostRequest {
    private LocalDateTime time;
    private Boolean active;
    private String title;
    private String text;
    private Set<String> tags;
}
