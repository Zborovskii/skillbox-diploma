package ru.skillbox.dto;

import lombok.Data;

@Data
public class UserAdditionalInfoDto extends UserWithPhotoDto {

    private boolean moderation;
    private Integer moderationCount;
    private boolean settings;
    private String email;
}
