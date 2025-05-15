package com.project.dozeo_appleGame.repository;

import com.project.dozeo_appleGame.entity.score.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {


}
