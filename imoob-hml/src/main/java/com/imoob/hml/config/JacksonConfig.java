package com.imoob.hml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.repository.RealEstateRepository;
import com.imoob.hml.service.CustomRealEstateDeserializer;
import com.imoob.hml.service.CustomRealEstateSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JacksonConfig {
    private final RealEstateRepository repository;
	
	
	@Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build()
        		.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        mapper.registerModule(new JavaTimeModule());
        
        SimpleModule module = new SimpleModule();
        module.addSerializer(RealEstate.class, new CustomRealEstateSerializer(repository, mapper));
        module.addDeserializer(RealEstate.class, new CustomRealEstateDeserializer(repository));

        mapper.registerModule(module);
        
        
        return mapper;
    }
}