package response;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonSerialize(using = URLResponse.class)
@Getter @AllArgsConstructor
@Setter @NoArgsConstructor
public class URLResponse extends JsonSerializer<URLResponse> {

    private URL url;

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
