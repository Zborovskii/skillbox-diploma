package ru.skillbox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.env.Environment;
import ru.skillbox.YAMLConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

@Service
public class StorageService {

    @Value("${getReferenceOfImagesStorage}")
    private String getReferenceOfImagesStorage;

    private final String rootPath = new File("").getAbsolutePath()
//            .concat(getReferenceOfImagesStorage);
            .concat("/src/main/java/ru/skillbox/blog_engine");

    public String store(MultipartFile file) {
        String absolutePathToFolder = "/upload/" + generatePathPart() + "/" + generatePathPart() + "/";
        new File(rootPath + absolutePathToFolder).mkdirs();
        String path = absolutePathToFolder + file.getOriginalFilename();

        try {
            Files.copy(file.getInputStream(), Paths.get(rootPath + path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }

    private String generatePathPart() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 10;

        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
