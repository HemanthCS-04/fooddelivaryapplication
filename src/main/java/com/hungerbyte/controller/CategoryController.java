package com.hungerbyte.controller;

import com.hungerbyte.dto.ApiResponse;
import com.hungerbyte.entity.Category;
import com.hungerbyte.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.ok("Categories retrieved", categoryService.getAllCategories()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Category retrieved", categoryService.getCategoryById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody Category category) {
        return ResponseEntity.ok(ApiResponse.ok("Category created", categoryService.createCategory(category)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return ResponseEntity.ok(ApiResponse.ok("Category updated", categoryService.updateCategory(id, category)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.ok("Category deleted"));
    }
}
