package ru.skillbox.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.AuthResponse;
import ru.skillbox.dto.AuthorizeUserRequest;
import ru.skillbox.dto.CalendarResponse;
import ru.skillbox.dto.CaptchaResponse;
import ru.skillbox.dto.CommentResponse;
import ru.skillbox.dto.ModerationRequest;
import ru.skillbox.dto.NewCommentRequest;
import ru.skillbox.dto.PasswordResetRequest;
import ru.skillbox.dto.PlainPostDto;
import ru.skillbox.dto.PostRequest;
import ru.skillbox.dto.PostWithCommentsResponse;
import ru.skillbox.dto.PostsResponse;
import ru.skillbox.dto.RegisterUserRequest;
import ru.skillbox.dto.ResultResponse;
import ru.skillbox.dto.TagDto;
import ru.skillbox.dto.TagsResponse;
import ru.skillbox.enums.ModerationStatus;
import ru.skillbox.enums.PostModerationStatus;
import ru.skillbox.enums.SortMode;
import ru.skillbox.enums.Vote;
import ru.skillbox.mappers.EntityMapper;
import ru.skillbox.model.CaptchaCode;
import ru.skillbox.model.GeneralBlogInfo;
import ru.skillbox.model.Post;
import ru.skillbox.model.PostComment;
import ru.skillbox.model.User;

@Service
public class ResponseService {

    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private CaptchaCodeService captchaCodeService;
    @Autowired
    private VotesService votesService;
    @Autowired
    private CommentsService commentsService;

