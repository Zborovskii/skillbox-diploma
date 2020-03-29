package ru.skillbox.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.dto.CalendarResponse;
import ru.skillbox.dto.ModerationRequest;
import ru.skillbox.dto.ResultResponse;
import ru.skillbox.dto.SettingsValues;
import ru.skillbox.dto.StatisticsDto;
import ru.skillbox.dto.TagsResponse;
import ru.skillbox.enums.StatisticsType;
import ru.skillbox.model.User;
import ru.skillbox.services.AuthService;
import ru.skillbox.services.ResponseService;
import ru.skillbox.services.SettingsService;
import ru.skillbox.services.StatisticsService;
import ru.skillbox.services.StorageService;

@RestController
public class ApiGeneralController {

    DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");

    @Autowired
    private ResponseService responseService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AuthService authService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private StatisticsService statisticsService;

//    @GetMapping(name = "/api/init")
//    public ResponseEntity<GeneralBlogInfo> getGeneralBlogInfo() {
//        return new ResponseEntity(responseService.getGeneralBlogInfo(), HttpStatus.OK);
//    }

    @PostMapping("/api/image")
    public ResponseEntity<String> postImage(@RequestParam("image") MultipartFile image) {
        return new ResponseEntity<>(storageService.store(image), HttpStatus.OK);
    }

    @GetMapping("/api/calendar")
    public ResponseEntity<CalendarResponse> getCalendar(@RequestParam String year) {
        return new ResponseEntity<>(responseService.getCalendarResponse(LocalDateTime.parse(year, YEAR_FORMATTER)),
                                    HttpStatus.OK);
    }

    @GetMapping("/api/tag")
    public ResponseEntity<TagsResponse> getTags(@RequestParam String query) {
        return new ResponseEntity<>(responseService.getTagsResponse(query), HttpStatus.OK);
    }

    @PostMapping("/api/moderation")
    public ResponseEntity<ResultResponse> postModeration(@RequestBody ModerationRequest moderationRequest) {
        return responseService.moderate(moderationRequest);
    }

    @PutMapping("/api/settings")
    public ResponseEntity<SettingsValues> updateSettings(@RequestBody SettingsValues settings) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        if (!user.getIsModerator()) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        SettingsValues updatedSettings = settingsService.saveSettings(settings);
        return new ResponseEntity<>(updatedSettings, HttpStatus.OK);
    }

    @GetMapping("/api/settings")
    public ResponseEntity<SettingsValues> getSettings() {
        return new ResponseEntity<>(settingsService.getSettings(), HttpStatus.OK);
    }

    @GetMapping("/api/statistics/{statisticsType}")
    public ResponseEntity<StatisticsDto> getStatistics(@PathVariable StatisticsType statisticsType) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        boolean isStatsPublic = settingsService.isStatsPublic();
        if (userOptional.isPresent()) {
            if (StatisticsType.ALL.equals(statisticsType) && isStatsPublic) {
                return new ResponseEntity<>(statisticsService.getStatistics(null), HttpStatus.OK);
            }
            return new ResponseEntity<>(statisticsService.getStatistics(userOptional.get()), HttpStatus.OK);
        }
        if (isStatsPublic) {
            return new ResponseEntity<>(statisticsService.getStatistics(null), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
