package com.imoob.hml.service.serializers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.repository.RealEstateRepository;
import com.imoob.hml.service.exceptions.ResourceNotFoundException;

public class CustomRealEstateDeserializer extends StdDeserializer<RealEstate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Autowired
	private RealEstateRepository realEstateRepository;

//	@Autowired
//	private ObjectMapper objectMapper;

	public CustomRealEstateDeserializer(Class<?> vc) {
		super(vc);
	}

	public CustomRealEstateDeserializer(RealEstateRepository realEstateRepository) {
	        this(RealEstate.class);
	        this.realEstateRepository = realEstateRepository;
	    }


	@Override
	public RealEstate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
//		Long id = p.getValueAsLong();
//        return realEstateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, RealEstate.class));
		
		
		
//		ObjectMapper mapper = new ObjectMapper();
//		RealEstate realEstate = mapper.readValue(p, RealEstate.class);
//
//		String id = p.getValueAsString();
//		if (id == null || id.equals("0")) {
//		    return realEstate;
//		}
//		Long realEstateId = Long.parseLong(id);
//		return realEstateRepository.findById(realEstateId).orElseThrow(() -> new ResourceNotFoundException(realEstateId, RealEstate.class));

		ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

		RealEstate realEstate = mapper.readValue(p, RealEstate.class);
	
		String id = p.getValueAsString();
	    if (id == null || id.equals("0")) {
	        return realEstate;
	    }
	    Long realEstateId = Long.parseLong(id);
	    return realEstateRepository.findById(realEstateId).orElseThrow(() -> new ResourceNotFoundException(realEstateId, RealEstate.class));
		
		
		
//		ObjectMapper mapper = new ObjectMapper();
//        ObjectCodec codec = p.getCodec();
//
//		JsonNode node = codec.readTree(p);
//        Long id = node.asLong();
//		return realEstateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, RealEstate.class));

	}

}
