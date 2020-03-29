package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.model.PostComment;

public interface CommentsRepository extends JpaRepository<PostComment, Integer> {

}
