package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.models.Piece;
import edu.nyu.welcomehome.services.ItemSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ItemSearchController {

    private final ItemSearchService itemSearchService;

    public ItemSearchController(ItemSearchService itemSearchService) {
        this.itemSearchService = itemSearchService;
    }

    /**
     * Handles the search request and returns the details of the pieces for the given item ID.
     */
    @GetMapping("/itemsearch")
    public String searchItem(@RequestParam(required = false) Integer itemID, Model model) {
        if (itemID != null && itemID > 0) {
            try {
                List<Piece> pieces = itemSearchService.getItemPieces(itemID); // Get list of Piece objects
                model.addAttribute("pieces", pieces); // Add the pieces list to the model
            } catch (Exception e) {
                model.addAttribute("error", "Error fetching data. Please check the Item ID.");
            }
        } else {
            model.addAttribute("error", "Invalid itemID. Please enter a value greater than or equal to 1.");
        }
        return "itemsearch"; // This should match your HTML page name
    }
}