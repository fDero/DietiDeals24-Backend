package json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import response.URLResponse;

import java.io.IOException;

public class URLResponseSerializer extends JsonSerializer<URLResponse> {

    @Override
    public void serialize(
        URLResponse urlResponse,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        gen.writeStartObject();
        gen.writeStringField("url", urlResponse.getUrl().toString());
        gen.writeEndObject();
    }
}
