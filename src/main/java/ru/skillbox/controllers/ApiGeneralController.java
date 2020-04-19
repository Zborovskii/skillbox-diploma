package ru.skillbox.controllers;

import java.io.File;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.dto.CalendarResponse;
import ru.skillbox.dto.ModerationRequest;
import ru.skillbox.dto.ResultResponse;
import ru.skillbox.dto.SettingsValues;
import ru.skillbox.dto.StatisticsDto;
import ru.skillbox.dto.TagsResponse;
import ru.skillbox.enums.StatisticsType;
import ru.skillbox.model.GeneralBlogInfo;
import ru.skillbox.services.ResponseService;
import ru.skillbox.services.SettingsService;
import ru.skillbox.services.StatisticsService;
import ru.skillbox.services.StorageService;

@RestController
public class ApiGeneralController {

    @Autowired
    @Value("${spring.image.reference}")
    private String IMAGE_REFERENCE;
    @Autowired
    @Value("${spring.image.reference.default}")
    private String IMAGE_REFERENCE_DEFAULT;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/api/image")
    public ResponseEntity<String> postImage(@RequestParam("image") MultipartFile image) {
        return new ResponseEntity<>(storageService.store(image), HttpStatus.OK);
    }

    @GetMapping(value = "/upload/{path1}/{path2}/{fileName}")
    public @ResponseBody
    byte[] getImage(@PathVariable String path1,
                    @PathVariable String path2,
                    @PathVariable String fileName) {
        String route;

        if (path1.equals("default")) {
            route = new File("").getAbsolutePath()
                .concat(IMAGE_REFERENCE_DEFAULT);
        } else {
            route = new File("").getAbsolutePath()
                .concat(IMAGE_REFERENCE + path1 + "/" + path2 + "/" + fileName);
        }

        return storageService.getImage(Path.of(route));
    }

    /**
     * логики не должно быть в контроллере
     * формирование пути до расположения картинки не корректное, как вариант использовать одну переменную path в
     * которой будет весь путь целиком
     * сейчас сделан костыль с дефолтным путем (если пользователь не загрузил картинку), на самом деле нужно при
     * сохранении в БД инфы от пользователя проверять, есть ли картинка, и если нет то заносить в БД дефолтный путь
     */

    @GetMapping("/api/calendar")
    public ResponseEntity<CalendarResponse> getCalendar(@RequestParam(required = false) String year) {
        return new ResponseEntity<>(responseService.getCalendarResponse(year), HttpStatus.OK);
    }

    @GetMapping("/api/tag")
    public ResponseEntity<TagsResponse> getTags(@RequestParam(required = false) String query) {
        return new ResponseEntity<>(responseService.getTagsResponse(query), HttpStatus.OK);
    }

    @PostMapping("/api/moderation")
    public ResponseEntity<ResultResponse> postModeration(@RequestBody ModerationRequest moderationRequest) {
        return responseService.moderate(moderationRequest);
    }

    @PutMapping("/api/settings")
    public ResponseEntity<SettingsValues> updateSettings(@RequestBody SettingsValues settings) {
        return statisticsService.updateSettings(settings);
    }

    @GetMapping("/api/settings")
    public ResponseEntity<SettingsValues> getSettings() {
        return new ResponseEntity<>(settingsService.getSettings(), HttpStatus.OK);
    }

    @GetMapping("/api/statistics/{statisticsType}")
    public ResponseEntity<StatisticsDto> getStatistics(@PathVariable StatisticsType statisticsType) {
        return statisticsService.getStatistics(statisticsType);
    }

    @GetMapping("/api/init")
    public ResponseEntity<GeneralBlogInfo> getApiInit() {
        return new ResponseEntity<>(responseService.getGeneralBlogInfo(), HttpStatus.OK);
    }
}
