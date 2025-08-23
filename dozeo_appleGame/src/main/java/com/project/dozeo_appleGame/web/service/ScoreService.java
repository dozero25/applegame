package com.project.dozeo_appleGame.web.service;

import com.project.dozeo_appleGame.entity.Score;
import com.project.dozeo_appleGame.entity.User;
import com.project.dozeo_appleGame.repository.ScoreRepository;
import com.project.dozeo_appleGame.repository.account.UserRepository;
import com.project.dozeo_appleGame.web.dto.ScoreRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final UserRepository userRepository;
    private final ScoreRepository scoreRepository;

    @Transactional
    public void saveOrUpdateScore(ScoreRequest scoreRequest){
        User user = userRepository.findById(scoreRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Score score = scoreRepository.findByUserAndGameType(user, scoreRequest.getGameType())
                .orElse(Score.builder()
                        .user(user)
                        .gameType(scoreRequest.getGameType())
                        .points(0)
                        .updateTime(LocalDateTime.now())
                        .build());

        if (scoreRequest.getPoints() > score.getPoints()){
            score.setPoints(scoreRequest.getPoints());
            score.setUpdateTime(LocalDateTime.now());
            scoreRepository.save(score);
        }
    }

    public List<ScoreRequest> getScoreByUserWithRank(Long userId) {
        List<Object[]> results = scoreRepository.findUserScoresWithRanking(userId);

        return results.stream()
                .map(o -> ScoreRequest.builder()
                        .userId(((Number)o[0]).longValue())
                        .gameType((String)o[1])
                        .points(((Number)o[2]).intValue())
                        .ranking(((Number)o[3]).intValue())
                        .build())
                .collect(Collectors.toList());
    }
}
