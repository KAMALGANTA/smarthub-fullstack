package com.noticeboard.service;

import com.noticeboard.model.Category;
import com.noticeboard.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getActiveCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public Category createCategory(String name, String description) {
        if (categoryRepository.existsByName(name))
            throw new RuntimeException("Category already exists: " + name);

        return categoryRepository.save(Category.builder()
                .name(name)
                .description(description)
                .isActive(true)
                .build());
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category existing = getCategoryById(id);
        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());
        existing.setActive(updatedCategory.isActive());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        category.setActive(false);
        categoryRepository.save(category);
    }
}