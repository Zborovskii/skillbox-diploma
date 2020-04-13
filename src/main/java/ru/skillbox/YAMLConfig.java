package ru.skillbox;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {

    private String referenceOfImagesStorage;

    public String getReferenceOfImagesStorage() {
        return referenceOfImagesStorage;
    }

    public void setReferenceOfImagesStorage(String referenceOfImagesStorage) {
        this.referenceOfImagesStorage = referenceOfImagesStorage;
    }
}

