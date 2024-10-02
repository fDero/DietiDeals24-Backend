package response;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonSerialize(using = OAuthDebugInformations.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class OAuthDebugInformations extends JsonSerializer<OAuthDebugInformations> {
    
    String emial;
    String id;

    @Override
    public void serialize(
        OAuthDebugInformations value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws 
            IOException 
    {
        gen.writeStartObject();
        gen.writeStringField("email", value.getEmial());
        gen.writeStringField("id", value.getId());
        gen.writeEndObject();
    }
}
