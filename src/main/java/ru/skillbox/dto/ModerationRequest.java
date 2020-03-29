package ru.skillbox.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.enums.Decision;

@Data
public class ModerationRequest {

    @JsonProperty(value = "post_id")
    private Integer postId;
    private Decision decision;

}
