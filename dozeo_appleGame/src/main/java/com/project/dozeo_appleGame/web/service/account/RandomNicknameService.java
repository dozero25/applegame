package com.project.dozeo_appleGame.web.service.account;

import com.project.dozeo_appleGame.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomNicknameService {

    private final UserRepository accountRepository;

    private static final String[] adjectives = {
            "귀여운", "용감한", "조용한", "빠른", "멋진", "씩씩한", "엉뚱한", "깜찍한",
            "졸린", "화난", "신나는", "배고픈", "행복한", "슬픈", "날카로운", "재빠른",
            "똑똑한", "게으른", "근엄한", "수줍은", "친절한", "느긋한", "강력한", "의외의"
    };

    private static final String[] nouns = {
            "호랑이", "토끼", "판다", "고양이", "늑대", "여우", "부엉이", "펭귄",
            "사자", "고래", "햄스터", "수달", "너구리", "청설모", "두더지", "치타",
            "거북이", "앵무새", "돌고래", "악어", "강아지", "참새", "타조", "하마"
    };

    private final Random random = new Random();

    public String generateNickname(){
        String nickname;

        do {
            String adjective = adjectives[random.nextInt(adjectives.length)];
            String noun = nouns[random.nextInt(nouns.length)];
            int number = random.nextInt(1000); // 0~999

            nickname = adjective + noun + number;
        } while(accountRepository.existsByNickname(nickname));

        return nickname;
    }
}
