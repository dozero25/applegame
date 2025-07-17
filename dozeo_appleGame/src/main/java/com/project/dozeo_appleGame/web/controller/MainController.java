package com.project.dozeo_appleGame.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/main")
    public String main(){
        return "html/main/main.html";
    }

    @GetMapping("/login")
    public String login(){
        return "html/main/login.html";
    }

    @GetMapping("/login/error")
    public String loginError(){
        return "html/main/login_error.html";
    }

    @GetMapping("/apple")
    public String appleMain(){
        return "html/apple/main.html";
    }

    @GetMapping("/apple/game")
    public String appleGame(){
        return "html/apple/game.html";

    }

    @GetMapping("/oauth2/redirect")
    public String oauth2Index(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "html/oauth2_index";
    }

}
