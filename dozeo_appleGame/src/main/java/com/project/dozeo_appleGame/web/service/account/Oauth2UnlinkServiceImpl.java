package com.project.dozeo_appleGame.web.service.account;

import com.project.dozeo_appleGame.config.NaverProperties;
import com.project.dozeo_appleGame.entity.User;
import com.project.dozeo_appleGame.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2UnlinkServiceImpl implements Oauth2UnlinkService {

    private static final String KAKAO_UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";
    private static final String NAVER_UNLINK_URL = "https://nid.naver.com/oauth2.0/token";

    private final UserRepository userRepository;

    private final NaverProperties naverProperties;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public void unlink(String provider, String accessToken) throws Exception {
        switch (provider) {
            case "kakao" -> unlinkKakao(accessToken);
            case "naver" -> unlinkNaver(accessToken);
            default -> throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
        }
    }

    @Override
    public void removeOauth2User(Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        System.out.println("1");
        if(optionalUser.isEmpty()){
            throw new IllegalArgumentException("해당 ID를 가진 사용자가 존재하지 않습니다: " + id);
        }
        System.out.println("2");
        userRepository.deleteById(id);
    }

    private void unlinkKakao(String accessToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(KAKAO_UNLINK_URL))
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        logResponse("카카오", response);
    }

    private void unlinkNaver(String accessToken) throws Exception {
        String clientId = naverProperties.getClientId();
        String clientSecret = naverProperties.getClientSecret();

        String url = String.format(
                "%s?grant_type=delete&client_id=%s&client_secret=%s&access_token=%s&service_provider=NAVER",
                NAVER_UNLINK_URL, clientId, clientSecret, accessToken
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        logResponse("네이버", response);
    }

    private void logResponse(String provider, HttpResponse<String> response) {
        if (response.statusCode() == 200) {
            log.info("[{} unlink 성공] 응답: {}", provider, response.body());
        } else {
            log.error("[{} unlink 실패] 상태 코드: {}, 응답: {}", provider, response.statusCode(), response.body());
        }
    }


}
