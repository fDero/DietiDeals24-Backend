package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import exceptions.GeographicalAwarenessFailureException;
import com.google.gson.Gson;
import exceptions.UnrecognizedCountryException;
import utils.GeographicalCityDescriptor;
import utils.GeographicalCountryDescriptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
            return Arrays.asList(countries);
        }
        catch (Exception e) {
            throw new GeographicalAwarenessFailureException();
        }
    }

    @Cacheable(value = "cities", key = "#countryCode")
    public List<GeographicalCityDescriptor> fetchCitiesFromCountry(String countryCode)
        throws UnrecognizedCountryException 
    {
        try {
            String jsonString = fetchCitiesFromCountryRaw(countryCode);
            GeographicalCityDescriptor[] countries = gson.fromJson(jsonString, GeographicalCityDescriptor[].class);
            return Arrays.asList(countries);
        }
        catch (Exception e) {
            throw new UnrecognizedCountryException();
        }
    }

    private String fetchEuropeanCountriesRaw() {
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

    private String fetchCitiesFromCountryRaw(String countryCode)
        throws
            UnrecognizedCountryException
    {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.apilayer.com/geo/country/cities/" + countryCode))
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