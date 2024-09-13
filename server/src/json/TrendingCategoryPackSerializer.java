package json;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Category;
import response.TrendingCategoryPack;


public class TrendingCategoryPackSerializer extends JsonSerializer<TrendingCategoryPack> {

    @Override
    public void serialize(
        TrendingCategoryPack trendingCategoryPack, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException 
    {
        gen.writeStartArray();
        for (Category category : trendingCategoryPack.getCategories()) {
            gen.writeString(category.getItemCategory());
        }
        gen.writeEndArray();
    }
}
