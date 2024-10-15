package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.JsonEncodingDecodingException;
import org.springframework.stereotype.Component;


@Component
public class JsonConverter {

    private final ObjectMapper mapper = new ObjectMapper();

    public String encode(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            throw new JsonEncodingDecodingException(object, exception);
        }
    }

    public <T> T decode(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException exception) {
            throw new JsonEncodingDecodingException(json, exception);
        }
    }
}
