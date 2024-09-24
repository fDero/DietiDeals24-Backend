package response;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import utils.GeographicalCountryDescriptor;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@JsonSerialize(using = CountriesInformationsPack.class)
public class CountriesInformationsPack extends JsonSerializer<CountriesInformationsPack> {

    List<GeographicalCountryDescriptor> countries;

    @Override
    public void serialize(
        CountriesInformationsPack countriesInformationsPack,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        gen.writeStartArray();
        for (GeographicalCountryDescriptor country : countriesInformationsPack.getCountries()) {
            gen.writeStartObject();
            gen.writeStringField("name", country.getName());
            gen.writeStringField("nativeName", country.getNative_name());
            gen.writeStringField("code", getCountryCode(country));
            gen.writeStringField("flag", country.getFlag());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }

    String getCountryCode(GeographicalCountryDescriptor country){
        String countryCode = country.getAlpha2code();
        if (countryCode != null && !countryCode.isEmpty())
            return countryCode;
        return country.getAlpha3code();
    }
}
