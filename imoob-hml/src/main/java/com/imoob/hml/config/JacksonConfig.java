package com.imoob.hml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.model.Route;
import com.imoob.hml.repository.RealEstateRepository;
import com.imoob.hml.repository.RouteRepository;
import com.imoob.hml.service.serializers.CustomRealEstateDeserializer;
import com.imoob.hml.service.serializers.CustomRealEstateSerializer;
import com.imoob.hml.service.serializers.CustomRouteDeserializer;
import com.imoob.hml.service.serializers.CustomRouteSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JacksonConfig {
    private final RealEstateRepository realEstateRepository;
    private final RouteRepository routeRepository;
	
	@Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build()
        		.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        mapper.registerModule(new JavaTimeModule());
        
        SimpleModule module = new SimpleModule();
        module.addSerializer(RealEstate.class, new CustomRealEstateSerializer(realEstateRepository, mapper));
        module.addDeserializer(RealEstate.class, new CustomRealEstateDeserializer(realEstateRepository));
        module.addSerializer(Route.class, new CustomRouteSerializer(routeRepository, mapper));
        module.addDeserializer(Route.class, new CustomRouteDeserializer(routeRepository));
        
        
        mapper.registerModule(module);
        
        
        return mapper;
    }
}