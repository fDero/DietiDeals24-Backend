package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import exceptions.UnrecognizedCountryException;
import response.CitiesInformationsPack;
import response.CountriesInformationsPack;
import utils.GeographicalCityDescriptor;
import utils.GeographicalCountryDescriptor;

@Service
public class GeographicalAwarenessService {
    
    private final String key;
    private final Gson gson = new Gson();

    public GeographicalAwarenessService(@Value("${geo.api.key}") String key){
        this.key = key;
    }
    
    @Cacheable(value = "europeanCountries")
    public CountriesInformationsPack fetchEuropeanCountries() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.apilayer.com/geo/country/region/EU"))
                .GET()
                .header("apikey", key)
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonString = response.body();
            System.out.println(jsonString);
            GeographicalCountryDescriptor[] countries = gson.fromJson(jsonString, GeographicalCountryDescriptor[].class);
            List<GeographicalCountryDescriptor> europeanCountries = Arrays.asList(countries);
            CountriesInformationsPack pack = new CountriesInformationsPack(europeanCountries);
            return pack;
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching European countries", e);
        }
    }

    @Cacheable(value = "citiesByCountry", key = "#country_code")
    public CitiesInformationsPack fetchCitiesFromCountry(String country_code)
        throws UnrecognizedCountryException 
    {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.apilayer.com/geo/country/cities/" + country_code))
                .GET()
                .header("apikey", key)
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonString = response.body();
            GeographicalCityDescriptor[] countries = gson.fromJson(jsonString, GeographicalCityDescriptor[].class);
            List<GeographicalCityDescriptor> cities = Arrays.asList(countries);
            CitiesInformationsPack pack = new CitiesInformationsPack(cities);
            return pack;
        }
        catch (Exception e) {
            throw new UnrecognizedCountryException();
        }
    }

    public boolean checkThatCityBelogsToCountry(String country_code, String city_name) {
        try {
            CitiesInformationsPack pack = fetchCitiesFromCountry(country_code);
            List<GeographicalCityDescriptor> cities = pack.getCities();
            for (GeographicalCityDescriptor city : cities) {
                if (city.getName().equals(city_name)) {
                    return true;
                }
            }
            return false;
        } catch (UnrecognizedCountryException e) {
            return false;
        }
    }
}
