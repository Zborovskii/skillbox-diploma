package ru.skillbox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.dto.NewCommentRequest;
import ru.skillbox.dto.ResultResponse;
import ru.skillbox.services.ResponseService;

@RestController
@RequestMapping("/api/comment")
public class ApiCommentController {

    @Autowired
    private ResponseService responseService;

    @PostMapping("")
    public ResponseEntity<ResultResponse> addComment(@RequestBody NewCommentRequest comment) {
        return responseService.comment(comment);
    }
}
