package com.project.dozeo_appleGame.web.service.gameSet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameLogicService {
    private final GameBoardService boardService;
    private final CombinationService combinationService;
    private final Random random = new Random();

    /**
     * 주어진 위치에 숫자 num을 놓고,
     * 50% 확률로 인접한 칸에 랜덤 숫자 배치,
     * 50% 확률로 num을 포함하는 합 10 조합을 인접 칸에 배치
     * * BFS를 사용해 효율적 처리
     */
    public void placeNumberWithLogic(int startR, int startC, int startNum){
        int[][] board = boardService.getBoard().getBoard();

        // 1. 시작 위치에 숫자 배치 시도. 시작 위치 : (0, 0) 고정
        if(!boardService.placeNumber(startR, startC, startNum)){
//            System.out.println("배치 실패 (이미 숫자 있음 또는 위치 불가): " + startR + ", " + startC);
            return;
        }

        // 2. BFS를 위한 큐 생성 및 시작 위치 삽입
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startR, startC});


        // 3. 큐가 빌 때까지 반복(BFS 탐색 반복문)
        while(!queue.isEmpty()){
            // 4. 큐에서 현재 위치 꺼냄 (가장 먼저 들어온 순서대로 처리)
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];
            int num = board[r][c];

            // 5. 현재 위치 주변의 빈 칸 탐색(인접 노드 조회)
            List<int[]> adjEmptyCells = boardService.getAdjacentEmptyCells(r, c);
            if(adjEmptyCells.isEmpty()){
//                System.out.println("인접 빈칸 없음 at (" + r + "," + c + ")");
                continue;
            }

            double chance = random.nextDouble();

            if(chance < 0.5){
                // 6-1. 인접 빈칸 중 무작위 선택해 숫자 배치 시도
                int idx = random.nextInt(adjEmptyCells.size());
                int[] pos = adjEmptyCells.get(idx);
                int randomNum = random.nextInt(9)+ 1;

                if(boardService.placeNumber(pos[0], pos[1], randomNum)){
//                    System.out.println("랜덤 숫자 " + randomNum + " 배치됨 at (" + pos[0] + "," + pos[1] + ")");
                    // 7. 숫자 배치에 성공하면 그 위치를 큐에 넣어 다음 탐색 대상으로 만듦 (탐색 영역 확장)
                    queue.offer(new int[]{pos[0], pos[1]});
                }
            } else {
                // 6-2. 조합 로직으로 숫자 배치 시도
                List<List<Integer>> combos = combinationService.getCombinationsContaining(num);

                if(combos.isEmpty()){
                    int idx = random.nextInt(adjEmptyCells.size());
                    int[] pos = adjEmptyCells.get(idx);
                    int randomNum = random.nextInt(9) + 1;

                    if(boardService.placeNumber(pos[0], pos[1], randomNum)){
//                        System.out.println("랜덤 숫자 " + randomNum + " 배치됨 at (" + pos[0] + "," + pos[1] + ")");
                        queue.offer(new int[]{pos[0], pos[1]});
                    }
                    continue;
                }
                // 6-3. 조합에서 숫자 골라서 배치
                List<Integer> selectCombo = new ArrayList<>(combos.get(random.nextInt(combos.size())));
                selectCombo.remove(Integer.valueOf(num));

                int placeCount = Math.min(selectCombo.size(), adjEmptyCells.size());

                for(int i = 0; i < placeCount; i++){
                    int[] pos = adjEmptyCells.get(i);
                    int n = selectCombo.get(i);

                    if(boardService.placeNumber(pos[0], pos[1], n)){
//                        System.out.println("조합 숫자 " + n + " 배치됨 at (" + pos[0] + "," + pos[1] + ")");
                        // 7. 배치 성공한 위치를 큐에 넣어 BFS 탐색 계속
                        queue.offer(new int[]{pos[0], pos[1]});
                    }
                }

            }
        }

        // 확인 못한 빈칸이 있는지 확인 후 값 추가
        List<int[]> remainingEmptyCells = getAllEmptyCells(board);
        for(int[] pos : remainingEmptyCells){
            int randomNum = random.nextInt(9) + 1;
            if(boardService.placeNumber(pos[0], pos[1], randomNum)){
                System.out.println("남은 빈칸에 랜덤 숫자 " + randomNum + " 배치됨 at (" + pos[0] + "," + pos[1] + ")");
            } else {
                System.out.println("남은 빈칸 배치 실패 at (" + pos[0] + "," + pos[1] + ")");
            }
        }
    }

    // 보드 전체에서 빈칸(0) 리스트 얻기
    private List<int[]> getAllEmptyCells(int[][] board){
        List<int[]> emptyCells = new ArrayList<>();
        for(int r = 0; r < board.length; r++){
            for(int c = 0; c < board[0].length; c++){
                if(board[r][c] == 0){
                    emptyCells.add(new int[]{r, c});
                }
            }
        }
        return emptyCells;
    }
}

