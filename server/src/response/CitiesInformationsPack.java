
package response;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import utils.GeographicalCityDescriptor;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonSerialize(using = CitiesInformationsPack.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CitiesInformationsPack extends JsonSerializer<CitiesInformationsPack> {

    List<GeographicalCityDescriptor> cities;

    @Override
    public void serialize(
        CitiesInformationsPack citiesInformationsPack,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        gen.writeStartArray();
        for (GeographicalCityDescriptor city : citiesInformationsPack.getCities()) {
            gen.writeString(city.getName());
        }
        gen.writeEndArray();
    }
}