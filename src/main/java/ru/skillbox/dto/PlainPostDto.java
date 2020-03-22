package ru.skillbox.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PlainPostDto {

    private Integer id;
    private LocalDateTime time;
    private UserDto user;
    private String title;
    private String announce;
    private Long likeCount;
    private Long dislikeCount;
    private Integer commentCount;
    private Integer viewCount;

}
