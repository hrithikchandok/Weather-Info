package org.freightfox.freightfox_weatherapis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FreightFoxWeatherApIsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreightFoxWeatherApIsApplication.class, args);
    }

}
