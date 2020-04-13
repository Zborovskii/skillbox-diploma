package ru.skillbox.dto;

import java.util.List;
import lombok.Data;

@Data
public class TagsResponse {

    private List<TagDto> tags;

}
