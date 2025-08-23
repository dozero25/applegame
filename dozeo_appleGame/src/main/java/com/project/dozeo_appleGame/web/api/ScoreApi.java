package com.project.dozeo_appleGame.web.api;

import com.project.dozeo_appleGame.entity.Score;
import com.project.dozeo_appleGame.security.custom.CustomUserDetails;
import com.project.dozeo_appleGame.web.dto.CMRespDTO;
import com.project.dozeo_appleGame.web.dto.ScoreRequest;
import com.project.dozeo_appleGame.web.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/score")
public class ScoreApi {

    private final ScoreService scoreService;

    @PostMapping("/add")
    public ResponseEntity<?> saveOrUpdateScore(@RequestBody ScoreRequest scoreRequest){
        scoreService.saveOrUpdateScore(scoreRequest);
        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", null));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getScoreByUserWithRank(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<ScoreRequest> list = scoreService.getScoreByUserWithRank(userDetails.getUser().getId());
        
        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", list));
    }

}
