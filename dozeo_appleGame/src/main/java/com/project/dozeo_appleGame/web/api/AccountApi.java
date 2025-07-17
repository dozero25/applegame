package com.project.dozeo_appleGame.web.api;

import com.project.dozeo_appleGame.entity.User;
import com.project.dozeo_appleGame.security.jwt.JwtUtil;
import com.project.dozeo_appleGame.web.dto.CMRespDTO;
import com.project.dozeo_appleGame.web.dto.JwtResponse;
import com.project.dozeo_appleGame.web.dto.LoginRequestDto;
import com.project.dozeo_appleGame.web.dto.UserSignupRequestDto;
import com.project.dozeo_appleGame.web.service.account.AccountService;
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
@RequestMapping("/api/account")
public class AccountApi {

    private final AccountService accountService;
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

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequestDto dto){
        accountService.signUp(dto);
        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", true));
    }

    @PostMapping("/guest/signup")
    public ResponseEntity<?> guestSignup(){
        User guestUser = accountService.guestReg();

        String token = jwtUtil.createToken(guestUser.getUsername(), guestUser.getNickname());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", guestUser.getUsername());

        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", response));
    }


    @GetMapping("/auth/login")
    public ResponseEntity<?> login(@RequestParam(value = "error", required = false) String error,
                                   @RequestParam(value = "exception", required = false) String exception) {
        Map<String, String> response = new HashMap<>();
        response.put("error", error);
        response.put("exception", exception);

        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Login info retrieved", response));
    }

    @GetMapping("/principal")
    public ResponseEntity<?> principal(@AuthenticationPrincipal Object principal){
        System.out.println("principal : "+principal);
        Map<String, Object> userInfo = accountService.separateUserOfLoginType(principal);

        if(userInfo == null){
            return ResponseEntity.badRequest()
                    .body(new CMRespDTO<>(HttpStatus.BAD_REQUEST.value(), "failed login", null));
        }
        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully data", userInfo));
    }

}
