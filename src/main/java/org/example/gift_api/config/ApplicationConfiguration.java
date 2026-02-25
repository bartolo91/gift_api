package org.example.gift_api.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ModelMapper modelMapper(Set<Converter> allConverters) {
        ModelMapper modelMapper = new ModelMapper();
        allConverters.forEach(modelMapper::addConverter);
        return modelMapper;
    }
}
