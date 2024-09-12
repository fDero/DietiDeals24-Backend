package response;

import java.util.List;

import entity.Category;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.TrendingCategoryPackSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = TrendingCategoryPackSerializer.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class TrendingCategoryPack {
    
    private List<Category> categories;
}
