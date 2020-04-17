package ru.skillbox.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PlainPostDto {

    private Integer id;
    @JsonFormat(pattern = "hh:mm dd.MM.yyyy")
    private LocalDateTime time;
    private UserDto user;
    private String title;
    private String announce;
    private Long likeCount;
    private Long dislikeCount;
    private Integer commentCount;
    private Integer viewCount;

}
