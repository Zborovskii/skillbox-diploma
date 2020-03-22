package ru.skillbox.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    private final String rootPath;
    private final String referenceOfImagesStorage;
    private final Integer randomLeftLimit;
    private final Integer randomRightLimit;
    private final Integer randomTargetStringLength;

    public StorageService(@Value("${referenceOfImagesStorage}") String referenceOfImagesStorage,
                          @Value("${randomLeftLimit}") Integer randomLeftLimit,
                          @Value("${randomRightLimit}") Integer randomRightLimit,
                          @Value("${randomTargetStringLength}") Integer randomTargetStringLength) {
        this.rootPath = new File("").getAbsolutePath().concat(referenceOfImagesStorage);
        this.referenceOfImagesStorage = referenceOfImagesStorage;
        this.randomLeftLimit = randomLeftLimit;
        this.randomRightLimit = randomRightLimit;
        this.randomTargetStringLength = randomTargetStringLength;
    }

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
        Random random = new Random();

        return random.ints(randomLeftLimit, randomRightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(randomTargetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }
}
