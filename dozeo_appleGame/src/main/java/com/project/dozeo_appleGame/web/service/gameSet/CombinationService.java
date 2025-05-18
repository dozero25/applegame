package com.project.dozeo_appleGame.web.service.gameSet;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CombinationService {
    private final List<List<Integer>> combinations;

    // 숫자 조합들을 미리 정리해 판별
    public CombinationService(){
        combinations = new ArrayList<>();

        combinations.add(Arrays.asList(5, 5));
        combinations.add(Arrays.asList(2, 3, 5));
        combinations.add(Arrays.asList(1, 4, 5));
        combinations.add(Arrays.asList(1, 2, 7));
        combinations.add(Arrays.asList(4, 6));
    }

    // 특정 숫자를 포함하는 조합 찾기
    public List<List<Integer>> getCombinationsContaining(int num){
        List<List<Integer>> filtered = new ArrayList<>();

        for(List<Integer> combo : combinations){
            if(combo.contains(num)){
                filtered.add(combo);
            }
        }
        return filtered;
    }
}
