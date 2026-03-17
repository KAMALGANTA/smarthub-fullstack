package com.noticeboard.controller;

import com.noticeboard.model.Category;
import com.noticeboard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");
        if (name == null || name.isBlank())
            throw new RuntimeException("Category name is required");
        return ResponseEntity.status(201).body(categoryService.createCategory(name, description));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id,
                                                    @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(Map.of("message", "Category deactivated successfully"));
    }
}