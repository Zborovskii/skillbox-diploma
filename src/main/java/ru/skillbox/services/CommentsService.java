package ru.skillbox.services;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.model.Post;
import ru.skillbox.model.PostComment;
import ru.skillbox.model.User;
import ru.skillbox.repository.CommentsRepository;

@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    public Optional<PostComment> findCommentById(Integer id) {
        return commentsRepository.findById(id);
    }

    public PostComment addComment(User user, PostComment parentComment, Post post, String text) {
        PostComment newComment = new PostComment();
        newComment.setParentPostComment(parentComment);
        newComment.setUser(user);
        newComment.setPost(post);
        newComment.setText(text);
        newComment.setTime(LocalDateTime.now());
        PostComment savedComment = commentsRepository.save(newComment);
        return savedComment;
    }


}
