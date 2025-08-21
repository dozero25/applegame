package com.project.dozeo_appleGame.web.api;

import com.project.dozeo_appleGame.web.dto.CMRespDTO;
import com.project.dozeo_appleGame.web.dto.ScoreRequest;
import com.project.dozeo_appleGame.web.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
