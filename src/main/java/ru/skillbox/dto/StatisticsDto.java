package ru.skillbox.dto;

import lombok.Data;

@Data
public class StatisticsDto {

    private long postsCount;

    private long likesCount;

    private long dislikesCount;

    private long viewsCount;

    private String firstPublication;
}
