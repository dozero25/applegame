package com.project.dozeo_appleGame.web.service.gameSet;

import com.project.dozeo_appleGame.web.dto.GameBoardDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GameBoardService {
    private final int ROWS = 10;
    private final int COLS = 17;

    private final GameBoardDTO gameBoard;

    public GameBoardService() {
        this.gameBoard = GameBoardDTO.builder()
                .rows(ROWS)
                .cols(COLS)
                .board(new int[ROWS][COLS])
                .build();
        initializeBoard();
    }

    // 초기 게임판 0으로 초기화
    public void initializeBoard(){
        for(int i = 0; i < gameBoard.getRows(); i++){
            Arrays.fill(gameBoard.getBoard()[i], 0);
        }
    }

    // 위치 유효 체크
    public boolean isValid(int r, int c){
        return r >= 0 && r < gameBoard.getRows() && c >= 0 && c < gameBoard.getCols();
    }

    // 주어진 위치에 숫자 배치(중복배치시 덮어쓰기 방지)
    public boolean placeNumber(int r, int c, int num){
        if(!isValid(r, c)) return false;
        if(gameBoard.getBoard()[r][c] != 0) return false;
        gameBoard.getBoard()[r][c] = num;
        return true;
    }

    // 인접 빈칸 찾기(상하좌우)
    public List<int[]> getAdjacentEmptyCells(int r, int c){
        List<int[]> emptyCells = new ArrayList<>();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for(int[] d : directions){
            int nr = r + d[0];
            int nc = c + d[1];

            if(isValid(nr, nc) && gameBoard.getBoard()[nr][nc] == 0){
                emptyCells.add(new int[]{nr, nc});
            }
        }
        return emptyCells;
    }

    // 현재 게임판 상태 확인
    public GameBoardDTO getBoard(){
        return gameBoard;
    }

    public void resetBoard() {
        int[][] board = this.gameBoard.getBoard();
        for(int r = 0; r < board.length; r++){
            for(int c = 0; c < board[r].length; c++){
                board[r][c] = 0;
            }
        }
        // 기타 초기화 로직 필요 시 추가
    }
}

