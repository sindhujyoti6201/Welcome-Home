package edu.nyu.welcomehome.controllers;
import edu.nyu.welcomehome.services.OrderRankingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class OrderRankingController {

    private final OrderRankingService orderRankingService;

    public OrderRankingController(OrderRankingService orderRankingService) {
        this.orderRankingService = orderRankingService;
    }

    /**
     * Endpoint to handle the ranking request and display the categories/subcategories rank.
     */
    @GetMapping("/categoryrankings")
    public String getCategoryRankings(@RequestParam("startDate") String startDateStr,
                                      @RequestParam("endDate") String endDateStr,
                                      Model model) {
        try {
            // Parse the date strings to LocalDate
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            // Get the rankings from the service layer
            List<Map<String, Object>> rankings = orderRankingService.getCategoryRankings(startDate, endDate);

            // Add rankings data to the model to display in the view
            model.addAttribute("rankings", rankings);
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching data. Please try again.");
        }

        return "categoryrankings";  // Name of the HTML page to return
    }
    @GetMapping("/categoryrank")
    public String getCategoryOnlyRankings(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate, Model model) {
       try {
           LocalDate start = LocalDate.parse(startDate);
           LocalDate end = LocalDate.parse(endDate);
           List<Map<String, Object>> rankings = orderRankingService.getCategoryOnlyRankings(start, end);

           // Add rankings data to the model to display in the view
           model.addAttribute("rankings", rankings);
       }
       catch (Exception e) {
           model.addAttribute("error", "Error fetching data. Please try again.");
       }
       return "categoryrank";

    }
}
