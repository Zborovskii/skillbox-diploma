package ru.skillbox.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentDto {

    private Integer id;
    private LocalDateTime time;
    private UserWithPhotoDto user;
    private String text;

}
