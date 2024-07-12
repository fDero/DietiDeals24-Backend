package service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import exceptions.UnrecognizedCountryException;
import utils.GeographicalCityDescriptor;
import utils.GeographicalCountryDescriptor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class GeographicalAwarenessService {
    
    private final String key;
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private final Gson gson = new Gson();

    public GeographicalAwarenessService(@Value("${geo.api.key}") String key){
        this.key = key;
    }

    public List<GeographicalCountryDescriptor> fetchEuropeanCountries() {
        try {
            Request request = new Request.Builder()
                .url("https://api.apilayer.com/geo/country/region/EU")
                .addHeader("apikey", key)
                .method("GET", null)
                .build();
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            System.out.println(jsonString);
            GeographicalCountryDescriptor[] countries = gson.fromJson(jsonString, GeographicalCountryDescriptor[].class);
            return Arrays.asList(countries);
        } catch (IOException e) {
            throw new RuntimeException("Error while fetching European countries", e);
        }
    }

    public List<GeographicalCityDescriptor> fetchCitiesFromCountry(String country_code)
        throws UnrecognizedCountryException 
    {
        try {
            Request request = new Request.Builder()
                .url("https://api.apilayer.com/geo/country/cities/" + country_code)
                .addHeader("apikey", key)
                .method("GET", null)
                .build();
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            GeographicalCityDescriptor[] countries = gson.fromJson(jsonString, GeographicalCityDescriptor[].class);
            return Arrays.asList(countries);
        }
        catch (IOException e) {
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
