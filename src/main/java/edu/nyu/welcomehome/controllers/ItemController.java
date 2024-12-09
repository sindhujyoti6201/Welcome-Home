package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/customer")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public String showCustomerPage(Model model,
                                   @RequestParam(required = false) String itemId,
                                   @RequestParam(required = false) String mainCategory,
                                   @RequestParam(required = false) String subCategory,
                                   @RequestParam(required = false) String username) {
         //Fetch main categories
        model.addAttribute("mainCategories", itemService.getMainCategories());

        // Fetch subcategories if main category is selected
        if (mainCategory != null && !mainCategory.isEmpty()) {
            model.addAttribute("subcategories", itemService.getSubcategoriesByMainCategory(mainCategory));
        }

        // Search and filter items
        List<Map<String, Object>> items = itemService.searchAndFilterItems(itemId, mainCategory, subCategory);
        model.addAttribute("items", items);

        // Pass the username to the model for display
        model.addAttribute("username", username);

        return "customer"; // Return the customer view
        }


    @PostMapping("/addToCart")
    @ResponseBody
    public String addToCart(@RequestParam String username, @RequestParam String itemId) {
        boolean added = itemService.addItemToCart(username, itemId);
        return added ? "Item added to cart successfully!" : "Failed to add item to cart.";
    }

    @GetMapping("/api/categories")
    @ResponseBody
    public Set<String> getCategories() {
        return itemService.getMainCategories();
    }

    @GetMapping("/api/subcategories/{mainCategory}")
    @ResponseBody
    public Set<String> getSubcategories(@PathVariable String mainCategory) {
        return itemService.getSubcategoriesByMainCategory(mainCategory);
    }

}

