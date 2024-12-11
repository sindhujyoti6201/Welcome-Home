package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public String addToCart(@RequestParam String username, @RequestParam String itemId, @RequestParam String orderNotes) {
        boolean added = itemService.addItemToCart(username, itemId, orderNotes);
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

    @PostMapping("/placeOrder")
    @ResponseBody
    public Map<String, Object> placeOrder(@RequestBody Map<String, Object> requestData) {
        String username = (String) requestData.get("username");
        List<Map<String, String>> items = (List<Map<String, String>>) requestData.get("items");

        // Assuming orderNotes is provided for a single item
        String orderNotes = (String) requestData.get("notes");

        // Call the service method to place the order
        return itemService.validateAndPlaceOrder(username, items, orderNotes);  // Pass orderNotes explicitly
    }


    @PostMapping("/placeMultiOrder")
    @ResponseBody
    public Map<String, Object> placeMultiOrder(@RequestBody Map<String, Object> requestData) {
        String username = (String) requestData.get("username");
        List<Map<String, String>> items = new ArrayList<>();

        // Get the itemIds and orderNote
        List<String> itemIds = (List<String>) requestData.get("items");
        String orderNote = (String) requestData.get("notes");  // Single order note for all items

        // Validate input
        if (itemIds == null || itemIds.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No items provided for the order.");
            return response;
        }

        // Prepare items for order validation, using the same order note for all items
        for (String itemId : itemIds) {
            Map<String, String> itemDetails = new HashMap<>();
            itemDetails.put("itemId", itemId);
            itemDetails.put("orderNotes", orderNote != null ? orderNote : "");  // Use the same order note for all items
            items.add(itemDetails);
        }

        // Reuse the existing order validation and placement logic
        return itemService.validateAndPlaceOrder(username, items, orderNote);  // Pass orderNote explicitly
    }
}