package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final ItemService itemService;

    @Autowired
    public CategoryController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        try {
            List<String> categories = new ArrayList<>(itemService.getMainCategories());
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/subcategories/{mainCategory}")
    public ResponseEntity<List<String>> getSubcategories(@PathVariable String mainCategory) {
        try {
            List<String> subcategories = new ArrayList<>(itemService.getSubcategoriesByMainCategory(mainCategory));
            return ResponseEntity.ok(subcategories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
