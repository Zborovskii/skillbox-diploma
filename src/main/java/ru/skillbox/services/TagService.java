package ru.skillbox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.TagDto;
import ru.skillbox.mappers.EntityMapper;
import ru.skillbox.model.Post;
import ru.skillbox.model.Tag;
import ru.skillbox.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private EntityMapper entityMapper;

    public List<Tag> getAllTagsFromRepository() {
        List<Tag> tagList = new ArrayList<>();
        tagRepository.findAll().forEach(tagList::add);
        return tagList;
    }

    public List<TagDto> getAllTagDtoList() {
        List<Post> allPostList = postService.getAllPostsFromRepository(true);
        List<Tag> allTagList = getAllTagsFromRepository();
        return allTagList.stream()
                .map(tag -> entityMapper.tagToTagDto(tag, allPostList.size()))
                .collect(Collectors.toList());
    }

    public List<TagDto> getTagDtoListByQuery(String query) {
        return getAllTagDtoList().stream()
                .filter(tag -> tag.getName().toLowerCase().startsWith(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
