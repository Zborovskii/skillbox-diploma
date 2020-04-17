package ru.skillbox.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PostWithCommentsResponse {

    private Integer id;
    @JsonFormat(pattern = "hh:mm dd.MM.yyyy")
    private LocalDateTime time;
    private UserDto user;
    private String title;
    private String text;
    private Long likeCount;
    private Long dislikeCount;
    private Integer viewCount;
    private List<CommentDto> comments;
    private List<String> tags;

}
