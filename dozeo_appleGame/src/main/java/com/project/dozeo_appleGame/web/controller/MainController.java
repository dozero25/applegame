package com.project.dozeo_appleGame.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(){
        return "html/main/main";
    }

    @GetMapping("/login")
    public String login(){
        return "html/main/login";
    }

    @GetMapping("/login/error")
    public String loginError(){
        return "html/main/login_error";
    }

    @GetMapping("/apple")
    public String appleMain(){
        return "html/apple/main";
    }

    @GetMapping("/apple/game")
    public String appleGame(){
        return "html/apple/game";

    }

    @GetMapping("/oauth2/redirect")
    public String oauth2Index(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "html/oauth2_index";
    }

    @GetMapping("/mypage")
    public String myPage(){
        return "html/mypage/mypage";
    }
}
