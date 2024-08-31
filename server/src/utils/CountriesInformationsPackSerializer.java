package utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import response.CountriesInformationsPack;

public class CountriesInformationsPackSerializer extends JsonSerializer<CountriesInformationsPack> {

    @Override
    public void serialize(CountriesInformationsPack value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException 
    {
        System.out.println("AQUI");
        gen.writeStartArray();
        for (GeographicalCountryDescriptor country : value.getCountries()) {
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
