package controller;


import java.util.List;
import org.springframework.http.ResponseEntity;
import entity.Category;
import exceptions.UnrecognizedCountryException;
import response.CategoryPack;
import response.CitiesInformationsPack;
import response.CountriesInformationsPack;
import service.GeographicalAwarenessService;
import service.MetadataGatheringService;
import utils.GeographicalCityDescriptor;
import utils.GeographicalCountryDescriptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetadataController {

    private final MetadataGatheringService metadataGatheringService;
    private final GeographicalAwarenessService countryDescriptor;

    @Autowired
    public MetadataController(
        MetadataGatheringService metadataGatheringService,
        GeographicalAwarenessService countryDescriptor
    ){
        this.metadataGatheringService = metadataGatheringService;
        this.countryDescriptor = countryDescriptor;
    }

    @GetMapping(value = "/categories", produces = "application/json")
    public ResponseEntity<CategoryPack> sendProfileInformations() {
        List<Category> categories = metadataGatheringService.fetchCategories();
        return ResponseEntity.ok().body(new CategoryPack(categories));
    }

    @GetMapping(value = "/countries", produces = "application/json")
    public ResponseEntity<CountriesInformationsPack> sendCountriesInformations() {
        List<GeographicalCountryDescriptor> countries = countryDescriptor.fetchEuropeanCountries();
        CountriesInformationsPack countriesPack = new CountriesInformationsPack(countries);
        return ResponseEntity.ok().body(countriesPack);
    }

    @GetMapping(value = "/cities", produces = "application/json")
    public ResponseEntity<CitiesInformationsPack> sendCitiesInformations(@RequestParam String country) 
        throws UnrecognizedCountryException 
    {
        List<GeographicalCityDescriptor> cities = countryDescriptor.fetchCitiesFromCountry(country);
        CitiesInformationsPack citiesPack = new CitiesInformationsPack(cities);
        return ResponseEntity.ok().body(citiesPack);
    }
}
