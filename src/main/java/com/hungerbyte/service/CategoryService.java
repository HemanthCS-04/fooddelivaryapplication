package com.hungerbyte.service;

import com.hungerbyte.entity.Category;
import com.hungerbyte.exception.ResourceNotFoundException;
import com.hungerbyte.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updated) {
        Category existing = getCategoryById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        if (updated.getImageUrl() != null) {
            existing.setImageUrl(updated.getImageUrl());
        }
        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        Category existing = getCategoryById(id);
        existing.setIsActive(false);
        categoryRepository.save(existing);
    }
}
