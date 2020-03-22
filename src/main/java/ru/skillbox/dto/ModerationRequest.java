package ru.skillbox.dto;

import lombok.Data;
import ru.skillbox.enums.Decision;

@Data
public class ModerationRequest {

    private Integer post_id;
    private Decision decision;

}
