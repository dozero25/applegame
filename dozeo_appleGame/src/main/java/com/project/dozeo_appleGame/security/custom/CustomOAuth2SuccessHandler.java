package com.project.dozeo_appleGame.security.custom;

import com.project.dozeo_appleGame.security.jwt.JwtUtil;
import com.project.dozeo_appleGame.web.service.account.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService accountService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> userInfo = accountService.separateUserOfLoginType(oAuth2User);
        String username = (String) userInfo.get("username");

        String token = jwtUtil.createToken(username, username);

        String redirectUrl = "http://localhost:8000/oauth2/redirect?token=" + token;

        response.sendRedirect(redirectUrl);

    }

}

