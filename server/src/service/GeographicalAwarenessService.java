package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import exceptions.GeographicalAwarenessFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import exceptions.UnrecognizedCountryException;
import utils.GeographicalCityDescriptor;
import utils.GeographicalCountryDescriptor;

@Service
public class GeographicalAwarenessService {
    
    private final String key;
    private final Gson gson = new Gson();

    public GeographicalAwarenessService(@Value("${geo.api.key}") String key){
        this.key = key;
    }

    @Cacheable(value = "countries")
    public List<GeographicalCountryDescriptor> fetchEuropeanCountries() {
        try {
            String jsonString = fetchEuropeanCountriesRaw();
            GeographicalCountryDescriptor[] countries = gson.fromJson(jsonString, GeographicalCountryDescriptor[].class);
            List<GeographicalCountryDescriptor> europeanCountries = Arrays.asList(countries);
            return europeanCountries;
        }
        catch (Exception e) {
            throw new RuntimeException("Error while fetching European countries", e);
        }
    }

    @Cacheable(value = "cities", key = "#country_code")
    public List<GeographicalCityDescriptor> fetchCitiesFromCountry(String country_code)
        throws UnrecognizedCountryException 
    {
        try {
            String jsonString = fetchCitiesFromCountryRaw(country_code);
            GeographicalCityDescriptor[] countries = gson.fromJson(jsonString, GeographicalCityDescriptor[].class);
            return Arrays.asList(countries);
        }
        catch (Exception e) {
            throw new UnrecognizedCountryException();
        }
    }

    private String fetchEuropeanCountriesRaw()
        throws
            GeographicalAwarenessFailureException
    {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.apilayer.com/geo/country/region/EU"))
                .GET()
                .header("apikey", key)
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GeographicalAwarenessFailureException();
        }
        catch (Exception e) {
            throw new GeographicalAwarenessFailureException();
        }
    }

    private String fetchCitiesFromCountryRaw(String country_code)
        throws
            UnrecognizedCountryException,
            GeographicalAwarenessFailureException
    {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.apilayer.com/geo/country/cities/" + country_code))
                        .GET()
                        .header("apikey", key)
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GeographicalAwarenessFailureException();
        }
        catch (Exception e) {
            throw new UnrecognizedCountryException();
        }
    }

    public boolean checkThatCityBelongsToCountry(String countryCode, String cityName) {
        try {
            List<GeographicalCityDescriptor> cities = fetchCitiesFromCountry(countryCode);
            for (GeographicalCityDescriptor city : cities) {
                if (city.getName().equals(cityName)) {
                    return true;
                }
            }
            return false;
        } catch (UnrecognizedCountryException e) {
            return false;
        }
    }
}