package com.project.dozeo_appleGame.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/main")
    public String main(){
        return "html/main/main.html";
    }

    @GetMapping("/game")
    public String game(){
        return "html/game/game.html";

    }

}
