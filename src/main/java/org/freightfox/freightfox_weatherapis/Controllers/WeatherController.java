package org.freightfox.freightfox_weatherapis.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.freightfox.freightfox_weatherapis.Dto.WeatherInfoDto;
import org.freightfox.freightfox_weatherapis.Services.WeatherNewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class WeatherController {

    // made it immutable so that i cant lead to any Race Condition.
    private final WeatherNewService weatherNewService;




    public WeatherController(WeatherNewService weatherNewService) {
        this.weatherNewService = weatherNewService;
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherInfoDto> getWeather(@RequestParam Integer pincode, @RequestParam LocalDate for_date) throws JsonProcessingException {
        WeatherInfoDto weather = weatherNewService.getWeather(pincode, for_date);
        return new ResponseEntity<>(weather ,HttpStatus.OK);
     }


}
