package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/customer")
    public String showCustomerPage(Model model,
                                   @RequestParam(required = false) String itemId,
                                   @RequestParam(required = false) String mainCategory,
                                   @RequestParam(required = false) String subCategory,
                                   @RequestParam(required = false) String username) {
        // Fetch main categories
        Set<String> mainCategories = itemService.getMainCategories();
        model.addAttribute("mainCategories", mainCategories);

        // Fetch subcategories if main category is selected
        if (mainCategory != null && !mainCategory.isEmpty()) {
            Set<String> subcategories = itemService.getSubcategoriesByMainCategory(mainCategory);
            model.addAttribute("subcategories", subcategories);
        }

        // Search and filter items
        List<Map<String, Object>> items = itemService.searchAndFilterItems(itemId, mainCategory, subCategory);
        model.addAttribute("items", items);

        // Pass the username to the model for display
        model.addAttribute("username", username); // Added username to model

        return "customer"; // Return the customer view
    }
}

//package edu.nyu.welcomehome.controllers;
//
//import edu.nyu.welcomehome.services.ItemService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@Controller
//public class ItemController {
//
//    private final ItemService itemService;
//
//    @Autowired
//    public ItemController(ItemService itemService) {
//        this.itemService = itemService;
//    }
//
//    @GetMapping("/customer")
//    public String showCustomerPage(Model model,
//                                   @RequestParam(required = false) String itemId,
//                                   @RequestParam(required = false) String mainCategory,
//                                   @RequestParam(required = false) String subCategory) {
//        // Fetch main categories
//        Set<String> mainCategories = itemService.getMainCategories();
//        model.addAttribute("mainCategories", mainCategories);
//
//        // Fetch subcategories if main category is selected
//        if (mainCategory != null && !mainCategory.isEmpty()) {
//            Set<String> subcategories = itemService.getSubcategoriesByMainCategory(mainCategory);
//            model.addAttribute("subcategories", subcategories);
//        }
//
//        // Search and filter items
//        List<Map<String, Object>> items = itemService.searchAndFilterItems(itemId, mainCategory, subCategory);
//        model.addAttribute("items", items);
//
//        return "customer";
//    }
//}