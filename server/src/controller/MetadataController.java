package controller;


import java.util.List;
import org.springframework.http.ResponseEntity;
import entity.Category;
import repository.CategoryRepository;
import response.CategoryPack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetadataController {

    private final CategoryRepository categoryRepository;

    @Autowired
    public MetadataController(
        CategoryRepository categoryRepository
    ){
        this.categoryRepository = categoryRepository;
    }

    @GetMapping(value = "/categories", produces = "application/json")
    public ResponseEntity<CategoryPack> sendProfileInformations() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok().body(new CategoryPack(categories));
    }
}
