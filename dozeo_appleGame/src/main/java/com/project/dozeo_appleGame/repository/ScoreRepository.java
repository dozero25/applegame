package com.project.dozeo_appleGame.repository;

import com.project.dozeo_appleGame.entity.Score;
import com.project.dozeo_appleGame.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Optional<Score> findByUserAndGameType(User user, String gameType);

    @Query(
            value = "SELECT user_id AS userId, game_type AS gameType, points, ranking "+
                    "FROM ("+
                    " SELECT user_id, game_type, points, " +
                    " RANK() OVER (PARTITION BY game_type ORDER BY points DESC) AS ranking " +
                    " FROM `applegame-dozero`.score" +
                    " ) ranked " +
                    " WHERE user_id = :userId",
            nativeQuery = true
    )
    List<Object[]> findUserScoresWithRanking(@Param("userId") Long userId);

    @Query(
            value = "SELECT ranked.nickname, ranked.ranking " +
                    "FROM (" +
                    " SELECT s.user_id, u.nickname, RANK() OVER (PARTITION BY game_type ORDER BY points DESC) as ranking " +
                    " FROM 'applegame-dozero'.score s " +
                    " JOIN 'applegame-dozero'.user u ON s.user_id = u.id "+
                    " WHERE game_type = :gameType " +
                    ") AS ranked " +
                    "WHERE ranked.ranking <= 10",
            nativeQuery = true
    )
    List<Object[]> findUserAllScoreOfAppleGameWithRanking(@Param("gameType") String gameType);



}
