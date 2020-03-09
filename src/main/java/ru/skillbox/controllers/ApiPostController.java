package ru.skillbox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.dto.PostWithCommentsResponse;
import ru.skillbox.dto.PostsResponse;
import ru.skillbox.services.ResponseService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class ApiPostController {
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private ResponseService responseService;

    @GetMapping("/api/post")
    public ResponseEntity<PostsResponse> getPosts(@RequestParam Integer offset,
                                                  @RequestParam Integer limit,
                                                  @RequestParam String mode) {
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, mode,
                null, null, null), HttpStatus.OK);
    }

    @GetMapping("/api/post/search")
    public ResponseEntity<PostsResponse> getPostsByQuery(@RequestParam Integer offset,
                                                         @RequestParam Integer limit,
                                                         @RequestParam String query) {
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, null, query,
                null, null), HttpStatus.OK);
    }

    @GetMapping("/api/post/{id}")
    public ResponseEntity<PostWithCommentsResponse> getPostById(@PathVariable Integer id) {
        return new ResponseEntity<>(responseService.getPostWithCommentsResponse(id), HttpStatus.OK);
    }

    @GetMapping("/api/post/byDate")
    public ResponseEntity<PostsResponse> getPostsByDate(@RequestParam Integer offset,
                                                        @RequestParam Integer limit,
                                                        @RequestParam String date) {
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, null,
                null, LocalDateTime.parse(date, DATE_FORMATTER), null), HttpStatus.OK);
    }

    @GetMapping("/api/post/byTag")
    public ResponseEntity<PostsResponse> getPostsByTag(@RequestParam Integer offset,
                                                       @RequestParam Integer limit,
                                                       @RequestParam String tag) {
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, null,
                null, null, tag), HttpStatus.OK);
    }


}
