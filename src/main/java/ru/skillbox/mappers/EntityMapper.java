package ru.skillbox.mappers;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.CommentDto;
import ru.skillbox.dto.PlainPostDto;
import ru.skillbox.dto.PostWithCommentsResponse;
import ru.skillbox.dto.TagDto;
import ru.skillbox.dto.UserAdditionalInfoDto;
import ru.skillbox.dto.UserDto;
import ru.skillbox.dto.UserWithPhotoDto;
import ru.skillbox.model.Post;
import ru.skillbox.model.PostComment;
import ru.skillbox.model.Tag;
import ru.skillbox.model.User;

@Service
public class EntityMapper {

    public UserWithPhotoDto userToUserWithPhotoDto(User user) {
        UserWithPhotoDto userWithPhotoDto = new UserWithPhotoDto();
        userWithPhotoDto.setPhoto(user.getPhoto());
        userWithPhotoDto.setId(user.getId());
        userWithPhotoDto.setName(user.getName());

        return userWithPhotoDto;
    }

    public CommentDto psotCommentToCommentDto(PostComment postComment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(postComment.getId());
        commentDto.setText(postComment.getText());
        commentDto.setTime(postComment.getTime());
        commentDto.setUser(userToUserWithPhotoDto(postComment.getUser()));

        return commentDto;
    }

    public PostWithCommentsResponse postToPostWithCommentsDto(Post post) {
        PostWithCommentsResponse postWithCommentsResponse = new PostWithCommentsResponse();
        postWithCommentsResponse.setId(post.getId());
        postWithCommentsResponse.setText(post.getText());
        postWithCommentsResponse.setTime(post.getTime());
        postWithCommentsResponse.setTitle(post.getTitle());
        postWithCommentsResponse.setViewCount(post.getViewCount());
        postWithCommentsResponse.setUser(userToUserDto(post.getUser()));

        postWithCommentsResponse.setComments(post.getPostComments().stream()
                                                 .map(this::psotCommentToCommentDto)
                                                 .collect(Collectors.toList()));
        postWithCommentsResponse.setTags(post.getTags().stream()
                                             .map(Tag::getName)
                                             .collect(Collectors.toList()));
        postWithCommentsResponse.setLikeCount(post.getPostVotes().stream()
                                                  .filter(item -> item.getValue() > 0)
                                                  .count());
        postWithCommentsResponse.setDislikeCount(post.getPostVotes().stream()
                                                     .filter(item -> item.getValue() < 0)
                                                     .count());

        return postWithCommentsResponse;
    }

    public PlainPostDto postToPlainPostDto(Post post) {
        PlainPostDto plainPostDto = new PlainPostDto();

        plainPostDto.setCommentCount(post.getPostComments().size());
        plainPostDto.setId(post.getId());
        plainPostDto.setTitle(post.getTitle());
        plainPostDto.setViewCount(post.getViewCount());
        plainPostDto.setTime(post.getTime());
        plainPostDto.setUser(userToUserDto(post.getUser()));
        String announce = post.getText().length() > 150 ? post.getText().substring(0, 150) + "..." : post.getText();
        plainPostDto.setAnnounce(announce);
        plainPostDto.setDislikeCount(post.getPostVotes().stream()
                                         .filter(item -> item.getValue() < 0)
                                         .count());
        plainPostDto.setLikeCount(post.getPostVotes().stream()
                                      .filter(item -> item.getValue() > 0)
                                      .count());

        return plainPostDto;
    }

    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

    public TagDto tagToTagDto(Tag tag, Integer activePostsCount) {
        TagDto tagDto = new TagDto();
        tagDto.setName(tag.getName());
        tagDto.setWeight((double) (tag.getPosts().size() / activePostsCount));
        return tagDto;
    }

    public UserAdditionalInfoDto getAuthorizedUserDTO(User user) {
        UserAdditionalInfoDto authorizedUser = new UserAdditionalInfoDto();

        if (user == null) {
            return authorizedUser;
        }

        authorizedUser.setId(user.getId());
        authorizedUser.setName(user.getName());
        authorizedUser.setPhoto(user.getPhoto());
        authorizedUser.setEmail(user.getEmail());
        authorizedUser.setModeration(user.getIsModerator());

        return authorizedUser;
    }
}
