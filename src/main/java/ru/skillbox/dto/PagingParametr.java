package ru.skillbox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PagingParametr {

    private Integer offset;
    private Integer limit;
    private String mode;
    private String query;
    private String date;
    private String tag;
}
