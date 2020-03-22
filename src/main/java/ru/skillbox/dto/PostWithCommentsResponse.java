package ru.skillbox.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PostWithCommentsResponse {

    private Integer id;
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
