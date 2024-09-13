package json;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import entity.Category;
import response.CategoryPack;


public class CategoryPackSerializer extends JsonSerializer<CategoryPack> {

    @Override
    public void serialize(
        CategoryPack categoryPack, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException 
    {
        Map<String, ArrayList<String>> categories = new TreeMap<String, ArrayList<String>>();
        for (Category category : categoryPack.getCategories()) {
            categories.putIfAbsent(category.getMacroCategory(), new ArrayList<String>());
            categories.get(category.getMacroCategory()).add(category.getItemCategory());
        }
        gen.writeStartObject();
        for (String macroCategory : categories.keySet()) {
            gen.writeArrayFieldStart(macroCategory);
            for (String itemCategory : categories.get(macroCategory)) {
                gen.writeString(itemCategory);
            }
            gen.writeEndArray();
        }
        gen.writeEndObject();
    }
}
