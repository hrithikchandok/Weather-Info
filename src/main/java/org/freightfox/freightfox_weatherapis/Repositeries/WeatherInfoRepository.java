package org.freightfox.freightfox_weatherapis.Repositeries;

import org.freightfox.freightfox_weatherapis.Entities.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long> {
    WeatherInfo findByPincodeAndDate(Integer pincode, LocalDate date);
}
