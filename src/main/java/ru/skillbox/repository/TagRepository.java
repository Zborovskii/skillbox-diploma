package ru.skillbox.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillbox.model.Tag;

public interface TagRepository extends CrudRepository<Tag, Integer> {
    Tag findByNameIgnoreCase(String name);
}
