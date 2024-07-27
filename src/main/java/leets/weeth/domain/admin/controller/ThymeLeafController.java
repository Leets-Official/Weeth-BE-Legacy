package leets.weeth.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminpage")
public class ThymeLeafController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
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

    @GetMapping("/penalty")
    public String penalty() {
        return "penalty";
    }

    @GetMapping("/account")
    public String account() {
        return "account";
    }
}
