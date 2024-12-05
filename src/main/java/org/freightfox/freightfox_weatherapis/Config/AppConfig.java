package org.freightfox.freightfox_weatherapis.Config;


import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class AppConfig
{

    @Bean
    public ModelMapper modelMapper(){return new ModelMapper();}
    @Bean
    public RestTemplate restTemplate() {return new RestTemplate();}
}
