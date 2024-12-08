package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.AnalyticsService;
import edu.nyu.welcomehome.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class MainController {

    private AnalyticsService analyticsService;

    private ItemService itemService;

    @Autowired
    public MainController(AnalyticsService analyticsService, ItemService itemService) {
        this.analyticsService = analyticsService;
        this.itemService = itemService;
    }

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
    public String dashboard(@RequestParam(required = false) String username) {
        System.out.println(username);
        return "dashboard";
    }

    @GetMapping("/delivering")
    public String delivery(Model model,
                           @RequestParam(required = false) String username) {
        model.addAttribute("username", username);
        return "delivery";
    }

    @GetMapping("/donation")
    public String donations(Model model,
                            @RequestParam(required = false) String mainCategory,
                            @RequestParam(required = false) String username) {
        // Fetch main categories
        Set<String> mainCategories = itemService.getMainCategories();
        model.addAttribute("mainCategories", mainCategories);

        // Fetch subcategories if main category is selected
        if (mainCategory != null && !mainCategory.isEmpty()) {
            Set<String> subcategories = itemService.getSubcategoriesByMainCategory(mainCategory);
            model.addAttribute("subcategories", subcategories);
        }

        // Pass the username to the model for display
        model.addAttribute("username", username); // Added username to model
        return "donation";
    }

    @GetMapping("/supervising")
    public String supervising(Model model,
                              @RequestParam(required = false) String username) {
        model.addAttribute("username", username);
        return "supervising";
    }

    @GetMapping("/manager")
    public String manager(Model model,
                          @RequestParam(required = false) String username) {
        model.addAttribute("username", username);
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

    @GetMapping("/range")
    public String range() {
        return "range";
    }

    @GetMapping("/search-order")
    public String getSearchOrder() {
        return "search-order";
    }
}
