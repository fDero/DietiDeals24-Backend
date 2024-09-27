package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class GeographicalCountryDescriptor {
    String name;
    String alpha2code;
    String alpha3code;
    String nativeName;
    String flag;
}