package response;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import utils.CountriesInformationsPackSerializer;
import utils.GeographicalCountryDescriptor;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@JsonSerialize(using = CountriesInformationsPackSerializer.class)
public class CountriesInformationsPack {

    List<GeographicalCountryDescriptor> countries;
}
