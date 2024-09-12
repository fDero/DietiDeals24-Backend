package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.CategoryRepository;
import entity.Category;
import java.util.List;

@Service
public class MetadataManagementService {
    
    private final CategoryRepository categoryRepository;

    @Autowired
    public MetadataManagementService(
        CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> fetchCategories() {
        return categoryRepository.findAll();
    }

    public boolean checkThatCategoryIsMacrocategory(String category, String macroCategory) {
        return fetchCategories().stream()
            .anyMatch(
                c -> c.getItemCategory().equals(category) && 
                c.getMacroCategory().equals(macroCategory)
            );
    }

    public String getMacroCategoryForCategory(String category) {
        return fetchCategories().stream()
            .filter(c -> c.getItemCategory().equals(category))
            .map(Category::getMacroCategory)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }
}
