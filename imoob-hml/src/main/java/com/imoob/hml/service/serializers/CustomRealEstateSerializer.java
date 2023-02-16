package com.imoob.hml.service.serializers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.model.DTO.realEstate.RealEstateDTO;
import com.imoob.hml.repository.RealEstateRepository;

public class CustomRealEstateSerializer extends StdSerializer<RealEstate> {

	@Autowired
    private RealEstateRepository realEstateRepository;
	
	@Autowired
	private ObjectMapper objectMapper;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomRealEstateSerializer() {
		this(RealEstate.class);
	}
	
	public CustomRealEstateSerializer(RealEstateRepository realEstateRepository, ObjectMapper mapper) {
		this(RealEstate.class);

	    this.realEstateRepository = realEstateRepository;
	    this.objectMapper = mapper;

	}

	public CustomRealEstateSerializer(Class<RealEstate> t) {
		super(t);
	}

	@Override
	public void serialize(RealEstate realEstate, JsonGenerator jsonGenerator, SerializerProvider serializers)
			throws IOException {
//		jsonGenerator.writeStartObject();
//		jsonGenerator.writeNumberField("id", realEstate.getId());
//		jsonGenerator.writeStringField("corporateName", realEstate.getCorporateName());
//		// ... Adicione outros campos relevantes aqui
//		jsonGenerator.writeEndObject();
		
//		 this.objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
//		    RealEstate realEstate2 = realEstateRepository.findById(realEstate.getId()).orElse(null);
//		    objectMapper.writeValue(jsonGenerator, realEstate2);
//		    this.objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true);


		RealEstateDTO realEstateDTO = new RealEstateDTO(realEstate);
        objectMapper.writeValue(jsonGenerator, realEstateDTO);


	}

}
