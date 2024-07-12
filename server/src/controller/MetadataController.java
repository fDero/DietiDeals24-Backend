package controller;


import java.util.List;
import org.springframework.http.ResponseEntity;
import entity.Category;
import exceptions.UnrecognizedCountryException;
import repository.CategoryRepository;
import response.CategoryPack;
import response.CitiesInformationsPack;
import response.CountriesInformationsPack;
import service.GeographicalAwarenessService;
import utils.GeographicalCityDescriptor;
import utils.GeographicalCountryDescriptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetadataController {

    private final CategoryRepository categoryRepository;
    private final GeographicalAwarenessService countryDescriptor;

    @Autowired
    public MetadataController(
        CategoryRepository categoryRepository,
        GeographicalAwarenessService countryDescriptor
    ){
        this.categoryRepository = categoryRepository;
        this.countryDescriptor = countryDescriptor;
    }

    @GetMapping(value = "/categories", produces = "application/json")
    public ResponseEntity<CategoryPack> sendProfileInformations() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok().body(new CategoryPack(categories));
    }

    @GetMapping(value = "/countries", produces = "application/json")
    public ResponseEntity<CountriesInformationsPack> sendCountriesInformations() {
        List<GeographicalCountryDescriptor> countries = countryDescriptor.fetchEuropeanCountries();
        return ResponseEntity.ok().body(new CountriesInformationsPack(countries));
    }

    @GetMapping(value = "/cities", produces = "application/json")
    public ResponseEntity<CitiesInformationsPack> sendCitiesInformations(@RequestParam String country) 
        throws UnrecognizedCountryException 
    {
        List<GeographicalCityDescriptor> cities = countryDescriptor.fetchCitiesFromCountry(country);
        return ResponseEntity.ok().body(new CitiesInformationsPack(cities));
    }
}
