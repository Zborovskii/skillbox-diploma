package ru.skillbox.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByIsModeratorTrue();
    List<User> findByIsModeratorFalse();
    User findByEmail(String email);
    User findByCode(String code);
}
