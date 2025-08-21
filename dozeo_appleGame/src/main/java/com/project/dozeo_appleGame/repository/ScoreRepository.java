package com.project.dozeo_appleGame.repository;

import com.project.dozeo_appleGame.entity.Score;
import com.project.dozeo_appleGame.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Optional<Score> findByUserAndGameType(User user, String gameType);

}
