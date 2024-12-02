package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.models.response.DonatedItemCategoryResponse;
import edu.nyu.welcomehome.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/clients-served")
    public int getTotalClientsServed() {
        return analyticsService.getTotalClientsServed();
    }

    @GetMapping("/items-donated")
    public int getTotalItemsDonated() {
        return analyticsService.getTotalItemsDonated();
    }

    @GetMapping("/order-completion-rate")
    public double getOrderCompletionRate() {
        return analyticsService.getOrderCompletionRate();
    }

    @GetMapping("/volunteer-contributions")
    public Map<String, Object> getVolunteerContributions() {
        return analyticsService.getVolunteerContributions();
    }

    @GetMapping("/average-processing-time")
    public Map<String, Object> getAverageProcessingTime() {
        return analyticsService.getAverageProcessingTime();
    }

    @GetMapping("/item-category-donations")
    public List<DonatedItemCategoryResponse> getItemCategoryDonations() {
        return analyticsService.getItemCategoryDonations();
    }

    @GetMapping("/top-donated-categories")
    public List<DonatedItemCategoryResponse> getTopDonatedCategories(@RequestParam(defaultValue = "5") int topN) {
        return analyticsService.getTopDonatedCategories(topN);
    }
}
