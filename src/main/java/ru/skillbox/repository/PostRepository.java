package ru.skillbox.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.skillbox.enums.ModerationStatus;
import ru.skillbox.model.Post;
import ru.skillbox.model.User;

public interface PostRepository extends CrudRepository<Post, Integer> {

    @Query("SELECT COUNT(*) FROM Post p WHERE (:user IS NULL OR p.user = :user)")
    Integer countByUser(@Param("user") User user);

    @Query("SELECT SUM(p.viewCount) FROM Post p WHERE (:user IS NULL OR p.user = :user)")
    Integer getViewsByUser(@Param("user") User user);

    @Query("SELECT DATE_FORMAT(MIN(p.time),'%Y-%m-%d %H:%m') " +
        "FROM Post p WHERE (:user IS NULL OR p.user = :user)")
    String getFirstPostDateByUser(@Param("user") User user);

    @Query("SELECT p FROM Post p WHERE (p.text LIKE %:query% OR p.title LIKE %:query%) AND (p.time < CURRENT_DATE() "
        + "AND p.isActive=true AND p.moderationStatus = 'ACCEPTED')")
    List<Post> findPostsByQuery(String query);

    @Query("SELECT p FROM Post p WHERE (p.time < CURRENT_DATE() AND p.time > :dateStart AND p.time<:dateFinish AND p"
        + ".isActive=true AND p.moderationStatus = 'ACCEPTED')")
    List<Post> findPostsByDate(LocalDateTime dateStart, LocalDateTime dateFinish);

    @Query("SELECT p FROM Post p WHERE (p.time < CURRENT_DATE() AND p.isActive=true AND p.moderationStatus = "
        + "'ACCEPTED')")
    List<Post> findPosts();

    @Query("SELECT p FROM Post p WHERE (p.isActive=true AND p.moderationStatus = "
        + ":status AND p.user = :user)")
    List<Post> findPostsByModerationStatus(User user, ModerationStatus status);

    @Query("SELECT p FROM Post p WHERE (p.isActive=:isActive AND p.moderationStatus = "
        + ":status AND p.user = :user)")
    List<Post> findMyPosts(User user, ModerationStatus status, Boolean isActive);

    @Query("SELECT p FROM Post p WHERE (p.id = :id AND p.time < CURRENT_DATE() AND p.isActive=true AND p"
        + ".moderationStatus = 'ACCEPTED')")
    Post findPostsByById(Integer id);
}
