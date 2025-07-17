package com.project.dozeo_appleGame.web.service.account;

import com.project.dozeo_appleGame.entity.User;
import com.project.dozeo_appleGame.exception.CustomInputSameIdException;
import com.project.dozeo_appleGame.exception.CustomInputSameNicknameException;
import com.project.dozeo_appleGame.exception.CustomNullCheckInputValueException;
import com.project.dozeo_appleGame.repository.account.AccountRepository;
import com.project.dozeo_appleGame.security.custom.CustomUserDetails;
import com.project.dozeo_appleGame.web.dto.UserSignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomNicknameService randomNicknameService;

    public User signUp(UserSignupRequestDto dto){
        nullCheckInputValue(dto);
        sameIdCheck(dto.getUsername());
        sameNicknameCheck(dto.getNickname());

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .isGuest(false)
                .registerDate(LocalDateTime.now())
                .build();

        return accountRepository.save(user);
    }
    public User guestReg(){
        String uuid = UUID.randomUUID().toString();
        String guestId = "guest" + uuid.substring(0, 8);

        User user = User.builder()
                .username(guestId)
                .password("")
                .email(guestId+"@")
                .nickname(guestId)
                .isGuest(true)
                .registerDate(LocalDateTime.now())
                .build();

        return accountRepository.save(user);
    }

    public void sameIdCheck(String username){
        Map<String, String > errorMap = new HashMap<>();
        if(accountRepository.existsByUsername(username)){
            errorMap.put("registerError", "ID를 확인해주세요");
            throw new CustomInputSameIdException(errorMap);
        }
    }

    public void sameNicknameCheck(String nickname){
        Map<String, String> errorMap = new HashMap<>();
        if(accountRepository.existsByNickname(nickname)){
            errorMap.put("registerError", "중복된 닉네임입니다.");
            throw new CustomInputSameNicknameException(errorMap);
        }
    }

    public void nullCheckInputValue(UserSignupRequestDto user){
        Map<String, String> errorMap = new HashMap<>();

        if (user.getUsername() == null) {
            errorMap.put("registerError", "빈값을 확인해주세요");
            throw new CustomNullCheckInputValueException(errorMap);
        }
        if (user.getPassword() == null) {
            errorMap.put("registerError", "빈값을 확인해주세요");
            throw new CustomNullCheckInputValueException(errorMap);
        }
        if (user.getNickname() == null) {
            errorMap.put("registerError", "빈값을 확인해주세요");
            throw new CustomNullCheckInputValueException(errorMap);
        }

    }

    // 일반 사용자 / oauth2 사용자 / 비로그인 사용자 구분
    public Map<String, Object> separateUserOfLoginType(Object principal){
        if(principal == null || "anonymousUser".equals(principal)){
            return null;
        }
        if(principal instanceof CustomUserDetails customUserDetails){
            return extraLocalUserInfo(customUserDetails);
        } else if(principal instanceof OAuth2User oAuth2User){
            return extraOAuth2Info(oAuth2User);
        }
        return null;
    }

    // 일반 사용자 데이터 추가
    private Map<String, Object> extraLocalUserInfo(CustomUserDetails customUserDetails){
        Map<String, Object> responseData = new HashMap<>();
        String username = customUserDetails.getUsername();

        responseData.put("type", "local");
        responseData.put("userIndex", accountRepository.findUserIdByUsername(username));
        responseData.put("username", username);
        responseData.put("nickname", accountRepository.findNicknameByUsername(username));

        return responseData;
    }

    // Oauth2 사용자 데이터 저장
    private Map<String, Object> extraOAuth2Info(OAuth2User oAuth2User){
        Map<String, Object> info = oAuth2User.getAttributes();
        String provider = detectProvider(info);

        Map<String, Object> userInfo = extraOAuth2UserInfo(info, provider);
        String email = (String) userInfo.get("email");

        // DB 저장 시 nickname을 직접 전달
        Map<String, Object> registeredInfo = registerUserInfoByOauth2(email);

        // Principal에게 전달할 정보
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("type", "oauth2");
        responseData.put("provider", provider);
        responseData.put("nickname", registeredInfo.get("nickname")); // 확실하게 DB 기준으로
        responseData.put("email", registeredInfo.get("email"));
        responseData.put("id", registeredInfo.get("userIndex"));

        return responseData;
    }

    // Oauth2 로그인 타입 구분(네이버, 카카오)
    private String detectProvider(Map<String, Object> info){
        if(info.containsKey("kakao_account")){
            return "kakao";
        } else if(info.containsKey("response")){
            return "naver";
        } else {
            return "google";
        }
    }

    // Oauth2 사용자 데이터 추가
    private Map<String, Object> extraOAuth2UserInfo(Map<String, Object> info, String provider){
        Map<String, Object> userInfo = new HashMap<>();
        String nickname = randomNicknameService.generateNickname();
        switch (provider){
            case "kakao" -> {
                Map<String, Object> kakaoAccount = (Map<String, Object>) info.get("kakao_account");
                userInfo.put("email", kakaoAccount.getOrDefault("email", "null"));
            }
            case "naver" -> {
                Map<String, Object> response = (Map<String, Object>) info.get("response");
                userInfo.put("email", response.getOrDefault("email", "null"));
            }
            default -> {
                userInfo.put("email", info.getOrDefault("email", "null"));
            }
        }

        userInfo.put("nickname", nickname);
        return userInfo;
    }

    // Oauth2 사용자 db 등록
    private Map<String, Object> registerUserInfoByOauth2(String email) {
        if (accountRepository.existsByEmail(email)) {
            User user = accountRepository.findByEmail(email);
            return Map.of(
                    "username", user.getUsername(),
                    "nickname", user.getNickname(),
                    "email", user.getEmail(),
                    "userIndex", user.getId()
            );
        }
        String nickname = randomNicknameService.generateNickname();
        String randomPassword = UUID.randomUUID().toString();

        User user = new User();
        user.setUsername("OAuth2_User" + accountRepository.findNextUserIndex());
        user.setPassword(passwordEncoder.encode(randomPassword));
        user.setEmail(email);
        user.setNickname(nickname);
        user.setRegisterDate(LocalDateTime.now());

        accountRepository.save(user);

        return Map.of(
                "username", user.getUsername(),
                "nickname", user.getNickname(),
                "email", user.getEmail(),
                "userIndex", user.getId()
        );
    }
}
