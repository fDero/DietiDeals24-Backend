package response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import json.TrendingCategoryPackSerializer;
import json.URLResponseSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;

@Getter @AllArgsConstructor
@Setter @NoArgsConstructor
@JsonSerialize(using = URLResponseSerializer.class)
public class URLResponse {

    private URL url;
}
