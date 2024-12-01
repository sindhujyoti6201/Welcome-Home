package edu.nyu.welcomehome.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/delivery")
    public String delivery() {
        return "delivery";
    }

    @GetMapping("/donations")
    public String donations() {
        return "donations";
    }

    @GetMapping("/supervise")
    public String supervise() {
        return "supervise";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/start-order")
    public String startOrder() {
        return "start-order";
    }
}
