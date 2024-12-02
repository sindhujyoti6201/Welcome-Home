package edu.nyu.welcomehome.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/volunteer-login")
    public String volunteerLogin() {
        return "volunteer-login";
    }

    @GetMapping("/customer-login")
    public String customerLogin() {
        return "customer-login";
    }

    @GetMapping("/volunteer-register")
    public String volunteerRegister() {
        return "volunteer-register";
    }

    @GetMapping("/customer-register")
    public String customerRegister() {
        return "customer-register";
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

    @GetMapping("search-order")
    public String searchOrder() {
        return "search-order";
    }
}
