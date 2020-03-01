package ru.skillbox.services;

import ru.skillbox.dto.CalendarResponse;
import ru.skillbox.model.GeneralBlogInfo;
import ru.skillbox.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResponseService {

    @Autowired
    private PostService postService;


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

}
