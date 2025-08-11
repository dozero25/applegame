package com.project.dozeo_appleGame.web.api;

import com.project.dozeo_appleGame.entity.User;
import com.project.dozeo_appleGame.security.custom.CustomUserDetails;
import com.project.dozeo_appleGame.security.jwt.JwtUtil;
import com.project.dozeo_appleGame.web.dto.*;
import com.project.dozeo_appleGame.web.service.account.Oauth2UnlinkService;
import com.project.dozeo_appleGame.web.service.account.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApi {

    private final UserService userService;
    private final Oauth2UnlinkService oauth2UnlinkService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.createToken(userDetails.getUsername(), userDetails.getUsername());

            return ResponseEntity.ok()
                    .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", new JwtResponse(token)));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new CMRespDTO<>(HttpStatus.BAD_REQUEST.value(), "Failed", "로그인 실패"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequestDto dto){
        userService.signUp(dto);
        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", true));
    }

    @PostMapping("/guest/signup")
    public ResponseEntity<?> guestSignup(){
        User guestUser = userService.guestReg();

        String token = jwtUtil.createToken(guestUser.getUsername(), guestUser.getNickname());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", guestUser.getUsername());

        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", response));
    }


    @GetMapping("/auth/login")
    public ResponseEntity<?> autologin(@RequestParam(value = "error", required = false) String error,
                                   @RequestParam(value = "exception", required = false) String exception) {
        Map<String, String> response = new HashMap<>();
        response.put("error", error);
        response.put("exception", exception);

        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Login info retrieved", response));
    }

    @PostMapping("/oauth2/unlink")
    public ResponseEntity<?> unlinkOauth2(@RequestBody UnlinkRequestDto unlinkRequestDto){
        String provider = unlinkRequestDto.getProvider();
        String accessToken = unlinkRequestDto.getAccessToken();
        Long index = unlinkRequestDto.getId();

        try {
            oauth2UnlinkService.unlink(provider, accessToken);
            oauth2UnlinkService.removeOauth2User(index);
            return ResponseEntity.ok()
                    .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", null));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CMRespDTO<>(HttpStatus.BAD_REQUEST.value(), "잘못된 사용자 : ", provider));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CMRespDTO<>(HttpStatus.BAD_REQUEST.value(), "연결 해제 실패: ", e.getMessage()));
        }
    }

    @GetMapping("/principal")
    public ResponseEntity<?> principal(@AuthenticationPrincipal Object principal){
        Map<String, Object> userInfo = userService.separateUserOfLoginType(principal);

        if(userInfo == null){
            return ResponseEntity.badRequest()
                    .body(new CMRespDTO<>(HttpStatus.BAD_REQUEST.value(), "failed login", null));
        }
        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully data", userInfo));
    }

    @PutMapping("/info/update")
    public ResponseEntity<?> userUpdate(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UserUpdateRequestDto dto){
        Long id = userDetails.getUser().getId();
        userService.updateUserInfo(id, dto);

        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", true));
    }

    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDto request, @AuthenticationPrincipal CustomUserDetails userDetails){
        Long userIndex = userDetails.getUser().getId();
        userService.changePassword(userIndex, request.getCurrentPassword(), request.getChangePassword());

        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", true));
    }

}
