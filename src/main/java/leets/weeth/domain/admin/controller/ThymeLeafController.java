package leets.weeth.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminpage")
public class ThymeLeafController {

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("pageTitle", "weeth admin");
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("pageTitle", "Login");
        return "login-html";
    }

    @GetMapping("/members")
    public String members(Model model) {
        model.addAttribute("pageTitle", "멤버 관리");
        return "members";
    }

    @GetMapping("/attendance")
    public String attendance(Model model) {
        model.addAttribute("pageTitle", "출석 관리");
        return "attendance";
    }

    @GetMapping("/penalty")
    public String penalty(Model model) {
        model.addAttribute("pageTitle", "패널티 관리");
        return "penalty";
    }

    @GetMapping("/account")
    public String account(Model model) {
        model.addAttribute("pageTitle", "weeth admin 회비");
        return "account";
    }
}
