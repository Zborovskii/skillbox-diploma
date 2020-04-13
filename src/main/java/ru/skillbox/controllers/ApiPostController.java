package ru.skillbox.controllers;

import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.dto.PostRequest;
import ru.skillbox.dto.PostWithCommentsResponse;
import ru.skillbox.dto.PostsResponse;
import ru.skillbox.dto.ResultResponse;
import ru.skillbox.enums.ModerationStatus;
import ru.skillbox.enums.PostModerationStatus;
import ru.skillbox.enums.SortMode;
import ru.skillbox.enums.Vote;
import ru.skillbox.services.ResponseService;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    @Autowired
    private ResponseService responseService;

    @GetMapping("/search")
    public ResponseEntity<PostsResponse> getPostsByQuery(
        @RequestParam Integer offset,
        @RequestParam Integer limit,
        @RequestParam String query) {
        return responseService.getPostsByQuery(offset, limit, query);
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostsResponse> getPostsByDate(
        @RequestParam Integer offset,
        @RequestParam Integer limit,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
        return responseService.getPostsByDate(offset, limit, date);
    }

    @GetMapping("")
    public ResponseEntity<PostsResponse> getPosts(
        @RequestParam Integer offset,
        @RequestParam Integer limit,
        @RequestParam SortMode mode) {
        return responseService.getPosts(offset, limit, mode);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsResponse> getPostsByTag(
        @RequestParam Integer offset,
        @RequestParam Integer limit,
        @RequestParam String tag) {
        return responseService.getPostsByTag(offset, limit, tag);
    }

    @GetMapping("/moderation")
    public ResponseEntity<PostsResponse> getPostsModeration(
        @RequestParam Integer offset,
        @RequestParam Integer limit,
        @RequestParam ModerationStatus status) {
        return responseService.getModeratedPosts(offset, limit, status);
    }

    @PostMapping("")
    public ResponseEntity<?> addNewPost(@RequestBody PostRequest request) {
        return responseService.addNewPost(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPost(
        @PathVariable Integer id,
        @RequestBody PostRequest request) {
        return responseService.editPost(id, request);
    }

    @GetMapping("/my")
    public ResponseEntity<PostsResponse> getMyPosts(
        @RequestParam Integer offset,
        @RequestParam Integer limit,
        @RequestParam PostModerationStatus status) {
        status = (status == null) ? PostModerationStatus.INACTIVE : status;
        return responseService.getMyPosts(offset, limit, status);
    }

    @PostMapping("/{vote}")
    public ResponseEntity<ResultResponse> votePost(@PathVariable Vote vote, @RequestBody Map<String, Integer> body) {
        return responseService.votePost(vote, body.getOrDefault("post_id", 0));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostWithCommentsResponse> getPostById(@PathVariable Integer id) {
        return responseService.getPostWithCommentsResponse(id);
    }
}
