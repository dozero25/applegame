package com.project.dozeo_appleGame.web.api;

import com.project.dozeo_appleGame.web.dto.CMRespDTO;
import com.project.dozeo_appleGame.web.dto.UserSignupRequestDto;
import com.project.dozeo_appleGame.web.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountApi {

    private final AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequestDto dto){
        accountService.signup(dto);
        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully", true));
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

        Map<String, Object> userInfo = accountService.separateUserOfLoginType(principal);

        if(userInfo == null){
            return ResponseEntity.badRequest()
                    .body(new CMRespDTO<>(HttpStatus.BAD_REQUEST.value(), "failed login", null));
        }
        return ResponseEntity.ok()
                .body(new CMRespDTO<>(HttpStatus.OK.value(), "Successfully data", userInfo));
    }

}
