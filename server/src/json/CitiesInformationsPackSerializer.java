package json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import response.CitiesInformationsPack;
import utils.GeographicalCityDescriptor;

public class CitiesInformationsPackSerializer extends JsonSerializer<CitiesInformationsPack> {

    @Override
    public void serialize(
        CitiesInformationsPack citiesInformationsPack, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws IOException 
    {
        gen.writeStartArray();
        for (GeographicalCityDescriptor city : citiesInformationsPack.getCities()) {
            gen.writeString(city.getName());
        }
        gen.writeEndArray();
    }
}
