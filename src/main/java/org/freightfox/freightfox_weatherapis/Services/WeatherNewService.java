package org.freightfox.freightfox_weatherapis.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.freightfox.freightfox_weatherapis.Dto.WeatherInfoDto;
import org.freightfox.freightfox_weatherapis.Entities.WeatherInfo;
import org.freightfox.freightfox_weatherapis.Repositeries.WeatherInfoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class WeatherNewService {
    @Value("${openweather.api.key}")
    private String apiKey;

    @Autowired
    private WeatherCacheService weatherCacheService; // Inject WeatherCacheService
    private final ModelMapper modelMapper;

    private final WeatherInfoRepository weatherInfoRepository;


    public WeatherNewService(ModelMapper modelMapper, WeatherInfoRepository weatherInfoRepository) {
        this.modelMapper = modelMapper;
        this.weatherInfoRepository = weatherInfoRepository;

    }

    public WeatherInfoDto getWeather(Integer pincode, LocalDate date) throws JsonProcessingException {

        // Check for cached weather information by pincode and date
        WeatherInfoDto cachedWeatherInfo = weatherCacheService.getWeatherCombination(pincode, date);
        if (cachedWeatherInfo!=null) {
            return cachedWeatherInfo;
        }
        WeatherInfo byPincodeAndDate = weatherInfoRepository.findByPincodeAndDate(pincode, date);
        if(byPincodeAndDate != null) {return modelMapper.map(byPincodeAndDate, WeatherInfoDto.class);}

        // Cache miss: Fetch and cache weather information
        String pincodeLocation = weatherCacheService.getPincodeLocation(pincode);
        String[] locationParts = pincodeLocation.split(",");
        double latitude = Double.parseDouble(locationParts[0]);
        double longitude = Double.parseDouble(locationParts[1]);

        String weatherData = weatherCacheService.getWeatherByLatLongAndDate(latitude, longitude, date);

        // Process and save data (optional, assuming this happens in WeatherCacheService)
        // ...

        // Map to WeatherInfoDto and return
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(weatherData);

        WeatherInfo weatherInfo = new WeatherInfo();

        weatherInfo.setDescription(node.get("weather").get(0).get("description").asText());
        weatherInfo.setPincode(pincode);
        weatherInfo.setHumidity(node.get("main").get("humidity").asLong());
        weatherInfo.setDate(date);
        weatherInfo.setTemperature(node.get("main").get("temp").asDouble());
        weatherInfo.setPressure(node.get("main").get("pressure").asDouble());
        weatherInfo.setWindSpeed(node.get("wind").get("speed").asDouble());
        weatherInfo.setCityName(node.get("name").asText());
        weatherInfoRepository.save(weatherInfo);

        return modelMapper.map(weatherInfo, WeatherInfoDto.class);
    }
}

