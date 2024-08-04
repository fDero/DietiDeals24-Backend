package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.CategoryRepository;
import entity.Category;
import java.util.List;

@Service
public class MetadataGatheringService {
    
    private final CategoryRepository categoryRepository;

    @Autowired
    public MetadataGatheringService(
        CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> fetchCategories() {
        return categoryRepository.findAll();
    }
}