    public CalendarResponse getCalendarResponse(String year) {

        Integer currentYear = LocalDate.now().getYear();
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(currentYear, 1, 1),
                                             LocalTime.MIDNIGHT);
        if (year != null) {
            ldt = ldt.withYear(Integer.parseInt(year));
        }
        List<Post> allPostList = postService.getPosts();
        List<Post> postList = postService.searchByDate(allPostList, ldt, LocalDateTime.now());
        Map<String, Long> postsCountPerYear = postList.stream()
            .collect(Collectors.groupingBy(p -> p.getTime().toString().split("T")[0],
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
        GeneralBlogInfo response = new GeneralBlogInfo();
        response.setCopyright("Зборовский Александр");
        response.setCopyrightFrom("2020");
        response.setEmail("shum-127127@mail.ru");
        response.setPhone("+7 910 666-44-55");
        response.setSubtitle("Blog engine");
        response.setTitle("DevPub");

        return response;
    }

    public TagsResponse getTagsResponse(String tagQuery) {
        List<TagDto> tagDtoList = tagService.getTagDtoListByQuery(tagQuery);
        TagsResponse tagsResponse = new TagsResponse();
        tagsResponse.setTags(tagDtoList);
        return tagsResponse;
    }

    public ResponseEntity<PostsResponse> getPostsByQuery(Integer offset, Integer limit, String searchQuery) {

        List<PlainPostDto> posts = postService.getPostsByQuery(searchQuery).stream()
            .map(entityMapper::postToPlainPostDto)
            .collect(Collectors.toList());
        return new ResponseEntity<>(formPostsResponse(offset, limit, posts), HttpStatus.OK);
    }

    public ResponseEntity<PostsResponse> getPostsByDate(Integer offset, Integer limit, LocalDate date) {

        List<PlainPostDto> posts = postService.getPostsByDate(LocalDateTime.of(date, LocalTime.MIDNIGHT)).stream()
            .map(entityMapper::postToPlainPostDto)
            .collect(Collectors.toList());
        return new ResponseEntity<>(formPostsResponse(offset, limit, posts), HttpStatus.OK);
    }

    public ResponseEntity<PostsResponse> getPosts(Integer offset, Integer limit, SortMode mode) {

        List<PlainPostDto> posts = postService.getPosts().stream()
            .map(entityMapper::postToPlainPostDto)
            .collect(Collectors.toList());
        if (mode != null) {
            posts = sortPlainPostDtoListByMode(posts, mode);
        }
        return new ResponseEntity<>(formPostsResponse(offset, limit, posts), HttpStatus.OK);
    }

    public ResponseEntity<PostsResponse> getPostsByTag(Integer offset, Integer limit, String tag) {

        List<PlainPostDto> posts = postService.searchByTag(postService.getPosts(), tag).stream()
            .map(entityMapper::postToPlainPostDto)
            .collect(Collectors.toList());

        return new ResponseEntity<>(formPostsResponse(offset, limit, posts), HttpStatus.OK);
    }

    public ResponseEntity<PostsResponse> getPostsByModeration(Integer offset, Integer limit, User user,
                                                              ModerationStatus status) {

        List<PlainPostDto> posts = postService.getPostsByModerationStatus(user, status).stream()
            .map(entityMapper::postToPlainPostDto)
            .collect(Collectors.toList());
        return new ResponseEntity<>(formPostsResponse(offset, limit, posts), HttpStatus.OK);
    }

    public ResponseEntity<PostsResponse> getMyPosts(Integer offset, Integer limit, User user,
                                                    ModerationStatus status, Boolean isActive) {

        List<PlainPostDto> posts = postService.getMyPosts(user, status, isActive).stream()
            .map(entityMapper::postToPlainPostDto)
            .collect(Collectors.toList());
        return new ResponseEntity<>(formPostsResponse(offset, limit, posts), HttpStatus.OK);
    }

    private List<PlainPostDto> sortPlainPostDtoListByMode(List<PlainPostDto> list, SortMode mode) {
        switch (mode) {
            case BEST:
                list.sort(Comparator.comparing(PlainPostDto::getLikeCount).reversed());
                break;
            case EARLY:
                list.sort(Comparator.comparing(PlainPostDto::getTime));
                break;
            case RECENT:
                list.sort(Comparator.comparing(PlainPostDto::getTime).reversed());
                break;
            case POPULAR:
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

    public ResponseEntity<PostWithCommentsResponse> getPostWithCommentsResponse(Integer id) {
        PostWithCommentsResponse postWithCommentsResponse = postService.getPostWithCommentsById(id);
        if (postWithCommentsResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postWithCommentsResponse, HttpStatus.OK);
    }

    public ResponseEntity<ResultResponse> moderate(ModerationRequest moderationRequest) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        if (!user.getIsModerator()) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        final Optional<Post> postOptional = postService.getPostById(moderationRequest.getPostId());
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        final Post post = postOptional.get();

        final User postModerator = post.getModeratedBy();
        if (postModerator != null && !postModerator.equals(user)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        Post updatedPost = postService.updatePostModerationStatus(user, post, moderationRequest.getDecision());
        ResultResponse result = new ResultResponse();
        result.setResult(updatedPost != null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<AuthResponse> login(AuthorizeUserRequest authorizeUserRequest) {
        AuthResponse response = new AuthResponse();
        User userFromDB = authService.loginUser(authorizeUserRequest);
        boolean result = userFromDB != null;

        if (result) {
            if (!authService.isValidPassword(authorizeUserRequest.getPassword(), userFromDB.getPassword())) {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
            response.setUser(entityMapper.getAuthorizedUserDTO(userFromDB));
        }
        response.setResult(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<AuthResponse> registerUser(RegisterUserRequest request) {
        AuthResponse response = new AuthResponse();
        if (request.getName() == null) {
            request.setName(request.getEmail().substring(0, request.getEmail().indexOf("@")));
        }
        Map<String, Object> errors = validateUserInputAndGetErrors(request);
        boolean result = errors.size() == 0;
        response.setResult(result);
        if (!result) {
            response.setErrors(errors);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.setUser(entityMapper.getAuthorizedUserDTO(authService.registerUser(request)));
        response.setResult(true);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResultResponse> resetUserPassword(PasswordResetRequest request) {
        ResultResponse response = new ResultResponse();
        response.setResult(authService.resetUserPassword(request));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResultResponse> logout() {
        ResultResponse response = new ResultResponse();
        authService.logoutUser();
        response.setResult(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<AuthResponse> getAuthorizedUserResponse() {
        if (authService.getAuthorizedUser().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        AuthResponse response = new AuthResponse();
        Optional<User> authorizedUser = authService.getAuthorizedUser();
        authorizedUser.ifPresent(user -> response.setUser(entityMapper.getAuthorizedUserDTO(user)));
        response.setResult(authService.getAuthorizedUser().isPresent());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResultResponse> restorePassword(String email) {
        ResultResponse response = new ResultResponse();
        response.setResult(authService.restoreUserPassword(email));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<CaptchaResponse> getCaptchaResponse() {
        CaptchaResponse response = new CaptchaResponse();
        CaptchaCode captchaCode = captchaCodeService.getCaptcha();
        response.setImage("data:image/png;base64, "
                              .concat(CaptchaCodeService.generateBase64Image(captchaCode.getCode())));
        response.setSecret(captchaCode.getSecretCode());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<PostsResponse> getModeratedPosts(Integer offset, Integer limit, ModerationStatus status) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User authorizedUser = userOptional.get();
        if (!authorizedUser.getIsModerator()) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return getPostsByModeration(offset, limit, authorizedUser, status);
    }

    public ResponseEntity<ResultResponse> addNewPost(PostRequest request) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        Post savedPost = postService.savePost(null, request, user);
        ResultResponse result = new ResultResponse();
        result.setResult(savedPost != null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<ResultResponse> editPost(Integer id, PostRequest request) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        Optional<Post> postOptional = postService.getPostById(id);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Post post = postOptional.get();
        if (!post.getUser().equals(user) &&
            (!user.getIsModerator() || (post.getModeratedBy() != null &&
                !post.getModeratedBy().equals(user)))) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        Post savedPost = postService.savePost(post, request, user);
        ResultResponse result = new ResultResponse();
        result.setResult(savedPost != null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<PostsResponse> getMyPosts(Integer offset, Integer limit, PostModerationStatus status) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User authorizedUser = userOptional.get();
        status = status == null ? PostModerationStatus.PUBLISHED : status;
        return getMyPosts(offset, limit, authorizedUser, status.getModerationStatus(), status.isActive());
    }

    public ResponseEntity<ResultResponse> votePost(Vote vote, Integer postId) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        if (postId <= 0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Post post = postService.getPostById(postId).orElse(null);
        if (post == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ResultResponse result = new ResultResponse();
        result.setResult(votesService.vote(vote, userOptional.get(), post));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<ResultResponse> comment(NewCommentRequest comment) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        Optional<Post> post = postService.getPostById(comment.getPostId());
        Optional<PostComment> parentComment = Optional.empty();
        if (post.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (comment.getParentId() != null) {
            List<PostComment> postComments = post.get().getPostComments();
            parentComment = commentsService.findCommentById(comment.getParentId());
            if (parentComment.isEmpty() || (!postComments.isEmpty() && !postComments.contains(parentComment.get()))) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        CommentResponse result = new CommentResponse();
        PostComment newComment = commentsService.addComment(user, parentComment.orElse(null),
                                                            post.get(), comment.getText());
        if (newComment != null) {
            result.setResult(true);
            result.setId(newComment.getId());
        } else {
            result.setResult(false);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Map<String, Object> validateUserInputAndGetErrors(RegisterUserRequest user) {
        final String email = user.getEmail();

        final String password = user.getPassword();
        final String captcha = user.getCaptcha();
        final String captchaSecretCode = user.getCaptchaSecret();

        Map<String, Object> errors = new HashMap<>();
        User userFromDB = authService.findUserByEmail(email);

        if (userFromDB != null) {
            errors.put("email", "Этот адрес уже зарегистрирован.");
        }
        if (password == null || password.length() < 6) {
            errors.put("password", "Пароль короче 6 символов");
        }
        if (!captchaCodeService.isValidCaptcha(captcha, captchaSecretCode)) {
            errors.put("captcha", "Код с картинки введен неверно.");
        }
        return errors;
    }
}
