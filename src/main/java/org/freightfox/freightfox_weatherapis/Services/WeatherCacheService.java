package org.freightfox.freightfox_weatherapis.Services;

import org.freightfox.freightfox_weatherapis.Dto.Geo;
import org.freightfox.freightfox_weatherapis.Dto.WeatherInfoDto;

import org.freightfox.freightfox_weatherapis.Entities.WeatherInfo;

import org.freightfox.freightfox_weatherapis.Repositeries.WeatherInfoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;


@Service
public class WeatherCacheService {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Autowired
    private ModelMapper modelMapper;
    private final WeatherInfoRepository weatherInfoRepository;
    private final RestTemplate restTemplate;

    public WeatherCacheService( WeatherInfoRepository weatherInfoRepository, RestTemplate restTemplate, ModelMapper modelMapper) {
        this.weatherInfoRepository = weatherInfoRepository;
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "weatherInfo", key = "#pincode + '-' + #date")
    public WeatherInfoDto getWeatherCombination(Integer pincode, LocalDate date) {
        System.out.println("Fetching weather info from cache for pincode: " + pincode + " and date: " + date);
        WeatherInfo weatherInfo = weatherInfoRepository.findByPincodeAndDate(pincode, date);
        if (weatherInfo != null) {
            System.out.println("Fetched weather info from database: " + weatherInfo);
        } else {
            System.out.println("Weather info not found in database");
            return null;
        }
        return modelMapper.map(weatherInfo, WeatherInfoDto.class);
    }

    @Cacheable(value = "pincodeLocation", key = "#pincode")
    public String getPincodeLocation(Integer pincode) {
        System.out.println("Pincode location not there");
        double latitude;
        double longitude;


            String url = "https://api.openweathermap.org/geo/1.0/zip?zip=" + pincode + ",in&appid=" + apiKey;
            Geo forEntity = restTemplate.getForObject(url, Geo.class);
            latitude = forEntity.getLat();
            longitude = forEntity.getLon();


        return latitude + "," + longitude;
    }

    @Cacheable(value = "weatherCache", key = "#latitude + '_' + #longitude + '_' + #date")
    public String getWeatherByLatLongAndDate(double latitude, double longitude, LocalDate date) {
        System.out.println("combination of lat , long and date is not there");
        long unixTimestamp = date.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&dt=" + unixTimestamp;
        return restTemplate.getForEntity(url, String.class).getBody();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherCacheService)) return false;
        WeatherCacheService that = (WeatherCacheService) o;
        return Objects.equals(apiKey, that.apiKey) && Objects.equals(modelMapper, that.modelMapper) && Objects.equals(weatherInfoRepository, that.weatherInfoRepository) && Objects.equals(restTemplate, that.restTemplate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiKey, modelMapper, weatherInfoRepository, restTemplate);
    }
}