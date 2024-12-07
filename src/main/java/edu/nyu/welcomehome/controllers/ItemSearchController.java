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
        if (itemID != null) {
            try {
                List<Piece> pieces = itemSearchService.getItemPieces(itemID); // Get list of Piece objects
                model.addAttribute("pieces", pieces); // Add the pieces list to the model
            } catch (Exception e) {
                model.addAttribute("error", "Error fetching data. Please check the Item ID.");
            }
        }
        return "itemsearch"; // This should match your HTML page name
    }
}


//package edu.nyu.welcomehome.controllers;
//
//import edu.nyu.welcomehome.services.ItemSearchService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//import java.util.Map;
//
//@Controller
//public class ItemSearchController {
//
//    private final ItemSearchService itemSearchService;
//
//    public ItemSearchController(ItemSearchService itemSearchService) {
//        this.itemSearchService = itemSearchService;
//    }
//
//    /**
//     * Handles the search request and returns the details of the pieces for the given item ID.
//     */
//    @GetMapping("/itemsearch")
//    public String searchItem(@RequestParam(required = false) Integer itemID, Model model) {
//        if (itemID != null) {
//            try {
//                List<Map<String, Object>> pieces = itemSearchService.getItemPieces(itemID);
//                model.addAttribute("pieces", pieces);
//            } catch (Exception e) {
//                model.addAttribute("error", "Error fetching data. Please check the Item ID.");
//            }
//        }
//        return "itemsearch"; // This should match your HTML page name
//    }
//}
