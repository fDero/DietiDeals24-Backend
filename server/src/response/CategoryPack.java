package response;

import java.util.List;

import entity.Category;
import json.CategoryPackSerializer;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = CategoryPackSerializer.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class CategoryPack {
    
    private List<Category> categories;
}
