package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private AnalyticsService analyticsService;

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

    @GetMapping("/donation")
    public String donation() {
        return "donation";
    }

    @GetMapping("/supervising")
    public String supervising() {
        return "supervising";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/start-order")
    public String startOrder() {
        return "start-order";
    }

    @GetMapping("/analytics")
    public String getAnalyticsDashboard(Model model) {
        model.addAttribute("clientsServed", analyticsService.getTotalClientsServed());
        model.addAttribute("itemsDonated", analyticsService.getTotalItemsDonated());
        model.addAttribute("orderCompletionRate", analyticsService.getOrderCompletionRate());
        model.addAttribute("categoryDonations", analyticsService.getItemCategoryDonations());
        model.addAttribute("topCategories", analyticsService.getTopDonatedCategories(5));
        model.addAttribute("volunteerContributions", analyticsService.getVolunteerContributions());
        model.addAttribute("processingTime", analyticsService.getAverageProcessingTime());
        return "analytics";
    }
}
