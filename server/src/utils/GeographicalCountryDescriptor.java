package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class GeographicalCountryDescriptor {
    private String name;
    private String alpha2code;
    private String alpha3code;
    private String nativeName;
    private String flag;
}