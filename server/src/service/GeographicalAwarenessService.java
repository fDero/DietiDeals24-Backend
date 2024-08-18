package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import exceptions.UnrecognizedCountryException;
import utils.GeographicalCityDescriptor;
import utils.GeographicalCountryDescriptor;

@Service
public class GeographicalAwarenessService {
    
    private final String key;
    private final Gson gson = new Gson();

    List<GeographicalCountryDescriptor> europeanCountries;
    Timestamp europeanCountriesLastUpdate = null;

    HashMap<String, List<GeographicalCityDescriptor>> citiesByCountry = new HashMap<>();
    HashMap<String, Timestamp> citiesByCountryLastUpdate = new HashMap<>();

    public GeographicalAwarenessService(@Value("${geo.api.key}") String key){
        this.key = key;
    }
    
    public List<GeographicalCountryDescriptor> fetchEuropeanCountries() {
        Timestamp oldestAcceptableUpateTimestamp = new Timestamp(System.currentTimeMillis() - 60 * 60);
        if (europeanCountriesLastUpdate != null && europeanCountriesLastUpdate.after(oldestAcceptableUpateTimestamp)) {
            return europeanCountries;
        }
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
            europeanCountries = Arrays.asList(countries);
            europeanCountriesLastUpdate = new Timestamp(System.currentTimeMillis());
            return europeanCountries;
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching European countries", e);
        }
    }

    public List<GeographicalCityDescriptor> fetchCitiesFromCountry(String country_code)
        throws UnrecognizedCountryException 
    {
        Timestamp oldestAcceptableUpateTimestamp = new Timestamp(System.currentTimeMillis() - 60 * 60);
        if (citiesByCountryLastUpdate.containsKey(country_code) && citiesByCountryLastUpdate.get(country_code).after(oldestAcceptableUpateTimestamp)) {
            return citiesByCountry.get(country_code);
        }
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
            List<GeographicalCityDescriptor> res = Arrays.asList(countries);
            citiesByCountry.put(country_code, res);
            citiesByCountryLastUpdate.put(country_code, new Timestamp(System.currentTimeMillis()));
            return res;
        }
        catch (Exception e) {
            throw new UnrecognizedCountryException();
        }
    }

    public boolean checkThatCityBelogsToCountry(String country_code, String city_name) {
        try {
            List<GeographicalCityDescriptor> cities = fetchCitiesFromCountry(country_code);
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
