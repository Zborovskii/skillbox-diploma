package ru.skillbox.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.skillbox.enums.Decision;
import ru.skillbox.enums.ModerationStatus;
import ru.skillbox.enums.PostModerationStatus;
import ru.skillbox.enums.SortMode;
import ru.skillbox.enums.StatisticsType;
import ru.skillbox.enums.Vote;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ModerationStatus.StringToEnumConverter());
        registry.addConverter(new PostModerationStatus.StringToEnumConverter());
        registry.addConverter(new SortMode.StringToEnumConverter());
        registry.addConverter(new Decision.StringToEnumConverter());
        registry.addConverter(new Vote.StringToEnumConverter());
        registry.addConverter(new StatisticsType.StringToEnumConverter());
    }
}
