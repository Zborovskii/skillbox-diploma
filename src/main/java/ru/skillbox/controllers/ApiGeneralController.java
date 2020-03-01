package ru.skillbox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.dto.CalendarResponse;
import ru.skillbox.services.ResponseService;
import ru.skillbox.services.StorageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class ApiGeneralController {
    DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");

    @Autowired
    private ResponseService responseService;
    @Autowired
    private StorageService storageService;

    @GetMapping(name = "/api/init")
    public ResponseEntity getGeneralBlogInfo() {

        return new ResponseEntity(responseService.getGeneralBlogInfo(), HttpStatus.OK);
    }

    @PostMapping("/api/image")
    public ResponseEntity<String> postImage(@RequestParam("image") MultipartFile image) {
        return new ResponseEntity<>(storageService.store(image), HttpStatus.OK);
    }

    @GetMapping("/api/calendar")
    public ResponseEntity<CalendarResponse> getCalendar(@RequestParam String year) {
        return new ResponseEntity<>(responseService.getCalendarResponse(LocalDateTime.parse(year, YEAR_FORMATTER)),
                HttpStatus.OK);
    }


}
