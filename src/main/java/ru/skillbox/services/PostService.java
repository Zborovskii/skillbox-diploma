package ru.skillbox.services;

import ru.skillbox.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.skillbox.enums.ModerationStatus.ACCEPTED;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;


    public List<Post> getAllPostsFromRepository(boolean isActive) {
        List<Post> postList = new ArrayList<>();
        postRepository.findAll().forEach(postList::add);
        if (isActive) {
            postList = postList.stream().filter(p -> p.isActive() &&
                    ACCEPTED.equals(p.getModerationStatus()) &&
                    p.getTime().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }
        return postList;
    }

    public List<Post> searchByDate(List<Post> list, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return list.stream()
                .filter(post -> post.getTime().isAfter(dateFrom) &&
                        post.getTime().isBefore(dateTo))
                .collect(Collectors.toList());
    }
}
