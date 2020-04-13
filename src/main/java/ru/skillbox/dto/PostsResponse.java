package ru.skillbox.dto;

import java.util.List;
import lombok.Data;

@Data
public class PostsResponse {

    private int count;
    private List<PlainPostDto> posts;

}
