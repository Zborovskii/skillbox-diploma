package ru.skillbox.model;

import lombok.Data;

@Data
public class GeneralBlogInfo {

    private final String title;
    private final String subtitle;
    private final String phone;
    private final String email;
    private final String copyright;
    private final String copyrightFrom;

}
