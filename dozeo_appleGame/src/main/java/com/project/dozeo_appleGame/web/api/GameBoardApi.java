package com.project.dozeo_appleGame.web.api;

import com.project.dozeo_appleGame.web.dto.CMRespDTO;
import com.project.dozeo_appleGame.web.dto.GameBoardDTO;
import com.project.dozeo_appleGame.web.service.gameSet.GameBoardService;
import com.project.dozeo_appleGame.web.service.gameSet.GameLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/set")
public class GameBoardApi {

    private final GameBoardService boardService;

    private final GameLogicService logicService;

    @PostMapping("/start-game")
    public ResponseEntity<CMRespDTO<?>> playGame(){
        boardService.resetBoard();
        Random random = new Random();
        int number = random.nextInt(9) + 1;
        logicService.placeNumberWithLogic(0, 0, number);
        GameBoardDTO boardDTO = boardService.getBoard();

        for(int i = 0; i < boardDTO.getBoard().length; i++){
            System.out.println(Arrays.toString(boardDTO.getBoard()[i]));
        }

        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", boardDTO));
    }

    @GetMapping("/info")
    public GameBoardDTO getBoard() {
        return boardService.getBoard();
    }

}
