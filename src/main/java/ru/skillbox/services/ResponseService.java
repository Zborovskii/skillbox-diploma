package ru.skillbox.services;

import ru.skillbox.dto.*;
import ru.skillbox.mappers.EntityMapper;
import ru.skillbox.model.GeneralBlogInfo;
import ru.skillbox.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResponseService {

    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;
    @Autowired
    private EntityMapper entityMapper;


    public CalendarResponse getCalendarResponse(LocalDateTime year) {
        List<Post> allPostList = postService.getAllPostsFromRepository(true);
        List<Post> postList = postService.searchByDate(allPostList, year, LocalDateTime.now());

        Map<String, Long> postsCountPerYear = postList.stream()
                .collect(Collectors.groupingBy(p -> p.getTime().toString().split(" ")[0],
                        Collectors.counting()));
        List<Integer> postYears = postList.stream()
                .map(p -> p.getTime().getYear())
                .collect(Collectors.toList());

        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.setYears(postYears);
        calendarResponse.setPosts(postsCountPerYear);

        return calendarResponse;
    }

    public GeneralBlogInfo getGeneralBlogInfo() {

        return new GeneralBlogInfo("DevPub",
                "Рассказы разработчиков",
                "+7 903 666-44-55",
                "mail@mail.ru",
                "Дмитрий Сергеев",
                "2005");
    }

    public TagsResponse getTagsResponse(String tagQuery) {
        List<TagDto> tagDtoList = tagService.getTagDtoListByQuery(tagQuery);
        TagsResponse tagsResponse = new TagsResponse();
        tagsResponse.setTags(tagDtoList);
        return tagsResponse;
    }

    public PostsResponse getPostsResponse(Integer offset,
                                          Integer limit,
                                          String sortMode,
                                          String searchQuery,
                                          LocalDateTime ldt,
                                          String tag) {
        List<Post> postList = postService.getAllPostsFromRepository(true);
        if (searchQuery != null) {
            postList = postService.searchByQuery(postList, searchQuery);
        }
        if (ldt != null) {
            LocalDateTime dateTo = ldt.plusDays(1).minusSeconds(1);
            postList = postService.searchByDate(postList, ldt, dateTo);
        }
        if (tag != null) {
            postList = postService.searchByTag(postList, tag);
        }
        List<PlainPostDto> plainPostDtoList = postList.stream()
                .map(entityMapper::postToPlainPostDto)
                .collect(Collectors.toList());
        if (sortMode != null) {
            plainPostDtoList = sortPlainPostDtoListByMode(plainPostDtoList, sortMode);
        }
        return formPostsResponse(offset, limit, plainPostDtoList);
    }

    private List<PlainPostDto> sortPlainPostDtoListByMode(List<PlainPostDto> list, String mode) {
        switch (mode) {
            case "best":
                list.sort(Comparator.comparing(PlainPostDto::getLikeCount).reversed());
                break;
            case "early":
                list.sort(Comparator.comparing(PlainPostDto::getTime));
                break;
            case "recent":
                list.sort(Comparator.comparing(PlainPostDto::getTime).reversed());
                break;
            case "popular":
                list.sort(Comparator.comparing(PlainPostDto::getCommentCount).reversed());
                break;
        }
        return list;
    }

    private <T> List<T> offsetList(Integer offset, Integer limit, List<T> list) {
        if (list.isEmpty() || offset > list.size()) {
            return Collections.EMPTY_LIST;
        }
        return list.subList(offset, limit + offset <= list.size() ? limit + offset : list.size());
    }

    private PostsResponse formPostsResponse(Integer offset, Integer limit, List<PlainPostDto> posts) {
        PostsResponse postsResponse = new PostsResponse();
        postsResponse.setCount(posts.size());
        postsResponse.setPosts(offsetList(offset, limit, posts));
        return postsResponse;
    }


    public PostWithCommentsResponse getPostWithCommentsResponse(Integer id) {
        return postService.getPostWithCommentsById(id);
    }
}