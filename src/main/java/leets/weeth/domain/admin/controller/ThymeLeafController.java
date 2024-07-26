package leets.weeth.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeLeafController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login-html")
    public String login() {
        return "login-html";
    }

    @GetMapping("/members")
    public String members() {
        return "members";
    }

    @GetMapping("/attendance")
    public String attendance() {
        return "attendance";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
