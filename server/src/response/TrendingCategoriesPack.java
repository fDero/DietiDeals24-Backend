package response;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Category;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = TrendingCategoriesPack.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class TrendingCategoriesPack extends JsonSerializer<TrendingCategoriesPack> {
    
    private List<Category> categories;

    @Override
    public void serialize(
        TrendingCategoriesPack trendingCategoryPack,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        gen.writeStartArray();
        for (Category category : trendingCategoryPack.getCategories()) {
            gen.writeString(category.getItemCategory());
        }
        gen.writeEndArray();
    }
}
