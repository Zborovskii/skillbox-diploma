package ru.skillbox.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.SettingsValues;
import ru.skillbox.dto.StatisticsDto;
import ru.skillbox.enums.StatisticsType;
import ru.skillbox.enums.Vote;
import ru.skillbox.model.User;

@Service
public class StatisticsService {

    @Autowired
    private AuthService authService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private PostService postService;

    @Autowired
    private VotesService votesService;

    public ResponseEntity<StatisticsDto> getStatistics(StatisticsType statisticsType) {
        Optional<User> userOptional = authService.getAuthorizedUser();

        boolean isStatsPublic = settingsService.isStatsPublic();
        if (userOptional.isPresent()) {
            if (StatisticsType.ALL.equals(statisticsType) && isStatsPublic) {
                return new ResponseEntity<>(getStatisticsFromDB(null), HttpStatus.OK);
            }

            return new ResponseEntity<>(getStatisticsFromDB(userOptional.get()), HttpStatus.OK);
        }
        if (isStatsPublic) {
            return new ResponseEntity<>(getStatisticsFromDB(null), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<SettingsValues> updateSettings(SettingsValues settings) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        if (!user.getIsModerator()) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        SettingsValues updatedSettings = settingsService.saveSettings(settings);
        return new ResponseEntity<>(updatedSettings, HttpStatus.OK);
    }

    public StatisticsDto getStatisticsFromDB(User user) {
        StatisticsDto statisticsDto = new StatisticsDto();

        statisticsDto.setPostsCount(postService.countByUser(user));
        statisticsDto.setLikesCount(votesService.countByUserAndValue(user, Vote.LIKE));
        statisticsDto.setDislikesCount(votesService.countByUserAndValue(user, Vote.DISLIKE));
        statisticsDto.setViewsCount(postService.countViewsByUser(user));
        statisticsDto.setFirstPublication(postService.getFirstPostDate(user));

        return statisticsDto;
    }
}
