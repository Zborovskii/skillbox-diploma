package ru.skillbox.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.PostRequest;
import ru.skillbox.dto.PostWithCommentsResponse;
import ru.skillbox.enums.Decision;
import ru.skillbox.enums.ModerationStatus;
import ru.skillbox.mappers.EntityMapper;
import ru.skillbox.model.Post;
import ru.skillbox.model.Tag;
import ru.skillbox.model.User;
import ru.skillbox.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private TagService tagService;

    public List<Post> getPostsByQuery(String searchQuery) {
        return postRepository.findPostsByQuery(searchQuery);
    }

    public List<Post> getPostsByDate(LocalDateTime date) {
        return postRepository.findPostsByDate(date, date.plusDays(1).minusSeconds(1));
    }

    public List<Post> getPosts() {
        return postRepository.findPosts();
    }

    public List<Post> getPostsByModerationStatus(User user, ModerationStatus moderationStatus) {
        return postRepository.findPostsByModerationStatus(user, moderationStatus);
    }

    public List<Post> getMyPosts(User user, ModerationStatus moderationStatus, Boolean isActive) {
        return postRepository.findMyPosts(user, moderationStatus, isActive);
    }

    public List<Post> searchByDate(List<Post> list, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return list.stream()
            .filter(post -> post.getTime().isAfter(dateFrom) &&
                post.getTime().isBefore(dateTo))
            .collect(Collectors.toList());
    }

    public List<Post> searchByTag(List<Post> postList, String tagQuery) {
        return postList.stream()
            .filter(post -> post.getTags().stream()
                .map(Tag::getName)
                .anyMatch(tag -> tag.toLowerCase().startsWith(tagQuery.toLowerCase())))
            .collect(Collectors.toList());
    }

    public PostWithCommentsResponse getPostWithCommentsById(Integer id) {

        return Optional.ofNullable(postRepository.findPostsByById(id))
            .map(entityMapper::postToPostWithCommentsDto)
            .orElse(null);
    }

    public Optional<Post> getPostById(Integer id) {
        return postRepository.findById(id);
    }

    public Post updatePostModerationStatus(User moderator, Post post, Decision decision) {
        ModerationStatus status = (decision == Decision.ACCEPT) ?
                                  ModerationStatus.ACCEPTED : ModerationStatus.DECLINED;
        post.setModerationStatus(status);
        post.setModeratedBy(moderator);
        return postRepository.save(post);
    }

    public Integer countByUser(User user) {
        return postRepository.countByUser(user);
    }

    public Integer countViewsByUser(User user) {
        Integer result = postRepository.getViewsByUser(user);
        result = result != null ? result : 0;
        return result;
    }

    public String getFirstPostDate(User user) {
        return postRepository.getFirstPostDateByUser(user);
    }

    public Post savePost(Post post, PostRequest postData, User editor) {
        final Post postToSave = (post == null) ? new Post() : post;
        final LocalDateTime NOW = LocalDateTime.now();
        postToSave.setTitle(postData.getTitle());
        postToSave.setText(postData.getText());
        postToSave.setIsActive(postData.getActive());
        if (postData.getTime() != null) {
            postToSave.setTime(postData.getTime().isBefore(NOW) ? NOW : postData.getTime());
        } else {
            postToSave.setTime(NOW);
        }
        postToSave.setUser((postToSave.getId() == null) ? editor :
                           postToSave.getUser());
        if ((post == null) || (editor.equals(postToSave.getUser()) && !editor.getIsModerator())) {
            postToSave.setModerationStatus(ModerationStatus.NEW);
        }
        if (postData.getTags() != null) {
            postData.getTags().forEach(tag -> postToSave.getTags().add(tagService.saveTag(tag)));
        }
        return postRepository.save(postToSave);
    }
}
