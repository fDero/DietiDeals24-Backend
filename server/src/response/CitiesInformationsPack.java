
package response;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import json.CitiesInformationsPackSerializer;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import utils.GeographicalCityDescriptor;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@JsonSerialize(using = CitiesInformationsPackSerializer.class)
public class CitiesInformationsPack {

    List<GeographicalCityDescriptor> cities;
}