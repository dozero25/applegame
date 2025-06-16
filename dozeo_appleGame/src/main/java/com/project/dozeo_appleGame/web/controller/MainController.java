package com.project.dozeo_appleGame.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

}
