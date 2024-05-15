package response;

import java.util.List;

import entity.Category;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.CategoryPackSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = CategoryPackSerializer.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class CategoryPack {
    
    private List<Category> categories;
}
