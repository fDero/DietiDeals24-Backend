package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@NoArgsConstructor
public class JsonConversionService {

    private final ObjectMapper mapper = new ObjectMapper();

    public String encode(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(
                "can't encode object because not json serializable: " +
                "source=" + object + " error=" + exception.getMessage()
            );
        }
    }

    public <T> T decode(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(
                "can't decode json because of a json error: " +
                "json=" + json + " error=" + exception.getMessage()
            );
        }
    }
}
